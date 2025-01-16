import json

from fastapi import FastAPI, Request, Response
from fastapi.exceptions import HTTPException
from fastapi.responses import RedirectResponse
from gql import graphql_app
from oauth.kakao_manager import KakaoAPI
from security.jwt_manager import create_jwt_token
from starlette.middleware.sessions import SessionMiddleware
from user.service import OauthService

app = FastAPI()
# TODO: secret_key를 환경변수로 설정
# kakao API에서 Redirect를 사용하기 때문에 HTTP를 사용해야 하고, 여기서 http에 대한 sesssion을 필요함.
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
        ourfit_user_id = OauthService.get_user_id_by_oauth(
            oauth_provider="kakao", oauth_user_id=kakao_user_id
        )
        token = create_jwt_token({"user_id": ourfit_user_id}) if ourfit_user_id else ""
        response = Response(
            content=json.dumps(
                {
                    "oauth_user_id": kakao_user_id,
                    "provider": "kakao",
                    "Authorization": f"Bearer {token}",
                }
            ),
        )
        return response
    else:
        return HTTPException(status_code=400, detail="Failed to get access token")
