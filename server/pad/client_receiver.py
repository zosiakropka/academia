"""@package pod.client
@author: Zosia Sobocinska
@date Nov 2, 2013
"""
import argparse
import logging
import socket
import sys


def main(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('-H', '--hostname', default='localhost')
    parser.add_argument('-P', '--port', required=True)
    args = parser.parse_args()

    hostname = args.hostname
    port = int(args.port)

    DATA_CHUNK_SIZE = 1024
    logger = logging.getLogger('client')
    # Connect to the server
    logger.debug('creating socket')
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    logger.debug('connecting to server')
    s.connect((hostname, port))
    while True:
        response = s.recv(DATA_CHUNK_SIZE)
        print(response)
    s.close()

if __name__ == "__main__":
    main(sys.argv[1:])
