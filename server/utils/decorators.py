#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package utils.decorators
@author Zosia Sobocinska
@date Dec 11, 2013
"""
from django.shortcuts import render
from academia.settings import LOGIN_URL
from functools import wraps
import logging
from django.http.response import HttpResponse, HttpResponseRedirect
from django.utils import simplejson
from utils.exceptions.response import HttpResponseUnauthorized


def authenticate(user=False, admin=False):
    def _decorator(fun):
        allowed = {'user': user, 'admin': admin}

        @wraps(fun)
        def _wrapper(request, *args, **kwargs):
            user = request.user
            if not (user and user.is_authenticated()):
                return HttpResponseRedirect('%s/?next=%s' % (LOGIN_URL, request.path))
            else:
                if (allowed['user']) or (allowed['admin'] and user.is_superuser):
                    return fun(user, request, *args, **kwargs)
                else:
                    raise HttpResponseUnauthorized()
        return _wrapper
    return _decorator


def abstractor(fun):
    @wraps(fun)
    def _wrapper(user, request, **kwargs):
        parameters = request.POST if request.method == 'POST' else request.GET
        for key, val in parameters:
            kwargs[key] = val
        template, data = fun(user, **kwargs)
        return render(request, template, data)
    return _wrapper


def api(fun):
    @wraps(fun)
    def _wrapper(user, request, **kwargs):
        if request.method == 'POST':
            parameters = request.POST
            for key, val in parameters:
                kwargs[key] = val
        data = fun(user, **kwargs)
        return HttpResponse(simplejson.dumps(data), mimetype='application/json')
    return _wrapper


def not_implemented(fun):
    @wraps(fun)
    def _wrapper(request, *args, **kwargs):
        fun()
        raise Exception("Function %s.%s not implemented." % (fun.__module__, fun.__name__))
    return _wrapper
