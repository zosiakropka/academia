"""@package account.models
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.db import models
from django.contrib.auth.models import User


class UserProfile(models.Model):
    user = models.ForeignKey(User, unique=True)
    email = models.EmailField("Email", unique=True)


User.profile = property(lambda u: UserProfile.objects.get_or_create(user=u)[0])
