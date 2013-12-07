"""@package browser.views
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from browser.models import Subject
from django.shortcuts import get_object_or_404, render
#from itertools import chain
from account.decorators import authenticate
from django.db.models import Q


@authenticate
def subject_detail(user, request, subject_id):

    subject = get_object_or_404(Subject, pk=subject_id)

    activities = []
    for activity in subject.activities.all():
        activities.append({
            "activity": activity,
            "notes": activity.notes.filter(Q(owner=request.user) | Q(access="open"))
        })

    return render(request, 'browser/subject.html',
                  {'subject': subject, 'activities': activities, 'editable': ['open', 'private']})


@authenticate
def subject_list(user, request):

    subjects = Subject.objects.all()

    return render(request, 'browser/subject_list.html',
                  {'subject_list': subjects})
