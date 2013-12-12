#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.server.codes
@author: Zosia Sobocinska
@date Nov 3, 2013
"""

CODES = {
    "purpose": 1,
    "pad": 2,
    "login": 3,
    "password": 4,
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
