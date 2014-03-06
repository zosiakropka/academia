#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package utils.decorators
@author Zosia Sobocinska
@date Dec 11, 2013
"""
from functools import wraps


def not_implemented(fun):
    @wraps(fun)
    def _wrapper(request, *args, **kwargs):
        fun(request, *args, **kwargs)
        raise Exception("Function %s.%s not implemented." % (fun.__module__, fun.__name__))
    return _wrapper
