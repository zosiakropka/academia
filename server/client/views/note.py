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
from django.core.exceptions import PermissionDenied

from academia.settings import PADSERVERS
wsserver = PADSERVERS['wsserver']

logger = logging.getLogger(__name__)


@authenticate(user=True)
@abstractor
def note_edit(user, note_id):

    note = get_object_or_404(Note, pk=note_id).for_edit(by_user=user)
    if not note:
        raise PermissionDenied()
    else:
        return ('client/note/pad.html', {"note_id": note.id, "content": note.content, "pad_port": wsserver["port"]})


@authenticate(user=True)
@abstractor
def note_create(user, access_type, subject_abbr, activity_type):

    subject = Subject.objects.get(abbr=subject_abbr)
    activity = get_object_or_404(Activity, type=activity_type, subject=subject)
    date = now()
    note = Note(activity=activity, owner=user, date=date, access=access_type)
    note.save()
    return ('client/note/pad.html', {"note_id": note.id, "content": "Editable pad", "pad_port": wsserver["port"]})


@authenticate(user=True)
@abstractor
def note_open(user, note_id):

    note = get_object_or_404(Note, pk=note_id).for_open(by_user=user)
    if not note:
        raise PermissionDenied()
    else:
        return ('client/note/open.html', {"note_id": note.id, "content": note.content})
