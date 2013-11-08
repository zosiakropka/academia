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
from hashlib import sha1


class PadBaseSocket(asyncore.dispatcher_with_send):
    DATA_CHUNK_SIZE = 1024

    CHNL = ""

    def __init__(self, sock=None, map=None):
        asyncore.dispatcher_with_send.__init__(self, sock=sock, map=map)
        sock.setblocking(0)
        self.buffer = ""
        self.pad = None
        self.server = None

    def set_server(self, server):
        self.server = server

    def handle_close(self):
        logging.debug("%s : %s disconnected" % (self.CHNL, str(self.socket.getpeername())))

    def handle_message(self, message):
        data = decode(message)
        if data and "purpose" in data:
            self.server.process_data(data)


class PadStandardSocket(PadBaseSocket, asyncore.dispatcher_with_send):

    DELIMITER = u"\u001E"
    CHNL = "StandardSocket"

    def handle_read(self):
        data = None
        try:
            data = self.recv(self.DATA_CHUNK_SIZE)
        except Exception:
            pass
        if (data):
            logging.debug("%s has data onboard: %s", self.CHNL, data)
            self.buffer += data
            if self.DELIMITER in self.buffer:
                records = self.buffer.split(self.DELIMITER)
                self.buffer = records.pop()
                for record in records:
                    self.handle_message(record)


class PadWebSocket(PadBaseSocket, asyncore.dispatcher_with_send):

    HANDSHAKE = (
        "HTTP/1.1 101 Web Socket Protocol Handshake\r\n"
        "Upgrade: WebSocket\r\n"
        "Connection: Upgrade\r\n"
        "WebSocket-Origin: %(origin)s\r\n"
        "WebSocket-Location: ws://%(bind)s:%(port)s/\r\n"
        "Sec-Websocket-Origin: %(origin)s\r\n"
        "Sec-Websocket-Location: ws://%(bind)s:%(port)s/\r\n"
    )
    HANDSHAKE_ACCEPT_UUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"
    HANDSHAKE_SEC_WEBSOCKET_ACCEPT_HEADER = "Sec-WebSocket-Accept: %s\r\n"

    DELIMITER = "\xff"
    CHNL = "WebSocket"

    def __init__(self, sock=None, map=None):
        PadBaseSocket.__init__(self, sock=sock, map=map)
        self.handshaken = False
        self.header = ""

    def handle_read(self):
        data = self.recv(self.DATA_CHUNK_SIZE)
        if (data):
            if not self.handshaken:
                self.header += data
                if self.header.find('\r\n\r\n') != -1: # TODO: check if "GET"
                    parts = self.header.split('\r\n\r\n', 1)
                    self.header = parts[0]
                    if self.dohandshake(self.header, parts[1]):
                        logging.info("Handshake successful")
                        self.handshaken = True
                    else:
                        self.close()
            else:
                self.buffer += data
                messages = self.buffer.split(self.DELIMITER)
                self.buffer = messages.pop()
                for msg in messages:
                    if msg[0] == '\x00':
                        self.handle_message(msg[1:])

    def dohandshake(self, header, key=None):
        logging.debug("Begin handshake: %s" % header)
        digitRe = re.compile(r'[^0-9]')
        spacesRe = re.compile(r'\s')
        part_1 = part_2 = origin = accept = None
        for line in header.split('\r\n')[1:]:
            name, value = line.split(': ', 1)
            if name.lower() == "sec-websocket-key":
                accept = sha1(value + self.HANDSHAKE_ACCEPT_UUID).digest().encode("base64")
#                 logging.error("Unsupported Sec-Websocket-Key")
#                 return False;
            elif name.lower() == "sec-websocket-key1":
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
        if accept:
            handshake = PadWebSocket.HANDSHAKE + self.HANDSHAKE_SEC_WEBSOCKET_ACCEPT_HEADER % accept
        elif part_1 and part_2:
            logging.debug("Using challenge + response")
            challenge = struct.pack('!I', part_1) + struct.pack('!I', part_2) + key
            response = hashlib.md5(challenge).digest()
            handshake = PadWebSocket.HANDSHAKE + response
        else:
            logging.debug("Not using challenge + response")
            handshake = PadWebSocket.HANDSHAKE
        handshake = handshake % {'origin': origin, 'port': self.server.port,
                                    'bind': self.server.socket.bind} + "\r\n"
        logging.debug("%s sending handshake: %s" % (self.CHNL, handshake))
        self.out_buffer = handshake
        return True

    def send(self, data):
        logging.info("Sent message: %s" % data)
        self.out_buffer = "\x00%s\xff" % data
