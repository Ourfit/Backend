from datetime import datetime

import sqlalchemy as sa
from gql.gql_typs import AthleticLevel, WorkoutTime
from sqlmodel import Column, DateTime, Field, SQLModel


class User(SQLModel, table=True):
    user_id: int | None = Field(default=None, primary_key=True)
    # 비즈니스 규칙 - 닉네임은 중복되지 않아야 함, 닉네임은 한글로만 구성되어야 함, 닉네임은 12자 이내로 구성되어야 함
    user_name: str = Field(max_length=255)
    email: str
    hashed_password: str
    gender: str | None = None
    age: int | None = None
    athletic_level: AthleticLevel | None = None
    workout_time_prefer: WorkoutTime | None = None
    profile_image_url: str | None = None
    created_at: datetime = Field(
        sa_column=Column(
            DateTime(timezone=True), server_default=sa.func.now(), nullable=False
        )
    )
    updated_at: datetime = Field(
        sa_column=Column(
            DateTime(timezone=True),
            server_default=sa.func.now(),
            onupdate=sa.func.now(),
            nullable=False,
        )
    )
    deleted_at: datetime | None = None

    # TODO: 적절한 위치 찾아서 이동하기
    @staticmethod
    def validated_user_name(user_name: str) -> bool:
        # 닉네임은 1자 이상, 12자 이내로 구성되어야 함
        if not 1 <= len(user_name) <= 12:
            return False
        # 닉네임은 한글로만 구성되어야 함
        if not all(ord("가") <= ord(c) <= ord("힣") for c in user_name):
            return False
        return True


class Oauth(SQLModel, table=True):
    # 하나의 계정에 여러개의 Oauth가 연결될 수 있음
    oauth_id: int | None = Field(default=None, primary_key=True)
    user_id: int = Field(foreign_key="user.user_id")
    provider: str

    # 여러 플랫폼에서 int를 사용하는지 str을 사용하는지 몰라 일단 str로 설정
    oauth_user_id: str
    created_at: datetime = Field(
        sa_column=Column(
            DateTime(timezone=True), server_default=sa.func.now(), nullable=False
        )
    )
    updated_at: datetime = Field(
        sa_column=Column(
            DateTime(timezone=True),
            server_default=sa.func.now(),
            onupdate=sa.func.now(),
            nullable=False,
        )
    )
    deleted_at: datetime | None = None
