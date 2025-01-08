from datetime import datetime

from sqlmodel import Field, SQLModel


class Mate(SQLModel, table=True):
    __tablename__ = "mate"

    id: int = Field(primary_key=True)
    created_at: datetime
    updated_at: datetime | None = None
    deleted_at: datetime | None = None
