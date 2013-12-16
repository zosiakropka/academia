# -*- coding: UTF-8 -*-
"""@package pad.channel.tcp.connection
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
import asyncore
from pad.channel.base.connection import PadBaseConnection
import logging
from pad.channel.endec import encode


class PadTCPConnection(asyncore.dispatcher_with_send, PadBaseConnection):

    DELIMITER = u"\u001E"
    CHNL = "TCPConnection"

    def __init__(self, *args, **kwargs):
        PadBaseConnection.__init__(self)
        sock = kwargs['sock'] if 'sock' in kwargs else args[0]
        asyncore.dispatcher_with_send.__init__(self, *args, **kwargs)
        sock.setblocking(0)
        self.buffer = ""
        self.send_data(encode({"purpose": u"test", "message": u"ęółąśłżźćń"}))

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
        asyncore.dispatcher_with_send.send(self, "%s%s%s" % (message, self.DELIMITER, '\n'))
