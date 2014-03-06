#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package webapp.views.subject
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from backbone.models import Subject, Note
from django.shortcuts import get_object_or_404
from webapp.utils.decorators import authenticate, abstractor
import logging

logger = logging.getLogger(__name__)


@authenticate(user=True)
@abstractor
def subject_list(user):

    subjects = Subject.objects.all()

    return ('webapp/subject/list.html',
                  {'subject_list': subjects})


@authenticate(user=True)
@abstractor
def subject_browse(user, subject_abbr):

    subject = get_object_or_404(Subject, abbr=subject_abbr)

    activities = []
    for activity in subject.activities.all():
        activities.append({
            "activity": activity,
            "notes": Note.get_notes_for_open(by_user=user, activity=activity)
        })

    return ('webapp/subject/browse.html',
                  {'subject': subject, 'activities': activities, 'user': user})
