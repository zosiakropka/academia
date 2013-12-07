"""@package pad.server.padserver
@author: Zosia Sobocinska
@date Nov 2, 2013
"""

from IN import AF_INET
from socket import SOCK_STREAM, SOL_SOCKET, SO_REUSEADDR
import asyncore
import logging
from libs.ws4py.server.geventserver import WSGIServer, GEventWebSocketPool
from libs.ws4py.server.wsgiutils import WebSocketWSGIApplication
from pad.server.padconnection import PadTCPConnection, PadWSConnection
from threading import Thread
from gevent import monkey

monkey.patch_all()
logger = logging.getLogger('main')


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
            #if conn_id != broadcaster_id:
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
            newSocket, address = accept_pair
            connection = self.PadConnection(newSocket)
            connection.conn_id = newSocket.fileno()
            self.new_connection(connection)

    def run(self):
        self.bind((self.hostname, self.port))
        self.listen(100)
        asyncore.loop()


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

