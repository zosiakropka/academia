"""@package api.views.note
@author Zosia Sobocinska
@date Dec 14, 2013
"""
from utils.decorators import authenticate, api
from backbone.models import Subject
from utils.serializer import query_to_dict, JsonEncoder
import json


@authenticate(user=True, admin=True)
@api
def note_create(user):
    pass


@authenticate(user=True, admin=True)
@api
def note_list(user, subject_abbr=None, activity_type=None):

    subjects = Subject.objects.all()
    subjects = query_to_dict(subjects)
    print subjects
    return JsonEncoder().encode(subjects)


@authenticate(user=True, admin=True)
@api
def note_get(user):
    pass
