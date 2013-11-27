"""@package pad
@author: Zosia Sobocinska
@date Nov 2, 2013
"""

from pad.server.padserver import PadWSServer, PadTCPServer
from pad.server.padthread import PadServerThread
from threading import Thread

tcpserver = PadTCPServer("0.0.0.0", 5001)  # @todo: shouldn't be hardcoded
tcpserver.start_server()

wsserver = PadWSServer("0.0.0.0", 5002)  # @todo: shouldn't be hardcoded
wsserver.start_server()
