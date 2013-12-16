#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package backbone.models
@author: Zosia Sobocinska
@date Nov 26, 2013
"""
from django.db import models
from django.contrib.auth.models import User
from django.db.models import Q
import re
from django.utils.text import slugify
from utils import utimestamp


class Subject(models.Model):
    name = models.CharField(max_length=200, unique=True)
    abbr = models.SlugField(max_length=10, blank=True, null=False, unique=True)

    def __unicode__(self):
        return self.name

    def save(self, force_insert=False, force_update=False, using=None, update_fields=None):
        if not self.abbr:
            self.abbr = (''.join(map(lambda x: '' if len(x) == 0 else x[0],
                                    re.split('\W', self.name, flags=re.UNICODE))))[0:10]

        return models.Model.save(self, force_insert=force_insert, force_update=force_update, using=using,
                                 update_fields=update_fields)


class Supervisor(models.Model):

    firstname = models.CharField(max_length=50)
    lastname = models.CharField(max_length=50)
    slug = models.SlugField(max_length=30, unique=True, blank=False)

    def save(self, force_insert=False, force_update=False, using=None,
        update_fields=None):
        if not self.slug:
            slug = slugify(self.firstname[0] + self.lastname)
            try:  # in case such a slug already exists
                Supervisor.objects.get(slug=slug)
                slug += utimestamp()
            except:
                pass
            self.slug = slug
        return models.Model.save(self, force_insert=force_insert, force_update=force_update, using=using,
                                 update_fields=update_fields)

    def __unicode__(self):
        return "%s %s" % (self.firstname, self.lastname)


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
        return '[' + self.subject.abbr + '] ' + dict(self.ACTIVITY_TYPES)[self.type] + ' (' + unicode(self.supervisor) + ')'

    def get_notes_for_open(self, by_user, **kwargs):
        notes = self.notes
        if len(kwargs) > 0:
            notes = notes.filter(kwargs)
        return notes.filter(Q(owner=by_user) | Q(access="open") | Q(access="public"))

    def get_notes_for_edit(self, by_user, **kwargs):
        notes = self.notes
        if len(kwargs) > 0:
            notes = notes.filter(kwargs)
        return notes.filter(Q(owner=by_user) | Q(access="open"))


class Note(models.Model):

    OPEN = 'open'
    PUBLIC = 'public'
    PRIVATE = 'private'

    NOTE_ACCESS = [
        (OPEN, u"Open"),
        (PUBLIC, u"Public"),
        (PRIVATE, u"Private"),
    ]

    access = models.CharField(max_length=10, choices=NOTE_ACCESS)
    owner = models.ForeignKey(User, related_name="notes")
    activity = models.ForeignKey(Activity, related_name="notes")

    date = models.DateField(auto_now_add=True, editable=False)
    title = models.CharField(max_length=200)
    slug = models.SlugField(max_length=100, unique=True)
    content = models.TextField()

    class Meta:
        ordering = ["date"]

    def __unicode__(self):
        return self.title or self.content[:40] + (self.content[40:] and "..")

    def save(self, force_insert=False, force_update=False, using=None, update_fields=None):
        if self.title in [u"no title", None] and len(self.content) > 5:
            self.title = self.content[0:200]
        if not self.title:
            self.title = u"no title"
        if not self.slug:
            self.slug = slugify(self.title + '-' + utimestamp())[0:100]
        return models.Model.save(self, force_insert=force_insert, force_update=force_update, using=using,
                                 update_fields=update_fields)

    def for_edit(self, by_user):
        return self if self.owner == by_user or self.access == self.OPEN else None

    def for_open(self, by_user):
        return self if self.owner == by_user or self.access in [self.PRIVATE, self.OPEN] else None


class NoteModification(models.Model):

    modifier = models.ForeignKey(User, related_name="modifications")
    date = models.DateField(auto_now=True, editable=False)
