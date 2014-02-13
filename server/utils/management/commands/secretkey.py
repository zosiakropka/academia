#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.management.commands.padtcpserver
@author: Zosia Sobocinska
@date Nov 27, 2013
"""

from django.core.management.base import BaseCommand
from optparse import make_option


class Command(BaseCommand):
    args = ''
    option_list = BaseCommand.option_list + (
      make_option('--length',
        dest='length',
        type='int',
        metavar='INTEGER',
        default=54,
        help='Secret key\'s length.'),
      )
    help = 'Generates random secret key that may be placed into SECRET_KEY setting in config.py file.'

    def handle(self, *args, **options):
        import M2Crypto
        import string

        length = options['length']

        chars = string.ascii_uppercase + string.digits + string.ascii_lowercase + string.punctuation
        key = ''
        for i in range(length):
            key += chars[ord(M2Crypto.m2.rand_bytes(1)) % len(chars)]

        print key
