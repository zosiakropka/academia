"""@package backbone.models.user_profile
@author: Zosia Sobocinska
@date Mar 16, 2014
"""
from django.db import models
from django.contrib.auth.models import User


class UserProfile(models.Model):
    user = models.ForeignKey(User, unique=True)
    email = models.EmailField("Email", unique=True)

    class Meta():
        app_label = 'backbone'


User.profile = property(lambda u: UserProfile.objects.get_or_create(user=u)[0])
