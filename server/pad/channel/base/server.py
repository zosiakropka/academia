#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.channel.base.server
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
from threading import Thread
import logging


class PadBaseServer(Thread):
    PadConnection = None
    port = None
    hostname = None
    CHNL = None
    connections = {}
    running = False

    def __init__(self, hostname, port):
        Thread.__init__(self)
        self.port = port
        self.hostname = hostname

    def pad_broadcast(self, rawdata, pad, broadcaster_id):
        for conn_id in self.connections:
            if conn_id != broadcaster_id and self.connections[conn_id].pad == pad:
                self.connections[conn_id].send(rawdata)

    def start_server(self):
        logging.info("%s listening on %s:%s" % (self.CHNL, str(self.hostname), str(self.port)))
        self.running = True
        Thread.start(self)

    def new_connection(self, connection):
        self.connections[connection.conn_id] = connection
        self.connections[connection.conn_id].set_server(self)
        pass
