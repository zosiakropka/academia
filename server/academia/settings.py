#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package academia.settings
@author: Zosia Sobocinska
@date Nov 4, 2013

Django settings for Academia project.
"""
import config

DEBUG = config.DEBUG

if not config.PRODUCTION:
    from os import path
    PROJECT_DIR = path.dirname(__file__) + "/.."

TEMPLATE_DEBUG = DEBUG

ADMINS = config.ADMINS

MANAGERS = config.MANAGERS

DATABASES = config.DATABASES

ALLOWED_HOSTS = config.ALLOWED_HOSTS

TIME_ZONE = config.TIME_ZONE

LANGUAGE_CODE = config.LANGUAGE_CODE

SITE_ID = config.SITE_ID

USE_I18N = config.USE_I18N

## If you set this to False, Django will not format dates, numbers and
#  calendars according to the current locale.
USE_L10N = config.USE_L10N

## If you set this to False, Django will not use timezone-aware datetimes.
USE_TZ = config.USE_TZ

## Absolute filesystem path to the directory that will hold user-uploaded files.
#  Example: "/home/media/media.lawrence.com/media/"
MEDIA_ROOT = config.MEDIA_ROOT

## URL that handles the media served from MEDIA_ROOT. Make sure to use a
#  trailing slash.
#  Examples: "http://media.lawrence.com/media/", "http://example.com/media/"
MEDIA_URL = config.MEDIA_URL

## Non-default serializer that enables including custom fields to / excluding
# specified fields from serialized JSON
SERIALIZATION_MODULES = {
    'json': 'wadofstuff.django.serializers.jsonizer'
}

## Absolute path to the directory static files should be collected to.
#  Don't put anything in this directory yourself; store your static files
#  in apps' "static/" subdirectories and in STATICFILES_DIRS.
#  Example: "/home/media/media.lawrence.com/static/"
STATIC_ROOT = ''

## URL prefix for static files.
#  Example: "http://media.lawrence.com/static/"
STATIC_URL = '/static/'

## Additional locations of static files
STATICFILES_DIRS = (
    # Put strings here, like "/home/html/static" or "C:/www/django/static".
    # Always use forward slashes, even on Windows.
    # Don't forget to use absolute paths, not relative paths.
    path.join(PROJECT_DIR, "static") if DEBUG else None,
)

## List of finder classes that know how to find static files in
#  various locations.
STATICFILES_FINDERS = (
    'django.contrib.staticfiles.finders.FileSystemFinder',
    'django.contrib.staticfiles.finders.AppDirectoriesFinder',
#    'django.contrib.staticfiles.finders.DefaultStorageFinder',
)

## Make this unique, and don't share it with anybody.
SECRET_KEY = config.SECRET_KEY

## List of callables that know how to import templates from various sources.
TEMPLATE_LOADERS = (
    'django.template.loaders.filesystem.Loader',
    'django.template.loaders.app_directories.Loader',
#     'django.template.loaders.eggs.Loader',
)

MIDDLEWARE_CLASSES = (
    'django.middleware.common.CommonMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    # Uncomment the next line for simple clickjacking protection:
    # 'django.middleware.clickjacking.XFrameOptionsMiddleware',
)

ROOT_URLCONF = 'academia.urls'

WSGI_APPLICATION = 'academia.wsgi.application'

TEMPLATE_DIRS = config.TEMPLATE_DIRS if config.PRODUCTION else (path.join(PROJECT_DIR, "templates"))

INSTALLED_APPS = (
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.sites',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'django.contrib.admin',

    'access_tokens',
    
    'account',
    'backbone',
    'client',
    'pad',
)

from academia import logs
LOGGING = logs.LOGGING

## Where to redirect unauthorized
LOGIN_URL = "/account/signin"

AUTH_PROFILE_MODULE = "account.UserProfile"
