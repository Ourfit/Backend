from database import get_session
from sqlmodel import select

from .db_models import Oauth


class OauthService:
    @staticmethod
    def get_user_id_by_oauth(oauth_provider: str, oauth_user_id: str) -> int | None:
        session = next(get_session())
        oauth = session.exec(
            select(Oauth).where(
                (Oauth.provider == oauth_provider)
                & (Oauth.oauth_user_id == oauth_user_id)
            )
        ).first()
        return oauth.user_id if oauth else None
