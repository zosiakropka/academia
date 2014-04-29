"""@package webapp.utils.decorators
@author: Zosia Sobocinska
@date Mar 6, 2014
"""
from functools import wraps
from django.shortcuts import render
from academia.settings import LOGIN_URL
from django.http.response import HttpResponseRedirect
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
            data['login'] = user.username
        except:
            return result
        return render(request, template, data)
    return _wrapper
