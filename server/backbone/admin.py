#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package backbone.admin
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.contrib import admin
from backbone.models import Subject, Activity, Note, Pad

admin.site.register(Activity)
admin.site.register(Subject)
admin.site.register(Note)
admin.site.register(Pad)
