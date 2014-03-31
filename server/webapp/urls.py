#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package webapp.urls
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.conf.urls import patterns, url

from django.contrib import admin
from django.views.generic.base import RedirectView
from django.conf import settings
admin.autodiscover()

urlpatterns = patterns('')
urlpatterns += patterns('webapp.views',
    url(r'^$', 'application'),
    url(r'^subjects$', 'subject_list'),
    url(r'^account/signin/$', 'signin'),
    url(r'^account/signout/$', 'signout'),
    url(r'^subject/(?P<subject_abbr>\w+)/$', 'subject_browse'),
    url(r'^note/new/(?P<access_type>[-\w]+)/activity/(?P<subject_abbr>\w+)-(?P<activity_type>\w+)/$', 'note_create'),
    url(r'^note/(?P<note_id>\d+)/edit/$', 'note_edit'),
    url(r'^note/(?P<note_id>\d+)/$', 'note_open'),
    (r'^favicon\.ico$', RedirectView.as_view(url=settings.STATIC_URL + 'webapp/images/favicon.ico')),
)
