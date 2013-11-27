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
    # Examples:
    url(r'^$', "channels", name="channels"),
    url(r'^channels/', include('channels.urls')),
    url(r'^admin/', include(admin.site.urls)),
    (r'^favicon\.ico$', RedirectView.as_view(url=settings.MEDIA_URL + 'static/images/favicon.ico')),

    # \url(r'^$', 'academia.views.home', name='home'),
    # url(r'^academia/', include('academia.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
    url(r'^account/', include('account.urls')),
)
