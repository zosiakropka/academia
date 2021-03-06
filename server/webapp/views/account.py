#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package webapp.views.account
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.shortcuts import render
from django.contrib.auth import authenticate, login, logout
from django.http.response import HttpResponseRedirect


def signin(request):

    nxt = request.GET['next'] or "/"
    failure = False

    user = request.user
    if user.is_authenticated():
        return HttpResponseRedirect(nxt)

    username = request.POST.get('username')
    password = request.POST.get('password')
    if username and password:
        user = authenticate(username=username, password=password)
        if user and user.is_active:
            login(request, user)
            return HttpResponseRedirect(nxt)
        else:
            failure = True

    return render(request, 'webapp/account/signin.html', {"nxt": nxt, "failure": failure})


def signout(request):

    logout(request)
    return HttpResponseRedirect("/")
