#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.channel.base.server
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
from threading import Thread


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

    def new_connection(self, connection):
        self.connections[connection.conn_id] = connection
        self.connections[connection.conn_id].pad_server = self
        pass
