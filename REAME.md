### 개발환경 설정

1. 가상환경 생성

- 준비: https://jaemunbro.medium.com/python-virtualenv-venv-%EC%84%A4%EC%A0%95-aaf0e7c2d24e
- CLI 명령어
  ```bash
  virtualenv .venv
  source .venv/bin/activate
  install -r requirements.txt
  ```

2. vscode 설정

- ctrl+shift+P -> select interpreter -> 생성한 .venv 선택
- 포매터 적용하기
  - https://super-son.tistory.com/10 (format on save)
  - extension 설치: https://marketplace.visualstudio.com/items?itemName=ms-python.black-formatter
