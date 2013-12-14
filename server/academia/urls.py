#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package academia.urls
@author: Zosia Sobocinska
@date Nov 4, 2013
"""


from django.conf.urls import patterns, include, url
from django.contrib import admin
from django.views.generic.base import RedirectView
from academia import settings
admin.autodiscover()

urlpatterns = patterns('')
urlpatterns += patterns('',

    url(r'^account/', include('account.urls')),
    url(r'^admin/', include(admin.site.urls)),

    url(r'^api/', include('api.urls')),

    url(r'^', include('client.urls')),
    (r'^favicon\.ico$', RedirectView.as_view(url=settings.STATIC_URL + 'images/favicon.ico')),
)

if settings.DEBUG:
    from django.contrib.staticfiles.urls import staticfiles_urlpatterns
    urlpatterns += staticfiles_urlpatterns()
