from datetime import datetime
from typing import Optional

from sqlmodel import SQLModel, Field


class Mate(SQLModel, table=True):
    __tablename__ = "mate"

    id: int = Field(primary_key=True)
    created_at: datetime
    updated_at: datetime | None = None
    deleted_at: Optional | None = None