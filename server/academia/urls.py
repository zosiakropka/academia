#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package academia.urls
@author: Zosia Sobocinska
@date Nov 4, 2013
"""


from django.conf.urls import patterns, include, url
from django.contrib import admin
from academia import settings
admin.autodiscover()

urlpatterns = patterns('')

urlpatterns += patterns('',
    url((r'^'), include(
        patterns('',
            url(r'^admin/', include(admin.site.urls)),

            url(r'^api/', include('api.urls')),
            url(r'^', include('webapp.urls')),
        )
    ))
)

if settings.DEBUG:
    from django.contrib.staticfiles.urls import staticfiles_urlpatterns
    urlpatterns += staticfiles_urlpatterns()
