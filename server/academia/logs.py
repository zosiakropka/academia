from config import DEBUG
from os import path

## A sample logging configuration. The only tangible logging
#  performed by this configuration is to send an email to
#  the site admins on every HTTP 500 error when DEBUG=False.
#  See http://docs.djangoproject.com/en/dev/topics/logging for
#  more details on how to customize your logging configuration.
LOGS_PATH = path.join("/tmp/academia/logs/%s.log") if DEBUG else None

LOGS_FORMAT_JSON = """{
  "time": "%(asctime)s",
  "lvl": "%(levelname)s",
  "module": "%(module)s",
  "line": "%(lineno)s",
  "thread": "%(thread)d",
  "msg": "%(message)s"
}"""
LOGS_FORMAT_STANDARD = """[%(asctime)s] [%(levelname)s:%(module)s:%(lineno)s] %(message)s"""

LOGS_DATEFORMAT = "%d/%b/%Y %H:%M:%S"

LOGGING = {
    'version': 1,
    'disable_existing_loggers': True,
    'formatters': {
        'standard': {
            #'()': 'djangocolors_formatter.DjangoColorsFormatter',
            'format': LOGS_FORMAT_STANDARD,
            'datefmt': LOGS_DATEFORMAT
        },
        'json': {
            #'()': 'djangocolors_formatter.DjangoColorsFormatter',
            'format': LOGS_FORMAT_JSON,
            'datefmt': LOGS_DATEFORMAT
        },
    },
    'handlers': {
        'null': {
            'level': 'DEBUG',
            'class': 'django.utils.log.NullHandler',
        },
        'logfile': {
            'level': 'DEBUG',
            'class': 'logging.handlers.RotatingFileHandler',
            'filename': LOGS_PATH % "log",
            'maxBytes': 50000,
            'backupCount': 2,
            'formatter': 'json',
        },
        'console': {
            'level': 'INFO',
            'class': 'logging.StreamHandler',
            'formatter': 'standard'
        },
    },
    'loggers': {
        'django': {
            'handlers': ['console', 'logfile'],
            'propagate': True,
            'level': 'DEBUG',
        },
        'django.db.backends': {
            'handlers': ['console'],
            'level': 'DEBUG',
            'propagate': False,
        },
        '': {
            'handlers': ['console', 'logfile'],
            'level': 'DEBUG',
        },
    }
}

