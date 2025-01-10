from datetime import datetime, timedelta, timezone

import jwt

# TODO : 환경변수로 수정
SECRET_KEY = "09d25e094faa6ca2556c818166b7a9563b93f7099f6f0f4caa6cf63b88e8d3e7"
ALGORITHM = "HS256"

# TODO: 현재는 user_id에 대해서만 검증하도록 되어있지만, 이후에 필요 시 permission 관련 검증 추가 필요


def create_jwt_token(data: dict, expires_delta: timedelta | None = None):
    if not "user_id" in data:
        return None
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.now(timezone.utc) + expires_delta
    else:
        expire = datetime.now(timezone.utc) + timedelta(days=1)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt


def verify_jwt(token: str):
    try:
        decode_token = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        if not decode_token:
            raise ValueError("Invalid token!")
        # current_timestamp = datetime.now().timestamp()
        elif decode_token["exp"] <= (datetime.now().timestamp()):
            raise ValueError("Token expired!")
        return {"user_id": decode_token["user_id"], "error": None}
    except ValueError as error:
        return {"user_id": None, "error": str(error)}
