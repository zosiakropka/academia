from config import LOGS_DIR
from os import path
LOGS_PATH = path.join(LOGS_DIR, "%s.log")

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
