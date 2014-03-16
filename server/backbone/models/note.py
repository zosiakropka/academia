"""@package backbone.models.note
@author: Zosia Sobocinska
@date Mar 16, 2014
"""
from django.db import models
from django.utils.text import slugify
from utils import utimestamp
from django.contrib.auth.models import User
from backbone.models.activity import Activity


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
        app_label = 'backbone'

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

    @staticmethod
    def get_notes_for_open(by_user, **kwargs):
        notes = Note.objects.all()
        if len(kwargs) > 0:
            notes = notes.filter(**kwargs)
        return notes.filter(models.Q(owner=by_user) | models.Q(access="open") | models.Q(access="public"))

    @staticmethod
    def get_notes_for_edit(by_user, **kwargs):
        notes = Note.objects.all()
        if len(kwargs) > 0:
            notes = notes.filter(**kwargs)
        return notes.filter(models.Q(owner=by_user) | models.Q(access="open"))
