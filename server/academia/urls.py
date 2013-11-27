"""@package academia.urls
@author: Zosia Sobocinska
@date Nov 4, 2013
"""


from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
from django.views.generic.base import RedirectView
from academia import settings
admin.autodiscover()

urlpatterns = patterns('')
urlpatterns += patterns('',

    url(r'^', include('browser.urls')),
    url(r'^account/', include('account.urls')),
    url(r'^pad/', include('pad.urls')),
    url(r'^admin/', include(admin.site.urls)),

    (r'^favicon\.ico$', RedirectView.as_view(url=settings.MEDIA_URL + 'static/images/favicon.ico')),
)
