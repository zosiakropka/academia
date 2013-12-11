"""@package pad.server.padconnection
@author: Zosia Sobocinska
@date Nov 2, 2013
"""

import asyncore
from pad.server.endec import encode, decode
import logging
from ws4py.websocket import WebSocket


class PadBaseConnection():
    CHUNK_SIZE = 1024
    CHNL = ""
    buffer = ""
    pad_server = None
    pad = None
    conn_id = None

    def __init__(self):
        logging.info("%s client" % (self.CHNL))

    def set_server(self, server):
        self.pad_server = server

    def handle_close(self):
        logging.debug("%s : %s disconnected" % (self.CHNL, str(self.socket.getpeername())))

    def handle_message(self, message):
        data = decode(message)
        if data and "purpose" in data:
            logging.debug("%s has data onboard." % (self.CHNL))
            if data["purpose"] == "patches":
                self.pad_broadcast(data)

    def close(self):
        pass

    def pad_broadcast(self, data):
        self.pad_server.pad_broadcast(encode(data), self.pad, self.conn_id)


class PadTCPConnection(asyncore.dispatcher_with_send, PadBaseConnection):

    #DELIMITER = u"\u001E"
    DELIMITER = "\n"
    CHNL = "TCPConnection"

    def __init__(self, sock=None, map=None):
        PadBaseConnection.__init__(self)
        asyncore.dispatcher_with_send.__init__(self, sock=sock, map=map)
        sock.setblocking(0)
        self.buffer = ""

    def handle_read(self):
        data = None
        try:
            data = self.recv(self.CHUNK_SIZE)
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

    def send_data(self, message):
        asyncore.dispatcher_with_send.send(self, message + self.DELIMITER)


class PadWSConnection(WebSocket, PadBaseConnection):

    def __init__(self, sock, protocols=None, extensions=None, environ=None, heartbeat_freq=None):
        WebSocket.__init__(self, sock, protocols=protocols, extensions=extensions, environ=environ, heartbeat_freq=heartbeat_freq)
        PadBaseConnection.__init__(self)

    def received_message(self, message):
        self.handle_message(str(message))

    def closed(self, code, reason=None):
        PadBaseConnection.close(self)
