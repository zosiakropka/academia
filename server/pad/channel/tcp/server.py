#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.channel.tcp.server
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
import asyncore
from IN import AF_INET
from socket import SOCK_STREAM, SOL_SOCKET, SO_REUSEADDR
from pad.channel.base.server import PadBaseServer
from pad.channel.tcp.connection import PadTCPConnection
import logging


class PadTCPServer(asyncore.dispatcher, PadBaseServer):

    CHNL = "TCP"
    PadConnection = PadTCPConnection

    def __init__(self, hostname, port):
        PadBaseServer.__init__(self, hostname, port)
        asyncore.dispatcher.__init__(self)
        self.create_socket(AF_INET, SOCK_STREAM)
        self.socket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        self.socket.settimeout(30)
        self.connections = {}

    def handle_accept(self):
        accept_pair = self.accept()
        if accept_pair is not None:
            newSocket = accept_pair[0]  # newSocket, address = accept_pair
            connection = self.PadConnection(newSocket)
            connection.conn_id = newSocket.fileno()
            self.new_connection(connection)

    def run(self):
        self.bind((self.hostname, self.port))
        self.listen(100)
        try:
            asyncore.loop()
        except KeyboardInterrupt:
            logging.info("Ctrl+C pressend, shutting down %s"%self.CHNL)
        except Exception:
            logging.info("Shutting down: %s"%self.CHNL)
