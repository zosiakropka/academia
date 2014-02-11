"""@package academia.config
@author Zosia Sobocinska
@date Dec 21, 2013
"""

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',  # Add 'postgresql_psycopg2', 'mysql', 'sqlite3' or 'oracle'.
        'NAME': 'academia',                      # Or path to database file if using sqlite3.
        'USER': 'academia',                      # Not used with sqlite3.
        'PASSWORD': 'change it!',     # Not used with sqlite3.
        'HOST': '',                      # Set to empty string for localhost. Not used with sqlite3.
        'PORT': '',                      # Set to empty string for default. Not used with sqlite3.
    }
}

PRODUCTION = False
DEBUG = True

ADMINS = (
    # ('Your Name', 'your_email@example.com'),
)

## Hosts/domain names that are valid for this site; required if DEBUG is False
#  See https://docs.djangoproject.com/en/1.4/ref/settings/#allowed-hosts
ALLOWED_HOSTS = ['localhost']

## Local time zone for this installation. Choices can be found here:
#  http://en.wikipedia.org/wiki/List_of_tz_zones_by_name
#  although not all choices may be available on all operating systems.
#  In a Windows environment this must be set to your system time zone.
TIME_ZONE = 'Europe/Warsaw'

## Language code for this installation. All choices can be found here:
#  http://www.i18nguy.com/unicode/language-identifiers.html
LANGUAGE_CODE = 'en-us'

## If you set this to False, Django will make some optimizations so as not
#  to load the internationalization machinery.
USE_I18N = True

## Absolute filesystem path to the directory that will hold user-uploaded files.
#  Example: "/home/media/media.lawrence.com/media/"
MEDIA_ROOT = ''

## Make this unique, and don't share it with anybody.
SECRET_KEY = 'change it!'


TEMPLATE_DIRS = (
    None
)
