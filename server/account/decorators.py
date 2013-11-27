"""@package account.decorators
@author: Zosia Sobocinska
@date Nov 26, 2013
"""
from django.shortcuts import redirect
from academia.settings import LOGIN_URL


class authenticate:
    f = None
    user = None

    def __init__(self, f):
        self.f = f

    def __call__(self, request, *args, **kwargs):
        user = request.user
        if not (user and user.is_authenticated()):
            return redirect('%s/?next=%s' % (LOGIN_URL, request.path))
        else:
            return self.f(user, request, *args, **kwargs)
