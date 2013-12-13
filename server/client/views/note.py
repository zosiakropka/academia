#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package client.views.note
@author: Zosia Sobocinska
@date Nov 2, 2013
"""
from django.shortcuts import get_object_or_404
from backbone.models import Note, Activity
from django.utils.timezone import now
from utils.decorators import authenticate, abstractor
import logging
from utils.exceptions.response import HttpResponseUnauthorized


logger = logging.getLogger(__name__)


@authenticate
@abstractor
def note_edit(user, note_id):

    note = get_object_or_404(Note, pk=note_id)
    if note.access == "open" or note.owner == user:
        return ('pad/pad.html', {"note_id": note.id, "content": note.content})
    else:
        raise HttpResponseUnauthorized


@authenticate
@abstractor
def note_create(user, access_type, activity_id):

    activity = get_object_or_404(Activity, pk=activity_id)
    note = Note(activity=activity, owner=user, date=now(), access=access_type, title="new")
    note.save()
    return ('pad/pad.html', {"note_id": note.id, "content": "Editable pad"})
