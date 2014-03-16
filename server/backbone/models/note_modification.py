"""@package backbone.models.note_modification
@author: Zosia Sobocinska
@date Mar 16, 2014
"""
from django.db import models
from django.contrib.auth.models import User


class NoteModification(models.Model):

    modifier = models.ForeignKey(User, related_name="modifications")
    date = models.DateField(auto_now=True, editable=False)

    class Meta():
        app_label = 'backbone'
