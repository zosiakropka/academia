"""@package webapp.templatetags.webapp
@author: Zosia Sobocinska
@date Mar 13, 2014
"""

from django import template
from django.core import urlresolvers
from academia.settings import URL_PREFIX, COPYRIGHT
from django.template.defaultfilters import stringfilter
from django.utils.html import conditional_escape
from django.utils.safestring import mark_safe
register = template.Library()

from datetime import date


@register.simple_tag(name='prefixed_url')
def prefixed_url(url, *args, **kwargs):
    reverse = urlresolvers.reverse("webapp.views.%s" % url, args=args, kwargs=kwargs)
    if len(URL_PREFIX):
        URL_PREFIX[-1] = ''
        return "/%s%s" % (URL_PREFIX, reverse)
    else:
        return reverse


@register.simple_tag(name='copyright_notice')
def copyright_notice(*args, **kwargs):
    try:
        try:
            author = "<a href='%(ADDRESS)s'>%(AUTHOR)s</a>" % COPYRIGHT
        except:
            author = COPYRIGHT["AUTHOR"]
    except:
        author = ""

    time = date.today().year
    try:
        time = "%d-%d" % (time, COPYRIGHT["SINCE"])
    except:
        pass

    try:
        return COPYRIGHT["SHORT_NOTICE"] % {"AUTHOR": author, "TIME": time}
    except:
        return "&#169; %(AUTHOR)s %(TIME)s" % {"AUTHOR": author, "TIME": time}


@stringfilter
def spacify(value, autoescape=None):
    if autoescape:
        esc = conditional_escape
    else:
        esc = lambda x: x
    return mark_safe(esc(value).replace('\n ', '\n&nbsp;').replace(' \n', '&nbsp;\n').replace('  ',  ' &nbsp;'))
spacify.needs_autoescape = True
register.filter(spacify)
