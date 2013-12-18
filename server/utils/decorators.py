#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package utils.decorators
@author Zosia Sobocinska
@date Dec 11, 2013
"""
from django.shortcuts import render
from academia.settings import LOGIN_URL
from functools import wraps
from django.http.response import HttpResponse, HttpResponseRedirect
from django.utils import simplejson
from django.core.exceptions import PermissionDenied


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
                    raise PermissionDenied()
        return _wrapper
    return _decorator


def abstractor(fun):
    @wraps(fun)
    def _wrapper(user, request, **kwargs):
        parameters = request.POST if request.method == 'POST' else request.GET
        for key, val in parameters:
            kwargs[key] = val
        result = fun(user, **kwargs)
        try:
            template, data = result
        except:
            return result
        return render(request, template, data)
    return _wrapper


def api(fun):
    @wraps(fun)
    def _wrapper(user, request, **kwargs):
        parameters = request.POST if request.method == 'POST' else request.GET
        data = fun(user, **dict(parameters))
        return HttpResponse(data, mimetype='application/json')
    return _wrapper


def not_implemented(fun):
    @wraps(fun)
    def _wrapper(request, *args, **kwargs):
        fun(request, *args, **kwargs)
        raise Exception("Function %s.%s not implemented." % (fun.__module__, fun.__name__))
    return _wrapper
