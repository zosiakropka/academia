"""@package pad.server.base.connection
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
import logging
from pad.server.endec import encode, decode


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
