#!/usr/bin/env python
"""@package backbone.models.admin
@author: Zosia Sobocinska
@date Mar 16, 2014
"""

try:
    from academia.settings import DEBUG_ADMIN, DEBUG
    if DEBUG and DEBUG_ADMIN:
        login = DEBUG_ADMIN['login']
        email = DEBUG_ADMIN['email']
        password = DEBUG_ADMIN['password']

        from django.contrib.auth import models as auth_models
        from django.contrib.auth.management import create_superuser
        from django.db.models import signals

        # Prevent interactive question about wanting a superuser created.
        # (http://stackoverflow.com/questions/1466827/)
        signals.post_syncdb.disconnect(
            create_superuser,
            sender=auth_models,
            dispatch_uid='django.contrib.auth.management.create_superuser')

        # Create our own test admin automatically.
        def create_testadmin(app, created_models, verbosity, **kwargs):
            try:
                auth_models.User.objects.get(username=login)
            except auth_models.User.DoesNotExist:
                print 'Creating test admin (login: %s, password: %s)' % (login, password)
                assert auth_models.User.objects.create_superuser(login, email, password)
            else:
                print 'Test admin already exists.'

        signals.post_syncdb.connect(create_testadmin,
            sender=auth_models, dispatch_uid='common.models.create_testadmin')

except:
    pass
