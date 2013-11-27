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
    url(r'^$',
        ListView.as_view(
            queryset=Subject.objects.all(),
            context_object_name='subject_list',
            template_name='browser/subject_list.html')),
    url(r'^subject/(?P<subject_id>\d+)/$', 'subject_detail'),
)
