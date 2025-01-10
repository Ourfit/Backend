"""init_sport

Revision ID: 490234416869
Revises: 
Create Date: 2025-01-10 19:53:14.803070

"""

from typing import Sequence, Union

import sqlalchemy as sa

from alembic import op
from app.sport.sports_dump_data import Sports

# revision identifiers, used by Alembic.
revision: str = "490234416869"
down_revision: Union[str, None] = None
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None

TABLE_NAME_SPORT = "sport"


def upgrade() -> None:
    conn = op.get_bind()
    inspector = sa.inspect(conn)
    if TABLE_NAME_SPORT not in inspector.get_table_names():
        op.create_table(
            TABLE_NAME_SPORT,
            sa.Column("sport_id", sa.Integer, primary_key=True, autoincrement=True),
            sa.Column(
                "korean_name",
                sa.String(255),
                nullable=False,
            ),
        )

    insert_query = sa.sql.text(
        f"INSERT INTO {TABLE_NAME_SPORT} (sport_id, korean_name) VALUES (:sport_id, :korean_name)"
    )

    for sport in Sports:
        sport_id, korean_name = sport.value
        conn.execute(
            insert_query,
            {
                "sport_id": sport_id,
                "korean_name": korean_name,
            },
        )


def downgrade() -> None:
    op.drop_table(TABLE_NAME_SPORT)
