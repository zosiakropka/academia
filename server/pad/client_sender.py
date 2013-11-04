"""@package pod.client
@author: Zosia Sobocinska
@date Nov 2, 2013
"""
import argparse
import logging
import socket
import sys
import endec


def start_client(hostname, port):
    DELIMITER = u"\u001E"
    DATA_CHUNK_SIZE = 1024

    logger = logging.getLogger('client')
    # Connect to the server
    logger.debug('creating socket')
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    logger.debug('connecting to server')
    s.connect((hostname, port))
    while True:
        message = raw_input("Type ('x'-exit): ")
        if message.lower() == 'x':
            break
        if message.__len__() > DATA_CHUNK_SIZE:
            print "Message too long (" + message.__len__() + ", only " + DATA_CHUNK_SIZE + " chars allowed)"
        else:
            data = {
                "purpose": "message",
                "message": message
            }
            msg = endec.encode(data)
            s.send(msg + DELIMITER)
    s.close()


def main(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('-H', '--hostname', default='localhost')
    parser.add_argument('-P', '--port', required=True)
    args = parser.parse_args()

    hostname = args.hostname
    port = int(args.port)

    start_client(hostname, port)

if __name__ == "__main__":
    main(sys.argv[1:])
