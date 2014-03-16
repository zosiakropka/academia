"""@package backbone.models.subject
@author: Zosia Sobocinska
@date Mar 16, 2014
"""
from django.db import models
import re


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

    class Meta():
        app_label = 'backbone'
