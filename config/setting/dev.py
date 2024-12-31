from .base import *

# Database
# https://docs.djangoproject.com/en/5.1/ref/settings/#databases
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

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

ALLOWED_HOSTS = ["*"]