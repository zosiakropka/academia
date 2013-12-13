#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package utils.exceptions.response
@author: Zosia Sobocinska
@date Dec 13, 2013
"""
from django.http.response import HttpResponse


class HttpResponseUnauthorized(HttpResponse):
    status_code = 401
