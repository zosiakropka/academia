#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package backbone.models
@author: Zosia Sobocinska
@date Nov 26, 2013
"""
from django.db import models
from django.contrib.auth.models import User
from django.db.models import Q


class Subject(models.Model):
    name = models.CharField(max_length=200)
    abbr = models.CharField(max_length=10)

    def __unicode__(self):
        return self.name


class Activity(models.Model):

    LECTURE = 'lect'
    LABORATORIUM = 'lab'
    PROJECT = 'proj'
    CONVERSATORY = 'conv'
    FOREIGN_LANGUAGE = 'lang'
    EXERCISE = 'exer'

    ACTIVITY_TYPES = [
        (LECTURE, "Lecture"),
        (LABORATORIUM, "Laboratorium"),
        (PROJECT, "Project"),
        (CONVERSATORY, "Conversatory"),
        (FOREIGN_LANGUAGE, "Foreign language"),
        (EXERCISE, "Exercise")
    ]

    type = models.CharField(max_length=200, choices=ACTIVITY_TYPES)
    subject = models.ForeignKey(Subject, related_name="activities")
    supervisor = models.CharField(max_length=200)

    def __unicode__(self):
        return '[' + self.subject.abbr + '] ' + dict(self.ACTIVITY_TYPES)[self.type] + ' (' + self.supervisor + ')'

    def get_accessible_notes(self, user):
        return self.notes.filter(Q(owner=user) | Q(access="open") | Q(access="public"))


class Note(models.Model):

    OPEN = 'open'
    PUBLIC = 'public'
    PRIVATE = 'private'

    NOTE_ACCESS = [
        (OPEN, "Open"),
        (PUBLIC, "Public"),
        (PRIVATE, "Private"),
    ]

    access = models.CharField(max_length=10, choices=NOTE_ACCESS)
    owner = models.ForeignKey(User, related_name="notes")
    activity = models.ForeignKey(Activity, related_name="notes")

    date = models.DateField()
    title = models.CharField(max_length=200)
    slug = models.SlugField(max_length=100)
    content = models.TextField()

    class Meta:
        ordering = ["date"]

    def __unicode__(self):
        return self.title or self.content[:40] + (self.content[40:] and "..")
