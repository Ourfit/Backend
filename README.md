### 개발환경 설정

1. 가상환경 생성

- 준비: https://jaemunbro.medium.com/python-virtualenv-venv-%EC%84%A4%EC%A0%95-aaf0e7c2d24e
- CLI 명령어
  ```bash
  brew install mysql pkg-config
  virtualenv .venv
  source .venv/bin/activate
  install -r requirements.txt
  ```

2. vscode 설정

- ctrl+shift+P -> select interpreter -> 생성한 .venv 선택
- 포매터 적용하기
  - https://super-son.tistory.com/10 (format on save)
  - extension 설치: https://marketplace.visualstudio.com/items?itemName=ms-python.black-formatter

3. (optional) DB 실행

- docker build -f db_sample.dockerfile -t custom-mysql .
- docker run -d -p 3308:3306 --name mysql_container custom-mysql

3. env파일 생성

- 위치: root/.env (main.py와 같은 레벨)

4. 실행

- fastapi dev main.py
- http://127.0.0.1:8000/initialize_databse 으로 접속해서 user 테이블 생성
