"""@package utils.decorators
@author Zosia Sobocinska
@date Dec 11, 2013
"""
from django.shortcuts import redirect
from academia.settings import LOGIN_URL
from functools import wraps
import logging


def authenticate(fun):
    @wraps(fun)
    def _wrapper(request, *args, **kwargs):
        user = request.user
        if not (user and user.is_authenticated()):
            return redirect('%s/?next=%s' % (LOGIN_URL, request.path))
        else:
            return fun(user, request, *args, **kwargs)
    return _wrapper


def not_implemented(fun):
    @wraps(fun)
    def _wrapper(request, *args, **kwargs):
        fun()
        raise Exception("Function %s.%s not implemented." % (fun.__module__, fun.__name__))
    return _wrapper


def logged(fun):
    @wraps(fun)
    def _wrapper(*args, **kwargs):
        logging.debug("""{
            
        }""" % (fun.__module__, fun.__name__))
        pass
    return _wrapper
