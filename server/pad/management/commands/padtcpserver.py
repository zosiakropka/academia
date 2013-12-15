#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.management.commands.padtcpserver
@author: Zosia Sobocinska
@date Nov 27, 2013
"""

from django.core.management.base import BaseCommand


class Command(BaseCommand):
    args = ''
    help = 'Runs TCP server to enable real time collaboration via TCP sockets'

    def handle(self, *args, **options):
        from pad.channel.tcp.server import PadTCPServer

        tcpserver = PadTCPServer("0.0.0.0", 5001)  # @todo: shouldn't be hardcoded
        tcpserver.run()
