"""@package backbone.models.activity
@author: Zosia Sobocinska
@date Mar 16, 2014
"""
from django.db import models
from backbone.models.subject import Subject
from backbone.models.supervisor import Supervisor


class Activity(models.Model):

    LECTURE = 'lect'
    LABORATORIUM = 'lab'
    PROJECT = 'proj'
    CONVERSATORY = 'conv'
    FOREIGN_LANGUAGE = 'lang'
    EXERCISE = 'exer'

    ACTIVITY_TYPES = [
        (LECTURE, u"Lecture"),
        (LABORATORIUM, u"Laboratorium"),
        (PROJECT, u"Project"),
        (CONVERSATORY, u"Conversatory"),
        (FOREIGN_LANGUAGE, u"Foreign language"),
        (EXERCISE, u"Exercise")
    ]

    type = models.CharField(max_length=200, choices=ACTIVITY_TYPES)
    subject = models.ForeignKey(Subject, related_name="activities")
    supervisor = models.ForeignKey(Supervisor, related_name="acitivities")

    def __unicode__(self):
        return '[' + self.subject.abbr + '] ' + dict(self.ACTIVITY_TYPES)[self.type] + \
            ' (' + unicode(self.supervisor) + ')'

    class Meta():
        app_label = 'backbone'
