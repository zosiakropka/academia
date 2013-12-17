"""
Serialize data to/from JSON
"""
import json
from python import Serializer as PythonSerializer
from django.core.serializers.json import Deserializer as JSONDeserializer, \
    DjangoJSONEncoder

class Serializer(PythonSerializer):
    """
    Convert a queryset to JSON.
    """
    def end_serialization(self):
        """Output a JSON encoded queryset."""
        txt = json.dumps(self.objects, self.stream, cls=DjangoJSONEncoder,
            **self.options)
        self.stream.write(txt)

    def getvalue(self):
        """
        Return the fully serialized queryset (or None if the output stream
        is not seekable).
        """

        if callable(getattr(self.stream, 'getvalue', None)):
            return self.stream.getvalue()

Deserializer = JSONDeserializer
