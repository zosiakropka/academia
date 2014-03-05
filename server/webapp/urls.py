#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package webapp.urls
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.conf.urls import patterns, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('')
urlpatterns += patterns('webapp.views',
    url(r'^$', 'subject_list'),
    url(r'^subject/(?P<subject_abbr>\w+)/$', 'subject_browse'),
    url(r'^note/new/(?P<access_type>[-\w]+)/activity/(?P<subject_abbr>\w+)-(?P<activity_type>\w+)/$', 'note_create'),
    url(r'^note/(?P<note_id>\d+)/edit/$', 'note_edit'),
    url(r'^note/(?P<note_id>\d+)/$', 'note_open'),
)
