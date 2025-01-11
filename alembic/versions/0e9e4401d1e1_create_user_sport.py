"""create_user_sport

Revision ID: 0e9e4401d1e1
Revises: 8c72ee1aa7ba
Create Date: 2025-01-11 17:53:19.453150

"""

from typing import Sequence, Union

import sqlalchemy as sa
from sqlalchemy import Column, DateTime, ForeignKey, Integer

from alembic import op

# revision identifiers, used by Alembic.
revision: str = "0e9e4401d1e1"
down_revision: Union[str, None] = "8c72ee1aa7ba"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "user_sport",
        Column("user_sport_id", Integer, primary_key=True),
        Column("user_id", Integer, ForeignKey("user.user_id")),
        Column("sport_id", Integer, ForeignKey("sport.sport_id")),
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
        sa.ForeignKeyConstraint(
            ["sport_id"],
            ["sport.sport_id"],
        ),
    )


def downgrade() -> None:
    op.drop_table("user_sport")
