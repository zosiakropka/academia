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
    url(r'^note/list/$', 'note_list'),
    url(r'^note/get/$', 'note_get'),
    url(r'^note/create/$', 'note_create'),

    url(r'^subject/list/$', 'subject_list'),
    url(r'^subject/get/$', 'subject_list'),
    url(r'^subject/list/$', 'subject_list'),

    url(r'^activity/list/$', 'activity_list'),
)
