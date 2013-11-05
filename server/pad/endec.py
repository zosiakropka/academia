"""@package pad.endec
@author: Zosia Sobocinska
@date Nov 3, 2013
"""
from pad import codes

UNIT_DELIMTR = 2 * u"\u001F"
INNER_DELIMTR = u"\u001F"


def decode(record):
    data = {}
    units = {unit for unit in record.split(UNIT_DELIMTR)}
    for unit in units:
        pair = unit.split(INNER_DELIMTR)
        key = None
        try:
            key = int(pair[0])
            key = codes.label(key)
        except Exception:  # handeled in the following if
            pass
        if not key:
            key = str(pair())

        data[key] = len(pair) == 1 or pair[1].decode('base64')
    return data


def encode(data):
    return (UNIT_DELIMTR).join("%s%s%s" % (str(codes.code(key)), "" if data[key] == True else INNER_DELIMTR, data[key].encode('base64') if data[key] != True  else "") for key in data)
