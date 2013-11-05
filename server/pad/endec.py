"""@package pad.endec
@author: Zosia Sobocinska
@date Nov 3, 2013

\warning
Specific socket-based protocols may encapsulate/decapsulate encoded/decoded
messages in their own way. Encoding/decoding mechanisms that are listed here
concern the content of the message send via specific channel. It means that
message encoded by pad.endec.encode() may require further encapsulation
processing before sending and, analogically, before decoding received message
it may be required to decapsulate it first.

Data record string sent/received via pad socket mechanism consists of logical
<i>units</i> joined by UNIT_DELIMTR characters sequence. <i>Unit</i> may be:
- either <b>key-value</b> pair,
- or <b>flag key</b>.

\par Key-value pair
If INNER_DELIMTR char sequence is present in unit content, then we handle
<b>key-value</b> pair. Key and value are separated by UNIT_DELIMTR. Key part
should be handled as-is, whereas value part is transmitted base64-encoded.

\par Flag key
If INNER_DELIMTR char sequence isn't present in unit content, then we handle
<b>flag key</b>. Flat is interpreted by server as boolean True.

Standard keys understood by server are predefined in pad.codes.CODES dict.
Some of those keys are required to be present in message so that it won't be
rejected in further processing.
"""
from pad import codes

UNIT_DELIMTR = 2 * u"\u001F"
INNER_DELIMTR = u"\u001F"


def decode(record):
    """
    Decodes single record string.
    @param{record,string} string encoded according to guidelines listed in
    pad.endec docstring.
    @returns{dict(string,string|data|boolean)} key-value pairs of received data;
    - for <b>key-value</b> pair value is string,
    - for <b>flag key</b> value is boolean True.
    """
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
    """
    Encodes dictionary to string record.
    @param{data,dict(string,string|data|boolean)} dictionary where:
    - for <b>key-value pair</b> value is string or data
    - for <b>flag key</b> value is boolean True
    """
    return (UNIT_DELIMTR).join("%s%s%s" % (str(codes.code(key)), "" if data[key] == True else INNER_DELIMTR, data[key].encode('base64') if data[key] != True  else "") for key in data)
