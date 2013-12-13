#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package client.urls
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.conf.urls import patterns, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('')
urlpatterns += patterns('client.views',
    url(r'^$', 'subject_list'),
    url(r'^subject/(?P<subject_id>\d+)/$', 'subject_browse'),
    url(r'^pad/note/new/(?P<access_type>[-\w]+)/activity/(?P<activity_id>\d+)/$', 'note_create'),
    url(r'^pad/note/(?P<note_id>\d+)/$', 'note_edit'),
    url(r'^note/(?P<note_id>\d+)/$', 'note_open'),
)
