"""@package pad.views
@author: Zosia Sobocinska
@date Nov 2, 2013
"""
from django.shortcuts import get_object_or_404, render
from browser.models import Note, Activity
from django.http.response import Http404
from django.utils.timezone import now
from utils.decorators import authenticate


@authenticate
def pad_edit(user, request, note_id):

    note = get_object_or_404(Note, pk=note_id)
    if note.access == "open" or note.owner == user:
        return render(request, 'pad/pad.html', {"note_id": note.id, "content": note.content})
    else:
        raise Http404


@authenticate
def pad_create(user, request, access_type, activity_id):

    activity = get_object_or_404(Activity, pk=activity_id)
    note = Note(activity=activity, owner=user, date=now(), access=access_type, title="new")
    note.save()
    return render(request, 'pad/pad.html', {"note_id": note.id, "content": "Editable pad"})
