#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.management.commands.padwsserver
@author: Zosia Sobocinska
@date Nov 27, 2013
"""

from django.core.management.base import BaseCommand


class Command(BaseCommand):
    args = ''
    help = 'Runs WS server to enable real time collaboration via WS sockets'

    def handle(self, *args, **options):
        from pad.channel.ws.proxy import PadWSProxy
        from academia.settings import PADSERVERS
        server = PADSERVERS['wsproxy']

        wsserver = PadWSProxy(server['host'], server['port'])
        wsserver.run()
