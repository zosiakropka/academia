#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.management.commands.padtcpserver
@author: Zosia Sobocinska
@date Nov 27, 2013
"""

from django_initd.daemon_command import DaemonCommand


class Command(DaemonCommand):
    args = ''
    help = 'Runs TCP server to enable real time collaboration via TCP sockets'

    PID_FILE = "academia_padtcpserver.pid"
    LOG_FILE = "academia_padtcpserver.log"

    def loop_callback(self, *args, **options):
        from academia.settings import PADSERVERS

        from pad.channel.tcp.server import PadTCPServer
        server = PADSERVERS['tcpserver']

        tcpserver = PadTCPServer(server['host'], server['port'])
        tcpserver.run()
