# -*- coding: UTF-8 -*-
"""@package pad.channel.tcp.connection
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
from asyncore import dispatcher_with_send as dispatcher
from pad.channel.base.connection import PadBaseConnection
import logging


class PadTCPConnection(dispatcher, PadBaseConnection):

    DELIMITER = u"\u001E"
    CHNL = "TCPConnection"

    def __init__(self, *args, **kwargs):
        PadBaseConnection.__init__(self)
        sock = kwargs['sock'] if 'sock' in kwargs else args[0]
        dispatcher.__init__(self, *args, **kwargs)
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
        dispatcher.send(self, "%s%s%s" % (message, self.DELIMITER, '\n'))
