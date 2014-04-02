"""@package webapp.views.application
@author: Zosia Sobocinska
@date Mar 30, 2014
"""
from django.shortcuts import render_to_response


def application(request):
    data = {
        "partials": {
             "menu/main-menu": "webapp/application/partials/menus/main-menu.phtml",
         }
    }
    return render_to_response("webapp/application/index.html", data)
