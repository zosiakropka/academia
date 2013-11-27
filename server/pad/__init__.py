"""@package pad
@author: Zosia Sobocinska
@date Nov 2, 2013
"""

from pad.server.padserver import PadWSServer, PadTCPServer

tcpserver = PadTCPServer("0.0.0.0", 5001)  # @todo: shouldn't be hardcoded
tcpserver.start_server()

wsserver = PadWSServer("0.0.0.0", 5002)  # @todo: shouldn't be hardcoded
wsserver.start_server()
