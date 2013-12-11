"""@package pad.management.commands.padtcpserver
@author: Zosia Sobocinska
@date Nov 27, 2013
"""

from django.core.management.base import BaseCommand


class Command(BaseCommand):
    args = '<poll_id poll_id ...>'
    help = 'Closes the specified poll for voting'

    def handle(self, *args, **options):
        from pad.server.tcp.server import PadTCPServer

        tcpserver = PadTCPServer("0.0.0.0", 5001)  # @todo: shouldn't be hardcoded
        tcpserver.run()
