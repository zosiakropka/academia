"""@package pad.channel.tcp.proxy
@author: Zosia Sobocinska
@date Feb 2, 2014
"""
from asyncore import dispatcher_with_send as dispatcher
from pad.channel.tcp.connection import PadTCPConnection
from academia import settings
from IN import AF_INET
from socket import SOCK_STREAM

class PadTCPProxy(PadTCPConnection):
    """
    @todo Since it's almost the same PadTCPConnection, it ought to be somehow
    reimplemented to avoid code redundancy. 
    """

    DELIMITER = PadTCPConnection.DELIMITER
    
    def __init__(self, connection):
        tcpserver = settings.PADSERVERS['tcpserver']
        dispatcher.__init__(self)
        self.create_socket(AF_INET, SOCK_STREAM)
        self.connect( (tcpserver['host'], tcpserver['port']) )
        self.connection = connection

    def handle_read(self):
        data = None
        try:
            data = self.recv(self.CHUNK_SIZE)
        except Exception:
            pass
        if (data):
            self.buffer += data
            if self.DELIMITER in self.buffer:
                records = self.buffer.split(self.DELIMITER)
                self.buffer = records.pop()
                for record in records:
                    self.connection.handle_message(record)

    def handle_close(self):
        """
        @todo Close corresponding connection
        """
        PadTCPConnection.handle_close(self)

    def send_data(self, message):
        dispatcher.send(self, "%s%s%s" % (message, self.DELIMITER, '\n'))
