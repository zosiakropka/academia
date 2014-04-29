#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.channel.tcp.server
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
import asyncore
from IN import AF_INET
from socket import SOCK_STREAM, SOL_SOCKET, SO_REUSEADDR
from pad.channel.tcp.connection import PadTCPConnection
import logging
from threading import Thread


class PadTCPServer(asyncore.dispatcher):

    CHNL = "TCP"

    def __init__(self, hostname, port):
        asyncore.dispatcher.__init__(self, None, None)
        self.connections = {}
        self.port = port
        self.hostname = hostname
        self.running = False
        self.create_socket(AF_INET, SOCK_STREAM)
        self.socket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        self.socket.settimeout(30)

    def run(self):
        self.bind((self.hostname, self.port))
        self.listen(100)
        try:
            asyncore.loop()
        except KeyboardInterrupt:
            logging.info("Ctrl+C pressend, shutting down %s" % self.CHNL)
        except Exception:
            logging.info("Shutting down: %s" % self.CHNL)

    def handle_accept(self):
        accept_pair = self.accept()
        if accept_pair is not None:
            newSocket = accept_pair[0]  # newSocket, address = accept_pair
            connection = PadTCPConnection(newSocket, pad_server=self)
            connection.conn_id = newSocket.fileno()
            self.connections[connection.conn_id] = connection
            self.connections[connection.conn_id].pad_server = self

    def pad_broadcast(self, record, pad_id, broadcaster_id):
        for conn_id, connection in self.connections.iteritems():
            if conn_id != broadcaster_id and connection.pad.note.pk == pad_id:
                connection.send_record(record)

    def start_server(self):
        logging.info("%s listening on %s:%s" % (self.CHNL, str(self.hostname), str(self.port)))
        self.running = True
        Thread.start(self)
