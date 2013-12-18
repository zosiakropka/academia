"""@package api.views.subject
@author Zosia Sobocinska
@date Dec 17, 2013
"""
from backbone.models import Subject
from utils.serializer import jsonize
from utils.decorators import authenticate, api


@authenticate(user=True, admin=True)
@api
def subject_list(user, admin=False, subject_id=None, subject_name=None, subject_abbr=None, activity_type=None):
    subjects = Subject.objects.all()

    if subject_name:
        subjects = subjects.filter(name__in=subject_name)

    if subject_abbr:
        subjects = subjects.filter(abbr__in=subject_abbr)

    if activity_type:
        subjects = subjects.filter(activities__type=activity_type)

    relations = {'activities': {'relations': {'supervisor': {},
                                              'notes': {'fields': ('pk', 'owner')}}}}
    return jsonize(subjects, relations=relations)
