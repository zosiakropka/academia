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
        view_kwargs = dict(parameters)
        if 'per_page' in view_kwargs:
            per_page = view_kwargs.pop('per_page')
            per_page = per_page[0]
            if 'page' in view_kwargs:
                page = view_kwargs.pop('page')
                page = page[0]
            else:
                page = 1
            view_kwargs['page'] = int(page)
            view_kwargs['per_page'] = int(per_page)
        data = fun(user, **view_kwargs)
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
