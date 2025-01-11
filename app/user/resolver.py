from sqlmodel import Session, select

from .db_models import Oauth, User, UserSport


class UserResolver:
    def __init__(self, session: Session):
        self.session = session

    def get_user(self, user_id: int) -> User | None:
        return self.session.get(User, user_id)

    def is_user_name_exist(self, user_name: str) -> bool:
        return bool(
            self.session.exec(select(User).where(User.user_name == user_name)).first()
        )

    def create_user_and_oauth(
        self, user: User, oauth_provider: str, oauth_user_id: str
    ) -> User:
        self.session.add(user)
        self.session.flush()
        oauth = Oauth(
            provider=oauth_provider, user_id=user.user_id, oauth_user_id=oauth_user_id
        )
        self.session.add(oauth)
        return user

    def create_user_sports(self, user_id: int, sport_ids: list[int]) -> None:
        for sport_id in sport_ids:
            user_sport = UserSport(user_id=user_id, sport_id=sport_id)
            self.session.add(user_sport)
        self.session.flush()
