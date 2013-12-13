#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package backbone.views.hierarchy
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from backbone.models import Subject
from django.shortcuts import get_object_or_404, render
#from itertools import chain
from django.db.models import Q
from utils.decorators import authenticate
import logging

logger = logging.getLogger(__name__)


@authenticate
def subject_list(user, request):

    subjects = Subject.objects.all()

    return render(request, 'client/subject_list.html',
                  {'subject_list': subjects})


@authenticate
def subject_detail(user, request, subject_id):

    subject = get_object_or_404(Subject, pk=subject_id)

    activities = []
    for activity in subject.activities.all():
        activities.append({
            "activity": activity,
            "notes": activity.notes.filter(Q(owner=request.user) | Q(access="open"))
        })

    return render(request, 'client/subject.html',
                  {'subject': subject, 'activities': activities, 'editable': ['open', 'private']})
