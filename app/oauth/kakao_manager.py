import httpx


class KakaoAPI:
    def __init__(self):
        self.client_id = "7e54f56c4b6cac1742e3e481f4dbd718"  # TODO: 환경변수로 수정
        self.redirect_uri = (
            "http://localhost:8000/login/kakao/callback"  # TODO: 환경변수로 수정
        )
        self.rest_api_key = self.client_id

    def get_code_auth_url(self):
        return f"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id={self.rest_api_key}&redirect_uri={self.redirect_uri}"

    async def get_token(self, code):
        # 카카오로부터 인증 코드를 사용해 액세스 토큰 요청
        token_request_url = "https://kauth.kakao.com/oauth/token"
        token_request_payload = {
            "grant_type": "authorization_code",
            "client_id": self.client_id,
            "redirect_uri": self.redirect_uri,
            "code": code,
        }

        async with httpx.AsyncClient() as client:
            response = await client.post(token_request_url, data=token_request_payload)
        result = response.json()
        return result

    async def get_user_info(self, access_token):
        # 액세스 토큰을 사용하여 카카오로부터 사용자 정보 요청
        userinfo_endpoint = "https://kapi.kakao.com/v2/user/me"
        headers = {"Authorization": f"Bearer {access_token}"}

        async with httpx.AsyncClient() as client:
            response = await client.get(userinfo_endpoint, headers=headers)
        return response.json() if response.status_code == 200 else None

    async def refreshAccessToken(self, clientId, refresh_token):
        # 리프레시 토큰을 사용하여 액세스 토큰 갱신 요청
        url = "https://kauth.kakao.com/oauth/token"
        payload = {
            "grant_type": "refresh_token",
            "client_id": clientId,
            "refresh_token": refresh_token,
        }

        async with httpx.AsyncClient() as client:
            response = await client.post(url, data=payload)
        refreshToken = response.json()
        return refreshToken
