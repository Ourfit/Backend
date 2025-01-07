from sqlmodel import Session, SQLModel, create_engine

from config import settings

from user import db_models
from mate import db_models

DATABASE_URL = settings.DB_URL

engine = create_engine(DATABASE_URL)


def create_db_and_tables():
    SQLModel.metadata.create_all(engine)


def get_session():
    with Session(engine) as session:
        yield session
