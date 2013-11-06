"""@package pad.server.server
@author: Zosia Sobocinska
@date Nov 4, 2013
"""
from IN import AF_INET
from socket import SOCK_STREAM, SOL_SOCKET, SO_REUSEADDR
import asyncore
import logging


class PadSocketServer(asyncore.dispatcher):

    def __init__(self, hostname, port, cls):
        asyncore.dispatcher.__init__(self)
        self.cls = cls
        self.port = port
        self.hostname = hostname
        self.create_socket(AF_INET, SOCK_STREAM)
        self.socket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        self.bind((hostname, port))
        self.connections = {}
        self.listen(100)
        logging.info("%s listening on %s:%s" % (self.cls.CHNL, str(self.hostname), str(self.port)))
        self.running = True

    def handle_accept(self):
        accept_pair = self.accept()
        if accept_pair is not None:
            newSocket, address = accept_pair
            logging.info("%s client: %s" % (self.cls.CHNL, str(address)))
            fileno = newSocket.fileno()
            self.connections[fileno] = self.cls(newSocket)
            #self.connections[fileno].init()
            self.connections[fileno].set_server(self)

    def broadcast(self, data, pad, broadcaster):
        # Needs to be filtered for pad and none-broadcaster
        for conn in self.connections:
            conn.send(data)
        pass

    def process_data(self, data):
        logging.info("%s received: %s" % (self.cls.CHNL, data["message"]))
