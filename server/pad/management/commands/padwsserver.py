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
        from pad.channel.ws.server import PadWSServer

        wsserver = PadWSServer("0.0.0.0", 5002)  # @todo: shouldn't be hardcoded
        wsserver.run()
