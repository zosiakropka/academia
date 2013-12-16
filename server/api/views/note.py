"""@package api.views.note
@author Zosia Sobocinska
@date Dec 14, 2013
"""
from utils.decorators import authenticate, api
from backbone.models import Subject, Activity, Note
from utils.serializer import query_to_list, JsonEncoder, model_to_dict
from django.shortcuts import get_object_or_404


@authenticate(user=True, admin=True)
@api
def note_create(user, note_access, subject_name=None, subject_abbr=None, activity_type=None):
    activity = get_object_or_404(Activity, subject_name=None, subject_abbr=None, activity_type=None)
    note = Note(access=note_access, activity=activity, owner=user)
    note.save()
    note = model_to_dict(note)
    return JsonEncoder().encode(note)


@authenticate(user=True, admin=True)
@api
def note_list(user, subject_name=None, subject_abbr=None, activity_type=None):
    """
    @todo Rewrite it entirely so that select starts from notes
    """

    subjects = Subject.objects.all()
    if subject_name:
        subject_name = subject_name.pop()
        subjects = subjects.filter(name=subject_name)
    if subject_abbr:
        subject_abbr = subject_abbr.pop()
        subjects = subjects.filter(abbr=subject_abbr)

    if subjects.count() == 1 and activity_type:
        activity_type = activity_type.pop()
        subject = subjects.get()
        activity = subject.activities.get(type=activity_type)
        notes = activity.get_notes_for_open(user=user)
        notes = query_to_list(notes, exclude_relations={'content': None})
        activity = model_to_dict(activity, exclude_relations={'notes': None})
        activity['notes'] = notes
        subject = model_to_dict(subject, exclude_relations={'activities': None})
        subject['activities'] = [activity]
        subjects = [subject]
    else:
        subjects_list = []
        for subject in subjects:
            activities = subject.activities.all()
            activities_list = []
            for activity in activities:
                notes = activity.get_notes_for_open(user=user)
                notes = query_to_list(notes, exclude_relations={'content': None})
                activity = model_to_dict(activity, exclude_relations={'notes': None})
                activity['notes'] = notes
                activities_list.append(activity)
            subject = model_to_dict(subject, exclude_relations={'activities': None})
            subject['activities'] = activities_list
            subjects_list.append(subject)
        subjects = subjects_list

    return JsonEncoder().encode(subjects)


@authenticate(user=True, admin=True)
@api
def note_get(user):
    pass
