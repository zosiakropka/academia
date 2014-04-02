#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package backbone.models
@author: Zosia Sobocinska
@date Nov 26, 2013
"""
from django.contrib.auth.models import User
from backbone.models.user_profile import UserProfile
from backbone.models.schedule import Schedule
from backbone.models.subject import Subject
from backbone.models.supervisor import Supervisor
from backbone.models.activity import Activity
from backbone.models.note import Note
from backbone.models.google_calendar import GoogleCalendar

from backbone.models import admin

__all__ = ['UserProfile', 'GoogleCalendar', 'Schedule', 'Subject', 'Supervisor', 'Activity', 'Note']
