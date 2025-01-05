from typing import Annotated

from fastapi import Depends
from sqlmodel import Session, SQLModel, create_engine

from config import settings

# create_db_and_tables 명령어로 특정 테이블을 생성하려면 아래에 생성한 모델의 import를 추가해야 함
from . import user

DATABASE_URL = settings.DB_URL

engine = create_engine(DATABASE_URL)


def create_db_and_tables():
    SQLModel.metadata.create_all(engine)


def get_session():
    with Session(engine) as session:
        yield session


SessionDep = Annotated[Session, Depends(get_session)]
