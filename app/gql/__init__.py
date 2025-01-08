# graphql이 아닌 gql이라고 명명한 이유: strawberry에서 graphql이라는 이름을 사용하고 있어서, 의존성 문제 발생
import strawberry
from fastapi import Depends
from sqlmodel import Session
from strawberry.fastapi import BaseContext, GraphQLRouter

from database import get_session
from user.gql_types import QueryUser


class GraphQLContext(BaseContext):
    def __init__(self, session: Session):
        self.session = session

    @property
    def session(self) -> Session:
        return self._session

    @session.setter
    def session(self, session: Session):
        self._session = session


async def get_db_session_context(
    session: Session = Depends(get_session),
) -> GraphQLContext:
    return GraphQLContext(session=session)


@strawberry.type
class QueryDefault(QueryUser):
    # 다른 도메인의 query를 사용하려면, QueryDefault에 해당 도메인의 query를 상속 시키기 (ex. QueryDefault(QueryUser, QueryChallenge))
    @strawberry.field
    def hello(self) -> str:
        return "Hello, World!"


@strawberry.type
class MutationDefault:
    async def notify_new_flavour(name: str):
        print(name)

    @strawberry.mutation
    def create_flavour(self, name: str, info: strawberry.Info) -> bool:
        info.context["background_tasks"].add_task(self.notify_new_flavour, name)
        return True


schema = strawberry.Schema(query=QueryDefault, mutation=MutationDefault)

graphql_app = GraphQLRouter(
    schema,
    context_getter=get_db_session_context,
)
