#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package backbone.admin
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.contrib import admin
from backbone.models import Subject, Supervisor, Activity, Note

admin.site.register(Activity)
admin.site.register(Supervisor)
admin.site.register(Subject)
admin.site.register(Note)
