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
        from academia.settings import PADSERVERS
        server = PADSERVERS['tcpserver']

        tcpserver = PadTCPServer(server['host'], server['port'])
        tcpserver.run()
