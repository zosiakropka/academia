"""@package webapp.templatetags.webapp
@author: Zosia Sobocinska
@date Mar 13, 2014
"""

from django import template
from django.core import urlresolvers
from academia.settings import URL_PREFIX
register = template.Library()


@register.simple_tag(name='prefixed_url')
def prefixed_url(url, *args, **kwargs):
    reverse = urlresolvers.reverse("webapp.views.%s" % url, args=args, kwargs=kwargs)
    if len(URL_PREFIX):
        URL_PREFIX[-1] = ''
        return "/%s%s" % (URL_PREFIX, reverse)
    else:
        return reverse
