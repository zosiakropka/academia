"""@package pad.server.tcp.connection
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
import asyncore
from pad.server.base.connection import PadBaseConnection
import logging
from pad.server.endec import encode


class PadTCPConnection(asyncore.dispatcher_with_send, PadBaseConnection):

    #DELIMITER = u"\u001E"
    #DELIMITER = u"\u2424"
    DELIMITER = "\n"
    CHNL = "TCPConnection"

    def __init__(self, sock=None, map=None):
        PadBaseConnection.__init__(self)
        asyncore.dispatcher_with_send.__init__(self, sock=sock, map=map)
        sock.setblocking(0)
        self.buffer = ""
        self.send_data(encode({"purpose": "tak sobie", "message": "tresciwa"}))

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
