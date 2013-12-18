"""@package api.views.activity
@author: Zosia Sobocinska
@date Dec 17, 2013
"""
from utils.decorators import authenticate, api
from backbone.models import Activity, Subject
from utils.serializer import jsonize
from json.encoder import JSONEncoder


@authenticate(user=True, admin=True)
@api
def activity_list(user, activity_id=None, subject_id=None, subject_abbr=None):
    activities = Activity.objects.all()
    if subject_id:
        activities = activities.filter(subject__pk__in=subject_id)
    if subject_abbr:
        activities = activities.filter(subject__abbr__in=subject_abbr)
    relations = {'supervisor': {'fields': ('firstname', 'lastname')}, }
    return jsonize(activities, relations=relations)


@authenticate(user=True, admin=True)
@api
def activity_list_by_subject(user, activity_id=None, subject_id=None, subject_abbr=None):
    subjects = Subject.objects.all()
    if subject_id:
        subjects = subjects.filter(pk__in=subject_id)
    if subject_abbr:
        subjects = subjects.filter(abbr__in=subject_abbr)
    relations = {'activities': {'relations': {'supervisor': {'fields': ('firstname', 'lastname')}, }}}
    return jsonize(subjects, relations=relations)
