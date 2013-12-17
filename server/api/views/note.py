"""@package api.views.note
@author Zosia Sobocinska
@date Dec 14, 2013
"""
from utils.decorators import authenticate, api
from backbone.models import Activity, Note
from django.shortcuts import get_object_or_404
from django.core.exceptions import PermissionDenied
from utils.serializer import jsonize


@authenticate(user=True, admin=True)
@api
def note_create(user, note_access, subject_abbr, activity_type):
    note_access = note_access.pop()
    subject_abbr = subject_abbr.pop()
    activity_type = activity_type.pop()
    activity = get_object_or_404(Activity, subject__abbr=subject_abbr, type=activity_type)
    note = Note(access=note_access, activity=activity, owner=user)
    note.save()
    note = Note.objects.filter()
    return jsonize(note)


@authenticate(user=True, admin=True)
@api
def note_list(user, admin=False, subject_name=None, subject_abbr=None, activity_type=None, note_access=None):
    """
    @todo Rewrite it entirely so that select starts from notes
    """

    notes = None

    if admin:
        notes = Note.objects.all()
    else:
        notes = Note.get_notes_for_open(by_user=user)

    if note_access:
        notes = notes.filter(access=note_access)

    if subject_name or subject_abbr:
        if subject_name:
            notes = notes.filter(subject__name=subject_name)

        if subject_abbr:
            notes = notes.filter(subject__abbr=subject_abbr)

        if activity_type:
            notes = notes.filter(subject__activity__type=activity_type)

    excludes = ('content', )
    relations = {'activity': {'relations': {'subject', 'supervisor'}}, }
    return jsonize(notes, excludes=excludes, relations=relations)


@authenticate(user=True, admin=True)
@api
def note_get(user, note_id, edit="False"):
    note_id = note_id.pop()
    note = get_object_or_404(Note, pk=note_id).for_edit(user)
    if not note:
        raise PermissionDenied()
    return jsonize(note)
