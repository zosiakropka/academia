from django.db import models
from browser.models import Note
from django.db.models.fields.related import ForeignKey


class Pad(models.Model):

    note = ForeignKey(Note)

    def __unicode__(self):
        return "Pad"
