"""@package pad.management.commands.padwsserver
@author: Zosia Sobocinska
@date Nov 27, 2013
"""

from django.core.management.base import BaseCommand


class Command(BaseCommand):
    args = '<poll_id poll_id ...>'
    help = 'Closes the specified poll for voting'

    def handle(self, *args, **options):
        from pad.server.ws.server import PadWSServer

        wsserver = PadWSServer("0.0.0.0", 5002)  # @todo: shouldn't be hardcoded
        wsserver.run()
