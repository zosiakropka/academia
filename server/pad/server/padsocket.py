"""@package pad.server.socket
@author: Zosia Sobocinska
@date Nov 2, 2013
"""

import struct
import hashlib
import re
import asyncore
from pad.endec import decode
import logging


class PadBaseSocket(asyncore.dispatcher_with_send):
    DATA_CHUNK_SIZE = 1024

    def __init__(self, baseServer, sock=None, mp=None):
        asyncore.dispatcher_with_send.__init__(self, sock=sock, map=mp)
        self.baseServer = baseServer
        self.buffer = ""
        self.pad = None

    def set_server(self, server):
        self.server = server

    def handle_close(self):
        logging.info(self.COMMUNICATION_CHANNEL + ": " + str(self.socket.getpeername()) + " disconnected")

    def handle_message(self, message):
        data = decode(message)
        logging.info(self.COMMUNICATION_CHANNEL + " received data: " + str(data))
        if "purpose" in data:
            self.baseServer.process_data(data)


class PadStandardSocket(PadBaseSocket, asyncore.dispatcher_with_send):

    DELIMITER = u"\u001E"
    COMMUNICATION_CHANNEL = "StandardSocket"
    
    def handle_read(self):
        logging.debug(self.COMMUNICATION_CHANNEL + " handle_read event ")
        data = self.recv(self.DATA_CHUNK_SIZE)
        if (data):
            self.buffer += data
            if self.DELIMITER in self.buffer:
                records = self.buffer.split(self.DELIMITER)
                self.buffer = records.pop()
                for record in records:
                    logging.info(self.COMMUNICATION_CHANNEL + " received " + str(record))
                    self.handle_message(record)
        else:
            self.close()


class PadWebSocket(PadBaseSocket, asyncore.dispatcher_with_send):

    HANDSHAKE = (
        "HTTP/1.1 101 Web Socket Protocol Handshake\r\n"
        "Upgrade: WebSocket\r\n"
        "Connection: Upgrade\r\n"
        "WebSocket-Origin: %(origin)s\r\n"
        "WebSocket-Location: ws://%(bind)s:%(port)s/\r\n"
        "Sec-Websocket-Origin: %(origin)s\r\n"
        "Sec-Websocket-Location: ws://%(bind)s:%(port)s/\r\n"
        "\r\n"
    )

    DELIMITER = "\xff"
    COMMUNICATION_CHANNEL = "WebSocket"

    def __init__(self, baseServer, sock=None, mp=None):
        PadBaseSocket.__init__(self, baseServer, sock=sock, mp=mp)
        self.handshaken = False
        self.header = ""
        PadBaseSocket.handle_read = self.handle_read()

    def handle_read(self):
        data = self.recv(self.DATA_CHUNK_SIZE)
        logging.debug(self.COMMUNICATION_CHANNEL + " received data: " + data)
        if (data):
            if not self.handshaken:
                self.header += data
                if self.header.find('\r\n\r\n') != -1:
                    parts = self.header.split('\r\n\r\n', 1)
                    self.buffer = parts.pop()
                    self.header = parts[0]
                    if self.dohandshake(self.header, parts[1]):
                        logging.info("Handshake successful")
                        self.handshaken = True
            else:
                self.buffer += data
                messages = self.buffer.split(self.DELIMITER)
                self.buffer = messages.pop()
                for msg in messages:
                    if msg[0] == '\x00':
                        self.handle_message(msg[1:])
        else:
            self.close()

    def dohandshake(self, header, key=None):
        logging.debug("Begin handshake: %s" % header)
        digitRe = re.compile(r'[^0-9]')
        spacesRe = re.compile(r'\s')
        part_1 = part_2 = origin = None
        for line in header.split('\r\n')[1:]:
            name, value = line.split(': ', 1)
            if name.lower() == "sec-websocket-key1":
                key_number_1 = int(digitRe.sub('', value))
                spaces_1 = len(spacesRe.findall(value))
                if spaces_1 == 0:
                    return False
                if key_number_1 % spaces_1 != 0:
                    return False
                part_1 = key_number_1 / spaces_1
            elif name.lower() == "sec-websocket-key2":
                key_number_2 = int(digitRe.sub('', value))
                spaces_2 = len(spacesRe.findall(value))
                if spaces_2 == 0:
                    return False
                if key_number_2 % spaces_2 != 0:
                    return False
                part_2 = key_number_2 / spaces_2
            elif name.lower() == "origin":
                origin = value
        if part_1 and part_2:
            logging.debug("Using challenge + response")
            challenge = struct.pack('!I', part_1) + struct.pack('!I', part_2) + key
            response = hashlib.md5(challenge).digest()
            handshake = PadWebSocket.HANDSHAKE + response
        else:
            logging.warning("Not using challenge + response")
            handshake = PadWebSocket.HANDSHAKE
        handshake = handshake % {'origin': origin, 'port': self.server.port,
                                    'bind': self.server.socketbind}
        logging.debug("Sending handshake %s" % handshake)
        self.out_buffer = handshake
        return True

    def handle_message(self, data):
        logging.info("Got message: %s" % data)

    def send(self, data):
        logging.info("Sent message: %s" % data)
        self.out_buffer = "\x00%s\xff" % data
