#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.server.ws.server
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
from ws4py.server.geventserver import WSGIServer, GEventWebSocketPool

from gevent import monkey
from pad.server.base.server import PadBaseServer
from pad.server.ws.connection import PadWSConnection
from ws4py.server.wsgiutils import WebSocketWSGIApplication
monkey.patch_all()


class PadWSServer(WSGIServer, PadBaseServer):

    CHNL = "WS"
    PadConnection = PadWSConnection

    def __init__(self, hostname, port):
        PadBaseServer.__init__(self, hostname, port)
        pad_server = self

        class PadWsApplication(WebSocketWSGIApplication):
            def make_websocket(self, sock, protocols, extensions, environ):
                connection = WebSocketWSGIApplication.make_websocket(self, sock, protocols, extensions, environ)
                connection.conn_id = sock.fileno()
                pad_server.new_connection(connection)
                return connection
            pass
        WSGIServer.__init__(self, (hostname, port), PadWsApplication(handler_cls=self.PadConnection))
        self.pool = GEventWebSocketPool()

    def run(self):
        self.serve_forever()
