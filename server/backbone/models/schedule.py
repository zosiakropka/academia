"""@package backbone.models.schedule
@author Zosia Sobocinska
@date Apr 2, 2014
"""
from django.db import models


class Schedule(models.Model):

    name = models.CharField(max_length=40)
    description = models.TextField()

    class Meta:
        app_label = 'backbone'
