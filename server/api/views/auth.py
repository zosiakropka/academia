"""@package api.views.auth
@author Zosia Sobocinska
@date Dec 20, 2013
"""
from django.http.response import HttpResponse
from django.contrib.auth import authenticate, login
from django.core.exceptions import PermissionDenied
from django.core.context_processors import csrf


def auth_csrf(request):
    ctkn = unicode(csrf(request)['csrf_token'])
    response = HttpResponse(status=204)
    response.set_cookie("csrftoken", ctkn)
    return response


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
        return HttpResponse(status=204)
