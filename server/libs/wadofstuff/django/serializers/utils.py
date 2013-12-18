"""@package wadofstuff.django.serializers.utils
@date Dec 18, 2013
"""

from collections import Iterable


def process_item_or_list(fun, inpt, **args):
    if isinstance(inpt, Iterable):
        return [fun(item, **args) for item in inpt]
    else:
        return fun(inpt, **args)
