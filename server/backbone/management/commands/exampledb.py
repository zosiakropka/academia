#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package backbone.management.commands.exampledb
@author: Zosia Sobocinska
@date Dec 15, 2013
"""

from django.core.management.base import BaseCommand
import os
import subprocess
from backbone.models import Subject, Note, Activity, Supervisor
from random import choice
from account.models import User
from django.utils.text import slugify


class Command(BaseCommand):
    args = ''
    help = 'Fills db with example entries'

    def handle(self, *args, **options):

        subjects_count = 4
        activities_count = 3
        users_count = 3

        subjects = get_nonsense_title(subjects_count)

        for i in range(0, users_count):
            firstname, lastname = get_nonsense_person()
            login = slugify(lastname)
            try:
                user = User(username=login, password="pass")
                user.save()
            except:
                pass

        for subject_name in subjects:
            subject = Subject(name=subject_name)
            subject.save()
            for i in range(0, activities_count):
                try:
                    supervisor_firstname, supervisor_lastname = get_nonsense_person()
                    supervisor = Supervisor(firstname=supervisor_firstname, lastname=supervisor_lastname)
                    supervisor.save()
                    activity_type, dummy = choice(Activity.ACTIVITY_TYPES)
                    activity = Activity(type=activity_type, supervisor=supervisor, subject=subject)
                    activity.save()

                    users = User.objects.all()
                    for user in users:
                        for access, dummy in Note.NOTE_ACCESS:
                            note_title = get_nonsense_title()
                            note_content = get_nonsense_content(10)
                            note = Note(access=access, owner=user, activity=activity, title=note_title,
                                        content=note_content)
                            note.save()
                except:
                    pass


def get_nonsense(parameter):
    nonsense_path = "libs/nonsense-0.6/"
    command = "cd " + nonsense_path + " ; ./nonsense %s"
    return subprocess.check_output(command % parameter, shell=True)


def get_nonsense_person():
    return unicode(get_nonsense("Person")).replace('\n', '').split(' ')


def get_nonsense_title(count=None):
    result = get_nonsense("-f college.data -n %s" % (1 if (count == None) else count))
    return result if count == None else result.split('\n')[0:-1]


def get_nonsense_content(lines):
    return get_nonsense("-f stupidlaws.data -n %s" % lines)
