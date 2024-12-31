"""
WSGI config for drf project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/5.1/howto/deployment/wsgi/
"""

import os

from django.core.wsgi import get_wsgi_application

# TODO : env 값 사용하기
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'config.setting.dev')

application = get_wsgi_application()
