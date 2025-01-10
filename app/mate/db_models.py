from datetime import datetime

import sqlalchemy as sa
from sqlmodel import Column, DateTime, Field, SQLModel


class Mate(SQLModel, table=True):
    __tablename__ = "mate"

    id: int = Field(primary_key=True)
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
