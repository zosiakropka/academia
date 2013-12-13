#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package api.views
@author: Zosia Sobocinska
@date Dec 13, 2013
"""
from utils.decorators import abstractor, authenticate


@authenticate
@abstractor
def get_notes():
    return 