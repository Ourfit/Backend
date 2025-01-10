from sqlmodel import Field, SQLModel


class Sport(SQLModel, table=True):
    sport_id: int = Field(primary_key=True)
    korean_name: str | None = Field(default=None, max_length=255)

    def __init__(self, sport_id: int, korean_name: str, **kwargs):
        if sport_id is not None:
            self.sport_id = sport_id
        if korean_name is not None:
            self.korean_name = korean_name
        super().__init__(**kwargs)

    def __str__(self):
        return f"Sport: {self.sport_id}:<{self.korean_name}>"
