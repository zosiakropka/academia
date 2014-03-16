"""@package utils.serializer
@author: Zosia Sobocinska
@date Dec 14, 2013
"""
from django.core import serializers
from json.encoder import JSONEncoder
from wadofstuff.django.serializers import jsonizer


def jsonize(queryset, *args, **kwargs):
    serializer = jsonizer.Serializer()
    data = serializer.serialize(queryset, *args, **kwargs)
    return JsonEncoder().encode(data)


def pythonize(queryset, *args, **kwargs):
    return serializers.serialize("python", queryset)


class JsonEncoder(JSONEncoder):
    def default(self, obj):
        if set(['quantize', 'year']).intersection(dir(obj)):
            return str(obj)
        if hasattr(obj, 'next'):
            return list(obj)
        return JSONEncoder.default(self, obj)
