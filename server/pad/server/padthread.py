"""@package pad.server.thread
@author: Zosia Sobocinska
@date Nov 3, 2013
"""
from threading import Thread


class PadServerThread (Thread):
    def __init__(self, server, group=None, target=None, name=None,
        args=(), kwargs=None, verbose=None):
        self.PadServer = server
        Thread.__init__(self, group=group, target=target, name=name, args=args, kwargs=kwargs, verbose=verbose)

    def run(self):
        self.PadServer.start_server()
