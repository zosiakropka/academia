"""@package backbone.models.supervisor
@author: Zosia Sobocinska
@date Mar 16, 2014
"""
from django.db import models
from django.utils.text import slugify
from utils import utimestamp


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

    class Meta():
        app_label = 'backbone'
