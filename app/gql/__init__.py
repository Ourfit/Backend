# graphql이 아닌 gql이라고 명명한 이유: strawberry에서 graphql이라는 이름을 사용하고 있어서, 의존성 문제 발생
from functools import cached_property
from operator import itemgetter

import strawberry
from database import get_session
from fastapi import Depends
from middlewares.jwt_middleware import verify_jwt
from sport.gql_types import QuerySport
from sqlmodel import Session
from strawberry.fastapi import BaseContext, GraphQLRouter
from user.gql_types import MutationUser, QueryUser


class GraphQLContext(BaseContext):
    def __init__(self, session: Session):
        self.session = session

    @property
    def session(self) -> Session:
        return self._session

    @session.setter
    def session(self, session: Session):
        self._session = session

    @cached_property
    def user_id(self) -> int | None:
        if not self.request:
            return None

        bearer = self.request.headers.get("Authorization", None)
        # Bearer eyJhbGciOiJIUzI1Ni~~.eyJ19~~.K6VjSSt9RRada6iOpqJho~~
        if not bearer:
            return None
        jwt = bearer[7:]
        user_id, error = itemgetter("user_id", "error")(verify_jwt(jwt))
        return user_id


async def get_db_session_context(
    session: Session = Depends(get_session),
) -> GraphQLContext:
    return GraphQLContext(session=session)


@strawberry.type
class QueryDefault(QueryUser, QuerySport):
    # 다른 도메인의 query를 사용하려면, QueryDefault에 해당 도메인의 query를 상속 시키기 (ex. QueryDefault(QueryUser, QueryChallenge))
    @strawberry.field
    def hello(self) -> str:
        return "Hello, World!"


@strawberry.type
class MutationDefault(MutationUser):
    pass


schema = strawberry.Schema(query=QueryDefault, mutation=MutationDefault)

graphql_app = GraphQLRouter(
    schema,
    context_getter=get_db_session_context,
)
