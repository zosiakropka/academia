"""@package webapp.views.application
@author: Zosia Sobocinska
@date Mar 30, 2014
"""
from django.shortcuts import render_to_response


def application(request):
    return render_to_response("webapp/application/index.html")
