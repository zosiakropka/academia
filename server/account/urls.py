#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package account.urls
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.conf.urls import patterns, url

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('')
urlpatterns += patterns('account.views',
    url(r'^signin/$', 'signin'),
    url(r'^signout/$', 'signout'),
)
