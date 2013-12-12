#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.server.ws.connection
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
from ws4py.websocket import WebSocket
from pad.server.base.connection import PadBaseConnection


class PadWSConnection(WebSocket, PadBaseConnection):

    def __init__(self, sock, protocols=None, extensions=None, environ=None, heartbeat_freq=None):
        WebSocket.__init__(self, sock, protocols=protocols, extensions=extensions, environ=environ,
                           heartbeat_freq=heartbeat_freq)
        PadBaseConnection.__init__(self)

    def received_message(self, message):
        self.handle_message(str(message))

    def closed(self, code, reason=None):
        PadBaseConnection.close(self)
