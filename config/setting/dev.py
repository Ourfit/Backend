from .base import *

DATABASES = {
    "default": {
        "ENGINE": "django.db.backends.mysql",
        "NAME": "test",
        "USER": "root",
        "PASSWORD": "qwvvJqwr11kd!",
        "HOST": "127.0.0.1",
        "PORT": "3306",
    }
}

DEBUG = True

ALLOWED_HOSTS = ["*"]