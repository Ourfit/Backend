from dataclasses import dataclass
from enum import Enum


@dataclass
class LevelInfo:
    code: str
    korean_name: str
    description: str


class AthleticLevel(str, Enum):
    BEGINNER = "beginner"
    INTERMEDIATE = "intermediate"
    ADVANCED = "advanced"

    @property
    def info(self) -> LevelInfo:
        return self._get_level_info()[self.value]

    @property
    def korean_name(self) -> str:
        return self.info.korean_name

    @property
    def description(self) -> str:
        return self.info.description

    @classmethod
    def _get_level_info(cls) -> dict[str, LevelInfo]:
        return {
            "beginner": LevelInfo(
                code="beginner",
                korean_name="초보",
                description="운동 막 시작한 단계! 운동에 조금씩 적응 중",
            ),
            "intermediate": LevelInfo(
                code="intermediate",
                korean_name="중수",
                description="운동이 생활의 일부가 된 단계! 운동이 꽤 익숙해요",
            ),
            "advanced": LevelInfo(
                code="advanced",
                korean_name="고수",
                description="운동이 이제 완전 내 몸 같은 단계! 고난도 동작도 척척",
            ),
        }


class WorkoutTime(str, Enum):
    WEEKDAY_MORNING = "weekday_morning"
    WEEKDAY_DAYTIME = "weekday_daytime"
    WEEKDAY_EVENING = "weekday_evening"
    WEEKEND_MORNING = "weekend_morning"
    WEEKEND_DAYTIME = "weekend_daytime"
    WEEKEND_EVENING = "weekend_evening"

    @property
    def korean_name(self) -> str:
        korean_names = {
            "weekday_morning": "평일 아침",
            "weekday_daytime": "평일 오전",
            "weekday_evening": "평일 저녁",
            "weekend_morning": "주말 아침",
            "weekend_daytime": "주말 오전",
            "weekend_evening": "주말 저녁",
        }
        return korean_names[self.value]
