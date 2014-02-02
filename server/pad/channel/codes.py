#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.channel.codes
@author: Zosia Sobocinska
@date Nov 3, 2013
"""

CODES = {
    "purpose": 1,
    "login": 2,
    "password": 3,
    "token": 4,
    "message": 5
}


def label(code):
    try:
        for label, c in CODES.items():
            if c == code:
                return label
    except KeyError:
        pass
    return None


def code(label):
    try:
        return CODES[label]
    except KeyError:
        pass
    return 0
