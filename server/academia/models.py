from django.db import models


class User(models.Model):
    nick = models.SlugField(blank=True, unique=True)
    first_name = models.CharField
    last_name = models.CharField

    pass
