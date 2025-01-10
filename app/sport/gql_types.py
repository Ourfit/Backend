import strawberry
from sqlmodel import Session
from strawberry.types import Info

from .resolver import SportResolver


@strawberry.type
class SportType:
    sport_id: int
    korean_name: str


@strawberry.type
class QuerySport:
    @strawberry.field
    def get_sport_list(self, info: Info) -> list[SportType] | None:
        session: Session = info.context.session
        resolver = SportResolver(session)
        return resolver.get_sport_list()
