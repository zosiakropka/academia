#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.management.commands.padwsserver
@author: Zosia Sobocinska
@date Nov 27, 2013
"""

from django_initd.daemon_command import DaemonCommand


class Command(DaemonCommand):
    args = ''
    help = 'Runs WS server to enable real time collaboration via WS sockets'

    PID_FILE = "academia_padwsproxy.pid"
    LOG_FILE = "academia_padwsproxy.log"

    def loop_callback(self):
        from academia.settings import PADSERVERS

        from pad.channel.ws.proxy import PadWSProxy
        server = PADSERVERS['wsproxy']

        wsserver = PadWSProxy(server['host'], server['port'])
        wsserver.run()
