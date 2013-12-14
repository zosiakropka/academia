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
from django.http.response import HttpResponseRedirect


def authenticate(fun):
    @wraps(fun)
    def _wrapper(request, *args, **kwargs):
        user = request.user
        if not (user and user.is_authenticated()):
            return HttpResponseRedirect('%s/?next=%s' % (LOGIN_URL, request.path))
        else:
            return fun(user, request, *args, **kwargs)
    return _wrapper


def abstractor(fun):
    @wraps(fun)
    def _wrapper(user, request, **kwargs):
        parameters = request.POST if request.method == 'POST' else request.GET
        for key, val in parameters:
            kwargs[key] = val
        template, data = fun(user, **kwargs)
        return render(request, template, data)
    return _wrapper


def not_implemented(fun):
    @wraps(fun)
    def _wrapper(request, *args, **kwargs):
        fun()
        raise Exception("Function %s.%s not implemented." % (fun.__module__, fun.__name__))
    return _wrapper
