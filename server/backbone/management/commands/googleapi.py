#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package backbone.management.commands.googleapi
@author: Zosia Sobocinska
@date Dec 15, 2013
"""
from django.core.management.base import BaseCommand


class Command(BaseCommand):
    args = ''
    help = 'Setup Google Callendar credentials'

    def handle(self, *args, **options):
        from oauth2client.file import Storage as oauth_Storage
        from oauth2client import client as oauth_client
        from oauth2client.tools import run as oauth2_run
        from academia.settings import GOOGLE_API
        import gflags
        GOOGLE_CALENDAR_API = GOOGLE_API["APIS"]["CALENDAR"]
        storage = oauth_Storage(GOOGLE_API["CREDENTIALS_DIR"] + GOOGLE_CALENDAR_API["CREDENTIALS_FILE"])
        credentials = storage.get()
        if credentials is None or credentials.invalid:

            # Disable the local server feature
            gflags.FLAGS.auth_local_webserver = False

            # Set up a Flow object to be used if we need to authenticate. This
            # sample uses OAuth 2.0, and we set up the OAuth2WebServerFlow with
            # the information it needs to authenticate. Note that it is called
            # the Web Server Flow, but it can also handle the flow for native
            # applications
            # The client_id and client_secret can be found in Google Developers Console
            flow = oauth_client.OAuth2WebServerFlow(
                client_id=GOOGLE_CALENDAR_API["CREDENTIALS"]["CLIENT_ID"],
                client_secret=GOOGLE_CALENDAR_API["CREDENTIALS"]["CLIENT_SECRET"],
                scope='https://www.googleapis.com/auth/calendar',
                user_agent=GOOGLE_API['USER_AGENT'])
            credentials = oauth2_run(flow, storage)
