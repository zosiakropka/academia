"""@package api.utils.decorators
@author: Zosia Sobocinska
@date Mar 6, 2014
"""
from functools import wraps
from django.http.response import HttpResponse
from django.core.exceptions import PermissionDenied


def abstractor(fun):
    @wraps(fun)
    def _wrapper(user, request, **kwargs):
        parameters = request.POST if request.method == 'POST' else request.GET
        data = fun(user, **dict(parameters))
        return HttpResponse(data, mimetype='application/json')
    return _wrapper


def authenticate(user=False, admin=False):
    def _decorator(fun):
        allowed = {'user': user, 'admin': admin}

        @wraps(fun)
        def _wrapper(request, *args, **kwargs):
            user = request.user
            if user and user.is_authenticated():
                if (allowed['user']) or (allowed['admin'] and user.is_superuser):
                    return fun(user, request, *args, **kwargs)
            raise PermissionDenied()
        return _wrapper
    return _decorator
