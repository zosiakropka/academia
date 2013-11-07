"""@package pad.server.thread
@author: Zosia Sobocinska
@date Nov 4, 2013
"""
from threading import Thread
import asyncore
import argparse
import sys
from pad.server.padsocket import PadStandardSocket
from pad.server.padserver import PadSocketServer


class PadServerThread (Thread):
    def __init__(self, hostname="localhost", port=5000, cls=PadStandardSocket, group=None, target=None, name=None,
        args=(), kwargs=None, verbose=None):
        self.hostname = hostname
        self.port = port
        self.cls = cls
        self.server = None
        Thread.__init__(self, group=group, target=target, name=name, args=args, kwargs=kwargs, verbose=verbose)

    def run(self):
        self.server = PadSocketServer(self.hostname, self.port, self.cls)
        asyncore.loop()


def __main(argv):
    """
    Called if socket server is ran from commandline.
    """
    parser = argparse.ArgumentParser()
    parser.add_argument('-H', '--hostname', default='localhost')
    parser.add_argument('-P', '--port', required=True)
    args = parser.parse_args()
    hostname = args.hostname
    port = int(args.port)
    thread = PadServerThread(hostname, port)
    thread.run()

if __name__ == "__main__":
    __main(sys.argv[1:])
