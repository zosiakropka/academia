"""@package api.views.subject
@author Zosia Sobocinska
@date Dec 17, 2013
"""
from backbone.models import Subject
from utils.serializer import jsonize
from api.utils.decorators import authenticate, abstractor
from backbone.models.google_calendar import GoogleCalendar


@authenticate(user=True, admin=True)
@abstractor
def subject_list(user, admin=False, subject_name=None, subject_abbr=None, activity_type=None):

    GoogleCalendar.service

    subjects = Subject.objects.all()

    if subject_name:
        subjects = subjects.filter(name__in=subject_name)

    if subject_abbr:
        subjects = subjects.filter(abbr__in=subject_abbr)

    if activity_type:
        subjects = subjects.filter(activities__type=activity_type)

    relations = {
        'activities': {
            'excludes': ('notes'),
            'relations': {
                'supervisor': {
                    'fields': ('firstname', 'lastname', 'pk')}}}}
    return jsonize(subjects, relations=relations)


@authenticate(user=True, admin=True)
@abstractor
def subject_get(user, admin=False, subject_id=None):

    subject = Subject.objects.filter(pk__in=subject_id)

    relations = {
        'activities': {
            'excludes': ('notes'),
            'relations': {
                'supervisor': {
                    'fields': ('firstname', 'lastname', 'pk')}}}}

    return jsonize(subject, relations=relations)
