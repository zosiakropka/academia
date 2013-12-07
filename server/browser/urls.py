"""@package browser.urls
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.conf.urls import patterns, url

from django.contrib import admin
from django.views.generic.list import ListView
from browser.models import Subject
admin.autodiscover()

urlpatterns = patterns('')
urlpatterns += patterns('browser.views',
    url(r'^$', 'subject_list'),
    url(r'^subject/(?P<subject_id>\d+)/$', 'subject_detail'),
)
