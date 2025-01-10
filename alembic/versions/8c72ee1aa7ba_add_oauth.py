"""add_oauth

Revision ID: 8c72ee1aa7ba
Revises: 46c558582522
Create Date: 2025-01-11 00:00:23.857517

"""

from typing import Sequence, Union

import sqlalchemy as sa
from sqlalchemy import Column, DateTime, Integer, String

from alembic import op

# revision identifiers, used by Alembic.
revision: str = "8c72ee1aa7ba"
down_revision: Union[str, None] = "46c558582522"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "oauth",
        Column("oauth_id", Integer, primary_key=True),
        Column("user_id", Integer, foreign_key=True),
        Column("provider", String(255)),
        Column("oauth_user_id", String(100)),
        Column(
            "created_at",
            DateTime,
            server_default=sa.func.now(),
            nullable=False,
        ),
        Column(
            "updated_at",
            DateTime,
            server_default=sa.func.now(),
            onupdate=sa.func.now(),
            nullable=True,
        ),
        Column(
            "deleted_at",
            DateTime,
            nullable=True,
        ),
        sa.ForeignKeyConstraint(
            ["user_id"],
            ["user.user_id"],
        ),
    )


def downgrade() -> None:
    op.drop_table("oauth")
