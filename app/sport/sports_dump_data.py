from enum import Enum


class Sports(Enum):
    GYM = (1, "헬스")
    PILATES = (2, "필라테스")
    SWIMMING = (3, "수영")
    DANCE = (4, "댄스")
    SQUASH = (5, "스쿼시")
    BOXING = (6, "복싱")
    GOLF = (7, "골프")
    TENNIS = (8, "테니스")
    YOGA = (9, "요가")
    CROSSFIT = (10, "크로스핏")
    CLIMBING = (11, "클라이밍")
    TAEKWONDO = (12, "태권도")
    HAPKIDO = (13, "합기도")
    AEROBICS = (14, "에어로빅")
    BALLET = (15, "발레")
    DANCING_ART = (16, "무용")
    BADMINTON = (17, "배드민턴")
    BOWLING = (18, "볼링")
    JIUJITSU = (19, "주짓수")
    BASKETBALL = (20, "농구")
    SOCCER = (21, "축구")
    VOLLEYBALL = (22, "배구")
    TABLE_TENNIS = (23, "탁구")
    JUDO = (24, "유도")
    KENDO = (25, "검도")

    def __init__(self, id, korean_name):
        self.sport_id = id
        self.korean_name = korean_name
