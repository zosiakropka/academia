"""@package utils.serializer
@author: Zosia Sobocinska
@date Dec 14, 2013
"""
from django.core.exceptions import ObjectDoesNotExist
from json.encoder import JSONEncoder


def model_to_dict(obj, exclude_types=['AutoField', 'ForeignKey', 'OneToOneField'], exclude_relations={}):
    """
    serialize model object to dict with related objects

    @author Vadym Zakovinko <vp@zakovinko.com>
    @editor Zosia Sobocinska
    @date Jan 31, 2011
    http://djangosnippets.org/snippets/2342/
    """
    tree = {}
    tree['pk'] = obj.pk
    for field_name in obj._meta.get_all_field_names():
        if (field_name not in exclude_relations or exclude_relations[field_name]):
            if field_name in exclude_relations:
                exclude_subrelations = exclude_relations[field_name]
            else:
                exclude_subrelations = {}
            try:
                field = getattr(obj, field_name)
            except (ObjectDoesNotExist, AttributeError):
                continue

            if field.__class__.__name__ in ['RelatedManager', 'ManyRelatedManager']:
                if field.model.__name__ in exclude_types:
                    continue

                if field.__class__.__name__ == 'ManyRelatedManager':
                    exclude_types.append(obj.__class__.__name__)
                subtree = []
                for related_obj in getattr(obj, field_name).all():
                    value = model_to_dict(related_obj, exclude_types=exclude_types, exclude_relations=exclude_subrelations)
                    if value:
                        subtree.append(value)
                if subtree:
                    tree[field_name] = subtree

                continue

            field = obj._meta.get_field_by_name(field_name)[0]
            if field.__class__.__name__ in exclude_types:
                continue

            if field.__class__.__name__ == 'RelatedObject':
                exclude_types.append(field.model.__name__)
                tree[field_name] = model_to_dict(getattr(obj, field_name), exclude_types=exclude_types, exclude_relations=exclude_subrelations)
                continue

            value = getattr(obj, field_name)
            if value:
                tree[field_name] = value

    return tree


def query_to_dict(query, exclude_types=None, exclude_relations={}):
    tree = []
    for entity in query:
        tree.append(model_to_dict(entity, exclude_relations=exclude_relations))
    return tree


class JsonEncoder(JSONEncoder):
    def default(self, obj):
        if set(['quantize', 'year']).intersection(dir(obj)):
            return str(obj)
        elif hasattr(obj, 'next'):
            return list(obj)
        return JSONEncoder.default(self, obj)
