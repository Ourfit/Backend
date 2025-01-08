from sqlmodel import Field, SQLModel


class User(SQLModel, table=True):
    user_id: int | None = Field(default=None, primary_key=True)
    user_name: str
    email: str
    hashed_password: str
