from sqlmodel import Session, select

from .db_models import Sport


class SportResolver:
    def __init__(self, session: Session):
        self.session = session

    def get_sport_list(self) -> list[Sport] | None:
        sports_list = self.session.exec(select(Sport)).all()
        return sports_list if sports_list else None
