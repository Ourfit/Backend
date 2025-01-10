from fastapi import FastAPI, Request
from fastapi.exceptions import HTTPException
from fastapi.responses import RedirectResponse
from gql import graphql_app
from oauth.kakao_manager import KakaoAPI
from starlette.middleware.sessions import SessionMiddleware

app = FastAPI()
app.add_middleware(SessionMiddleware, secret_key="your-secret-key")
app.include_router(graphql_app, prefix="/graphql")
kakao_api = KakaoAPI()


@app.get("/")
def read_root():
    return {"Hello": "World"}


@app.get("/ping")
def ping():
    return "pong"


@app.get("/login/kakao")
async def login_kakao():
    return RedirectResponse(url=kakao_api.get_code_auth_url())


@app.route("/login/kakao/callback")
async def login_kakao_callback(request: Request):
    code = request.query_params.get("code")
    token_info = await kakao_api.get_token(code)
    if "access_token" in token_info:
        request.session["access_token"] = token_info["access_token"]
        user_info = await kakao_api.get_user_info(token_info["access_token"])
        kakao_user_id = user_info["id"]
        # TODO: DB에 있는지 검사해서 로그인 관련 기능 추가 필요

        # 만약 DB에 없다면, 아래의 데이터를 프론트엔드에 반환하고 프론트엔드에서 회원가입 페이지로 이동, 회원가입 시 아래의 정보 전달
        return {
            "data": {
                "oauth_type": "kakao",
                "oauth_user_id": kakao_user_id,
            }
        }
    else:
        return HTTPException(status_code=400, detail="Failed to get access token")
