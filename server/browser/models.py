#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package browser.models
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.db import models
from django.contrib.auth.models import User


class Subject(models.Model):
    name = models.CharField(max_length=200)
    abbr = models.CharField(max_length=10)

    def __unicode__(self):
        return self.name


class Activity(models.Model):

    ACTIVITY_TYPES = [
        ('lect', "Lecture"),
        ('lab', "Laboratorium"),
        ('proj', "Project"),
        ('conv', "Conversatory"),
        ('lang', "Foreign language"),
        ('exer', "Exercise")
    ]
    type = models.CharField(max_length=200, choices=ACTIVITY_TYPES)
    subject = models.ForeignKey(Subject, related_name="activities")
    supervisor = models.CharField(max_length=200)

    def __unicode__(self):
        return '[' + self.subject.abbr + '] ' + dict(self.ACTIVITY_TYPES)[self.type] + ' (' + self.supervisor + ')'


class Note(models.Model):

    NOTE_ACCESS = [
        ('open', "Open"),
        ('public', "Public"),
        ('private', "Private"),
    ]
    access = models.CharField(max_length=10, choices=NOTE_ACCESS)
    owner = models.ForeignKey(User, related_name="notes")
    activity = models.ForeignKey(Activity, related_name="%(class)ss")

    date = models.DateField()
    title = models.CharField(max_length=200)
    slug = models.SlugField(max_length=100)
    content = models.TextField()

    class Meta:
        #abstract = True
        ordering = ["date"]

    def __unicode__(self):
        return self.title or self.content[:40] + (self.content[40:] and "..")
