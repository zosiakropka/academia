#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package client.views.note
@author: Zosia Sobocinska
@date Nov 2, 2013
"""
from django.shortcuts import get_object_or_404
from backbone.models import Note, Activity, Subject
from django.utils.timezone import now
from utils.decorators import authenticate, abstractor
import logging
from utils.exceptions.response import HttpResponseUnauthorized
from django.http.response import HttpResponseNotFound


logger = logging.getLogger(__name__)


@authenticate(user=True)
@abstractor
def note_edit(user, note_id):

    note = Note.objects.get(pk=note_id)
    if not note:
        raise HttpResponseNotFound
    if note.access != "open" and note.owner != user:
        raise HttpResponseUnauthorized
    else:
        return ('note/pad.html', {"note_id": note.id, "content": note.content})


@authenticate(user=True)
@abstractor
def note_create(user, access_type, subject_abbr, activity_type):

    subject = Subject.objects.get(abbr=subject_abbr)
    activity = get_object_or_404(Activity, type=activity_type, subject=subject)
    date = now()
    note = Note(activity=activity, owner=user, date=date, access=access_type, title=str(date))
    note.save()
    return ('note/pad.html', {"note_id": note.id, "content": "Editable pad"})
