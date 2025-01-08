import os

from pydantic_settings import BaseSettings

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ENV_FILE_DIR = os.path.join(BASE_DIR, ".env")


class Settings(BaseSettings):
    DB_URL: str

    class Config:
        env_file = ENV_FILE_DIR
        case_sensitive = True


settings = Settings()
