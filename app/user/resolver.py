from sqlmodel import Session, select

from .db_models import User


class UserResolver:
    def __init__(self, session: Session):
        self.session = session

    def get_user(self, user_id: int) -> User | None:
        return self.session.get(User, user_id)

    def is_user_name_exist(self, user_name: str) -> bool:
        return bool(
            self.session.exec(select(User).where(User.user_name == user_name)).first()
        )
