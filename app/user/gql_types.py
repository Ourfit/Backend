import strawberry
from sqlmodel import Session
from strawberry.types import Info

from .resolver import UserResolver


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
        resolver = UserResolver(session)
        user = resolver.get_user(user_id)
        if not user:
            return None
        return UserType(
            user_id=user.user_id, user_name=user.user_name, email=user.email
        )
