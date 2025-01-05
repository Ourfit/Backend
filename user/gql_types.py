import strawberry
from sqlmodel import Session
from strawberry.types import Info

from .db_models import User


@strawberry.type
class UserType:
    user_id: int
    user_name: str
    email: str


@strawberry.type
class QueryUser:
    @strawberry.field
    def get_user(self, info: Info, user_id: int) -> None | UserType:
        session: Session = info.context.session
        user = session.get(User, user_id)
        if not user:
            return None
        return UserType(
            user_id=user.user_id, user_name=user.user_name, email=user.email
        )
