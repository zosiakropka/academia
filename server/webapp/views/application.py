"""@package webapp.views.application
@author: Zosia Sobocinska
@date Mar 30, 2014
"""
from django.shortcuts import render_to_response


def application(request):

    def set_partial(partials, key):
        partials[key] = "webapp/application/partials/%s.phtml" % key

    partials = {}
    set_partial(partials, "menus/main")
    set_partial(partials, "miniatures/schedule")
    set_partial(partials, "miniatures/subjects")
    set_partial(partials, "miniatures/recent-notes")

    data = {
        "partials": partials
    }

    return render_to_response("webapp/application/index.html", data)
