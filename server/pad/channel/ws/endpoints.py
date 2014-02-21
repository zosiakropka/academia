#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.channel.ws.connection
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
from ws4py.websocket import WebSocket
import threading
import logging


class PadWSProxyWSEndpoint(WebSocket):

    def __init__(self, sock, protocols=None, extensions=None, environ=None, heartbeat_freq=None):

        WebSocket.__init__(self, sock, protocols=protocols, extensions=extensions, environ=environ,
                           heartbeat_freq=heartbeat_freq)

        self.tcp_endpoint = PadWSProxyTCPEndpoint(self)
        self.tcp_endpoint.thread.start()

    def received_message(self, record):
        self.tcp_endpoint.forward(record)

    def forward(self, record, binary=False):
        self.send(record, binary)

    def closed(self, code, reason=None):
        self.tcp_endpoint.close()

##############################################################################

from asyncore import dispatcher_with_send
from academia import settings
from socket import SOCK_STREAM, AF_INET
from pad.channel.tcp import DELIMITER
import asyncore
from threading import Thread
from gevent import monkey
monkey.patch_all()


class PadWSProxyTCPEndpoint(dispatcher_with_send, Thread):
    def __init__(self, ws_endpoint):
        tcpserver = settings.PADSERVERS['tcpserver']
        dispatcher_with_send.__init__(self)
        Thread.__init__(self)
        self.create_socket(AF_INET, SOCK_STREAM)
        self.connect((tcpserver['host'], tcpserver['port']))
        self.ws_endpoint = ws_endpoint
        self.buffer = ''
        self.thread = threading.Thread(target=self.run)

    def handle_read(self):
        rawdata = None
        try:
            rawdata = self.recv(self.CHUNK_SIZE)
        except:
            pass
        if (rawdata):
            self.buffer += rawdata
            if DELIMITER in self.buffer:
                records = self.buffer.split(DELIMITER)
                self.buffer = records.pop()
                for record in records:
                    self.ws_endpoint.forward(record)

    def handle_close(self):
        """
        @todo Close corresponding connection
        """
        self.ws_endpoint.close(self)

    def forward(self, record):
        """
        Should be called by PadWSConnection when it receives a complete record.
        """
        dispatcher_with_send.send(self, "%s%s" % (record, DELIMITER))

    def run(self):

        self.tcp_endpoint = PadWSProxyTCPEndpoint(self)
        try:
            asyncore.loop()
        except Exception, e:
            logging.exception(e.message)
