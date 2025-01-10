"""init_user

Revision ID: 1c3ceb2b7036
Revises: 490234416869
Create Date: 2025-01-10 19:59:35.629272

"""

from typing import Sequence, Union

import sqlalchemy as sa

from alembic import op

# revision identifiers, used by Alembic.
revision: str = "1c3ceb2b7036"
down_revision: Union[str, None] = "490234416869"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None

TABLE_NAME_USER = "user"


def upgrade() -> None:
    conn = op.get_bind()
    inspector = sa.inspect(conn)
    if TABLE_NAME_USER not in inspector.get_table_names():
        op.create_table(
            TABLE_NAME_USER,
            sa.Column("user_id", sa.Integer, primary_key=True, autoincrement=True),
            sa.Column(
                "user_name",
                sa.String(255),
                nullable=False,
            ),
            sa.Column(
                "hashed_password",
                sa.String,
            ),
            sa.Column(
                "email",
                sa.String(255),
                nullable=False,
            ),
        )


def downgrade() -> None:
    op.drop_table(TABLE_NAME_USER)
