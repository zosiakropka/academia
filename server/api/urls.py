#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package api.urls
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.conf.urls import patterns, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('')
urlpatterns += patterns('api.views',
    url(r'^note/get/(?P<note_id>\d+)/$', 'note_open'),
    url(r'^note/create/(?P<access_type>[-\w]+)/activity/(?P<activity_id>\d+)/$', 'note_create'),
)
