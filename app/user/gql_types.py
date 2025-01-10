import strawberry
from sqlmodel import Session
from strawberry.types import Info

from .db_models import User
from .resolver import UserResolver


@strawberry.type
class UserType:
    user_id: int
    user_name: str
    email: str


@strawberry.type
class IsUserNameAvailable:
    user_name: str
    is_available: bool
    description: str = "Check if the user name is available"


@strawberry.type
class QueryUser:
    @strawberry.field
    def user(self, info: Info, user_id: int) -> None | UserType:
        session: Session = info.context.session
        resolver = UserResolver(session)
        user = resolver.get_user(user_id)
        if not user:
            return None
        return UserType(
            user_id=user.user_id, user_name=user.user_name, email=user.email
        )

    @strawberry.field
    def is_user_name_available(self, info: Info, user_name: str) -> IsUserNameAvailable:
        session: Session = info.context.session
        resolver = UserResolver(session)
        is_available = User.validated_user_name(user_name)
        if not is_available:
            return IsUserNameAvailable(
                user_name=user_name,
                is_available=False,
                description="User name is not Korean or not in the right length",
            )

        is_available = not (resolver.is_user_name_exist(user_name))
        return IsUserNameAvailable(
            user_name=user_name,
            is_available=is_available,
            description=(
                "User name is available"
                if is_available
                else "User name is already using"
            ),
        )
