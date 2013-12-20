"""@package api.views.auth
@author Zosia Sobocinska
@date Dec 20, 2013
"""
from django.core.context_processors import csrf
from django.shortcuts import render_to_response
import logging
from django.http.response import HttpResponse
from django.contrib.auth import authenticate, login
from django.core.exceptions import PermissionDenied


def auth_csrf(request):
    """
    @todo find out why this requires template and I just can't send 201
    """
    c = {}
    ctkn = csrf(request)
    logging.info(unicode(ctkn['csrf_token']))
    c.update(csrf(request))
    return render_to_response("api/csrftoken.html", c)


def signin(request):

    failure = True

    user = request.user
    if user.is_authenticated():
        failure = False
    else:
        username = request.POST.get('username')
        password = request.POST.get('password')
        if username and password:
            user = authenticate(username=username, password=password)
            if user and user.is_active:
                login(request, user)
                failure = False
    if failure:
        raise PermissionDenied()
    else:
        return HttpResponse(201)
