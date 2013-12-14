#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package client.views.subject
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from backbone.models import Subject
from django.shortcuts import get_object_or_404, render
#from itertools import chain
from django.db.models import Q
from utils.decorators import authenticate, abstractor
import logging

logger = logging.getLogger(__name__)


@authenticate(user=True)
@abstractor
def subject_list(user):

    subjects = Subject.objects.all()

    return ('client/subject_list.html',
                  {'subject_list': subjects})


@authenticate(user=True)
@abstractor
def subject_browse(user, subject_id):

    subject = get_object_or_404(Subject, pk=subject_id)

    activities = []
    for activity in subject.activities.all():
        activities.append({
            "activity": activity,
            "notes": activity.get_accessible_notes()
        })

    return ('client/subject_browse.html',
                  {'subject': subject, 'activities': activities, 'editable': ['open', 'private']})
