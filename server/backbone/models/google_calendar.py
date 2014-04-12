"""@package backbone.models.google_calendar
@author Zosia Sobocinska
@date Apr 1, 2014
"""
from academia.settings import GOOGLE_API
from django.db import models
from backbone.utils import classproperty
from apiclient.discovery import build as build_service
import httplib2
from oauth2client.file import Storage as oauth_Storage
from backbone.models.schedule import Schedule
# from oauth2client.appengine import OAuth2Decorator

GOOGLE_CALENDAR_API = GOOGLE_API["APIS"]["CALENDAR"]


class GoogleCalendar(models.Model):
    __service__ = None
    __decorator__ = None
    __http__ = None

    schedule = models.OneToOneField(Schedule, related_name="calendar")
    identifier = models.IntegerField(blank=True)

    def save(self, *args, **kwargs):
        identifier = self.__g_insert__()
        self.identifier = identifier
        return models.Model.save(self, *args, **kwargs)

    @classmethod
    def __initialize__(cls):
        if not cls.__service__:
            storage = oauth_Storage(GOOGLE_API["CREDENTIALS_DIR"] + GOOGLE_CALENDAR_API["CREDENTIALS_FILE"])
            credentials = storage.get()
            if credentials is None or credentials.invalid == True:
                raise MissingOrInvalidCredentialsException("Can't initialize Google Calendar API")

            cls.__http__ = httplib2.Http()
            cls.__http__ = credentials.authorize(cls.__http__)

            cls.__service__ = build_service(
                    serviceName='calendar', version='v3', http=cls.__http__)

#         if not cls.__decorator__:
#             cls.__decorator__ = OAuth2Decorator(
#                 client_id=GOOGLE_CALENDAR_API["CREDENTIALS"]["CLIENT_ID"],
#                 client_secret=GOOGLE_CALENDAR_API["CREDENTIALS"]["CLIENT_SECRET"],
#                 scope='https://www.googleapis.com/auth/calendar',
#                 user_agent=GOOGLE_API['USER_AGENT'])

    @classproperty
    def service(self):
        try:
            if not self.__service__:
                self.__initialize__()
            return self.__service__
        except:
            return None

#     @classproperty
#     def decorator(self):
#         if not self.__decorator__:
#             self.__initialize__()
#         return self.__decorator__

    def __g_insert__(self):
        id_prefix = GOOGLE_CALENDAR_API["ID_PREFIX"]
        print "%s_%d" % (id_prefix, self.schedule.pk)
        calendar = self.service.calendars().insert(body={
            "kind": "calendar#calendar",
            "description": self.schedule.name,
            "summary": self.schedule.name,
            "etag": "",
            "id": ("%s_%d" % (id_prefix, self.schedule.pk))
        }).execute()
        return calendar.id

    class Meta:
        app_label = 'backbone'


class MissingOrInvalidCredentialsException(Exception):
    def __init__(self, *args, **kwargs):
        Exception.__init__(self, *args, **kwargs)
