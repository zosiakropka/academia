"""@package pad.endec
@author: Zosia Sobocinska
@date Nov 3, 2013
"""
from pad import codes

DELIMITER = u"\u001F"

def decode(record):
#    return dict((unit[0], (len(unit) == 1 or unit[1].decode('base64'))) for unit in [unit.split(DELIMITER) for unit in record.split(2 * DELIMITER)])
    data = {}
    units = {unit for unit in record.split(2 * DELIMITER)}
    for unit in units:
        pair = unit.split(DELIMITER)
        data[codes.label(int(pair[0]))] = len(pair) == 1 or pair[1].decode('base64')
    return data


def encode(data):
    return (2 * DELIMITER).join("%s%s" % (str(codes.code(key)), "" if data[key] == True else DELIMITER + data[key].encode('base64')) for key in data)
