from django.contrib.auth.base_user import AbstractBaseUser
from django.db import models


class Member(AbstractBaseUser):
    is_active: bool = models.BooleanField(
        default = True,
        null = False,
        db_column = "is_active",
        db_comment = "멤버 활성화 여부"
    )
    is_admin: bool = models.BooleanField(
        default = False,
        null = False,
        db_column = "is_admin",
        db_comment = "계정의 어드민 여부"
    )

    class Meta:
        db_table = "member"
        db_table_comment = "멤버 정보 (유저 + 어드민)"
        ordering = ["-id"]


class MemberProfile(models.Model):
    member: Member = models.OneToOneField(
        to = "member",
        on_delete = models.DO_NOTHING,
        null = False,
        db_column = "member_id",
        db_comment = "연결된 멤버"
    )

    class Meta:
        db_table = "member_profile"
        db_table_comment = "멤버의 프로필"
        ordering = ["-id"]
