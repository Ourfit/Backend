from rest_framework import routers
from django.urls import path, include

from app.service.member.views import TestMemberViewSet

router = routers.DefaultRouter(trailing_slash = False)
router.register("members", TestMemberViewSet)

urlpatterns = [
    path('', include(router.urls)),
]