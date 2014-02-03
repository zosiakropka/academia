#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.channel.thread
@author: Zosia Sobocinska
@date Nov 3, 2013
"""
from threading import Thread


class PadServerThread (Thread):
    def __init__(self, server, *args, **kwargs):
        self.PadServer = server
        Thread.__init__(self, *args, **kwargs)

    def run(self):
        self.PadServer.start_server()
