"""@package api.views.note
@author Zosia Sobocinska
@date Dec 14, 2013
"""
from api.utils.decorators import abstractor, authenticate
from backbone.models import Activity, Note
from django.shortcuts import get_object_or_404
from django.core.exceptions import PermissionDenied
from utils.serializer import jsonize


@authenticate(user=True, admin=True)
@abstractor
def note_create(user, admin=False, note_access=None, subject_abbr=None, activity_type=None):
    note_access = note_access.pop()
    subject_abbr = subject_abbr.pop()
    activity_type = activity_type.pop()
    activity = get_object_or_404(Activity, subject__abbr=subject_abbr, type=activity_type)
    note = Note(access=note_access, activity=activity, owner=user)
    note.save()
    note = Note.objects.filter()
    return jsonize([note])


@authenticate(user=True, admin=True)
@abstractor
def note_list(user, admin=False,
              subject_name=None, subject_abbr=None, activity_type=None, activity_id=None, note_access=None):

    notes = None

    if admin:
        notes = Note.objects.all()
    else:
        notes = Note.get_notes_for_open(by_user=user)

    if note_access:
        notes = notes.filter(access__in=note_access)

    if activity_id:
        notes = notes.filter(activity__pk__in=activity_id)

    if subject_name or subject_abbr:
        if subject_name:
            notes = notes.filter(activity__subject__name__in=subject_name)

        if subject_abbr:
            notes = notes.filter(activity__subject__abbr__in=subject_abbr)

        if activity_type:
            notes = notes.filter(activity__subject__activity__type__in=activity_type)

    excludes = ('content', )
    relations = {'owner': {'fields': {'username'}}, }
    return jsonize(notes, relations=relations, excludes=excludes)


@authenticate(user=True, admin=True)
@abstractor
def note_get(user, admin=False, note_id=None, edit="False"):
    note = get_object_or_404(Note, pk__in=note_id)
    if edit == "True":
        note = note.for_edit(by_user=user)
    else:
        note = note.for_open(by_user=user)
    if not note:
        raise PermissionDenied()
    return jsonize([note])
