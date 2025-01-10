"""add_fields_user

Revision ID: 46c558582522
Revises: e10a9e02b8b8
Create Date: 2025-01-10 20:45:56.590270

"""

from typing import Sequence, Union

import sqlalchemy as sa
from sqlalchemy import Column, DateTime, String

from alembic import op

# revision identifiers, used by Alembic.
revision: str = "46c558582522"
down_revision: Union[str, None] = "e10a9e02b8b8"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None

TABLE_NAME_USER = "user"


def upgrade() -> None:
    op.add_column(
        TABLE_NAME_USER,
        Column("gender", String(10), nullable=True),
    )
    op.add_column(
        TABLE_NAME_USER,
        Column("age", sa.Integer, nullable=True),
    )
    op.add_column(
        TABLE_NAME_USER,
        Column("athletic_level", String(50), nullable=True),
    )
    op.add_column(
        TABLE_NAME_USER,
        Column("workout_time_prefer", String(50), nullable=True),
    )
    op.add_column(
        TABLE_NAME_USER,
        Column("profile_image_url", String(500), nullable=True),
    )
    op.add_column(
        TABLE_NAME_USER,
        Column(
            "created_at",
            DateTime,
            server_default=sa.func.now(),
            nullable=False,
        ),
    )
    op.add_column(
        TABLE_NAME_USER,
        Column(
            "updated_at",
            DateTime,
            server_default=sa.func.now(),
            onupdate=sa.func.now(),
            nullable=True,
        ),
    )
    op.add_column(
        TABLE_NAME_USER,
        Column(
            "deleted_at",
            DateTime,
            nullable=True,
        ),
    )


def downgrade() -> None:
    op.drop_column(TABLE_NAME_USER, "gender")
    op.drop_column(TABLE_NAME_USER, "age")
    op.drop_column(TABLE_NAME_USER, "athletic_level")
    op.drop_column(TABLE_NAME_USER, "workout_time")
    op.drop_column(TABLE_NAME_USER, "profile_image_url")
    op.drop_column(TABLE_NAME_USER, "created_at")
    op.drop_column(TABLE_NAME_USER, "updated_at")
    op.drop_column(TABLE_NAME_USER, "deleted_at")
