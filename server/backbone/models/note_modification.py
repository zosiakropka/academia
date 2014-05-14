"""@package backbone.models.note_modification
@author: Zosia Sobocinska
@date Mar 16, 2014
"""
from django.db import models
from django.contrib.auth.models import User
from backbone.models.note import Note


class NoteModification(models.Model):

    modifier = models.ForeignKey(User, related_name="modifications")
    date = models.DateTimeField(auto_now=True, editable=False)
    note = models.ForeignKey(Note, related_name="modifications")

    def __unicode__(self):
        title_excerpt = self.note.title[:30] + (self.note.title[30:] and "...")
        return "%s (%s@%s)" % (title_excerpt, self.modifier, self.date)

    class Meta:
        app_label = 'backbone'

        ordering = ["-date"]
