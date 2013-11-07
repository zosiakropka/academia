"""@package pad.codes
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
    for label, c in CODES.items():
        if c == code:
            return label
    return None


def code(label):
    return CODES[label]
