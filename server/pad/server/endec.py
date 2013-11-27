"""@package pad.server.endec
@author: Zosia Sobocinska
@date Nov 3, 2013

\warning
Specific socket-based protocols may encapsulate/decapsulate encoded/decoded
messages in their own way. Encoding/decoding mechanisms that are listed here
concern the content of the message send via specific channel. It means that
message encoded by pad.endec.encode() may require further encapsulation
processing before sending and, analogically, before decoding received message
it may be required to decapsulate it first.

Data record string sent/received via pad socket mechanism consists of single-
character version number (version number encoded by chr(key_int) and decoded
by ord(key_char)) followed by sequence of logical <i>units</i> joined by
UNIT_DELIMTR characters sequence. <i>Unit</i> may be:
- either <b>key-value</b> pair,
- or <b>flag key</b>.

\par Key-value pair unit
If INNER_DELIMTR char sequence is present in unit content, then we handle
<b>key-value</b> pair. Key and value are separated by UNIT_DELIMTR. Key is
represented as described in "Keys" paragraph. Key character/string is
transmitted as-is, whereas value string is transmitted base64-encoded.

\par Flag key unit
If INNER_DELIMTR char sequence isn't present in unit content, then we handle
<b>flag key</b>. Flat is interpreted by server as boolean True.

\par Keys
Standard keys understood by server are predefined in pad.codes.CODES dict.
It's recommended for key part to be a single code in char representation
(key's code encoded by chr(key_int) and decoded by ord(key_char)). String
codes may also proceeded, though.

There are some keys required to be present in message so that it won't be
rejected in further processing. Those are indicated in pad.codes.CODES docs.

Key ought to be one of keys listed in pad.codes.CODES. Custom keys are also
proceeded, but may not be understood and handled by server.
"""
from pad.server import codes
import logging

## two instances of 'INFORMATION SEPARATOR ONE' (U+001F)
UNIT_DELIMTR = 2 * u"\u001F"

## 'INFORMATION SEPARATOR ONE' (U+001F)
INNER_DELIMTR = u"\u001F"

## The only (unstable) version supported for the moment of writing
VERSION = 1


def decode(record):
    """
    Decodes single record string.
    @param{record,string} string encoded according to guidelines listed in
    pad.endec docstring.
    @returns{dict(string,string|data|boolean)} key-value pairs of received data;
    - for <b>key-value</b> pair value is string,
    - for <b>flag key</b> value is boolean True.
    @return{None} if decode fails (eg. unsupported version provided)
    """
    try:
        version = ord(record[0])
    except TypeError:
        logging.error(TypeError.message)
        return None
    if version == VERSION:
        data = {}
        units = {unit for unit in record[1:-1].split(UNIT_DELIMTR)}
        for unit in units:
            pair = unit.split(INNER_DELIMTR)
            key = None
            try:
                key = ord(pair[0][0])
                key = codes.label(key)
            except Exception:  # handeled in the following if
                pass
            if not key:
                key = str(pair[0])
            try:
                #data[key] = (len(pair) == 1) or pair[1].decode('base64')
                data[key] = (len(pair) == 1) or pair[1]
            except UnicodeError:
                logging.error("Exception: %s" % UnicodeError)
            #data[key] = len(pair) == 1 or pair[1]
        return data
    else:
        return None


def encode(data):
    """
    Encodes dictionary to string record.
    @param{data,dict(string,string|data|boolean)} dictionary where:
    - for <b>key-value pair</b> value is string or data
    - for <b>flag key</b> value is boolean True
    @returns string encoded according to guidelines listed in pad.endec
    docstring.
    """
    #return chr(VERSION) + (UNIT_DELIMTR).join("%s%s%s" % (chr(codes.code(key)), "" if data[key] == True else INNER_DELIMTR, data[key].encode('base64') if data[key] != True  else "") for key in data)
    return chr(VERSION) + (UNIT_DELIMTR).join("%s%s%s" % (chr(codes.code(key)), "" if data[key] == True else INNER_DELIMTR, data[key] if data[key] != True  else "") for key in data)
