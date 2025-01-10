"""init_mate

Revision ID: e10a9e02b8b8
Revises: 1c3ceb2b7036
Create Date: 2025-01-10 20:03:13.691179

"""

from typing import Sequence, Union

import sqlalchemy as sa
from sqlalchemy import Column, DateTime, String

from alembic import op

# revision identifiers, used by Alembic.
revision: str = "e10a9e02b8b8"
down_revision: Union[str, None] = "1c3ceb2b7036"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None

TABLE_NAME_MATE = "mate"


def upgrade() -> None:
    conn = op.get_bind()
    inspector = sa.inspect(conn)
    if TABLE_NAME_MATE not in inspector.get_table_names():
        op.create_table(
            TABLE_NAME_MATE,
            Column("id", sa.Integer, primary_key=True, autoincrement=True),
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
        )


def downgrade() -> None:
    op.drop_table(TABLE_NAME_MATE)
