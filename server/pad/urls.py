#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.urls
@author: Zosia Sobocinska
@date Nov 2, 2013
"""

from django.conf.urls import patterns, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('')
urlpatterns += patterns('pad.views',
    url(r'^note/new/(?P<access_type>[-\w]+)/activity/(?P<activity_id>\d+)/$', 'pad_create'),
    url(r'^note/(?P<note_id>\d+)/$', 'pad_edit'),
)
