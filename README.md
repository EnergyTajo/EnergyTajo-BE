# 에너지타죠 프로젝트
우리 프로젝트는 자가 발전이 가능한 자전거를 도시 전역에 확장하고, 시민들이 적극적으로 참여하도록 유도한다. 사용자는 자전거에 부착된 QR 코드를 스캔하여 어플리케이션에 등록하고 운동 중 생성되는 전기를 추적할 수 있다. 운동 기록을 바탕으로 일정량의 전기가 생산되면, 포인트를 지급 받는다. 해당 포인트를 부산 지역 화폐(동백전)과 연계하여 시민들의 지역 화폐 사용을 증진시킨다.
또한, 관리자용 어플리케이션에서 완전히 충전된 자가 발전기의 위치를 표시하고 모니터링하며 보다 효율적인 유지 보수가 가능하다.
시민들의 참여로 재생 가능한 에너지를 생산하고, 이를 공공기관 및 지역 사회에 활용하여 에너지 절약과 친환경 전력 생산이 가능하다. 시민들의 건강한 라이프 스타일 지원은 물론이고, 나아가 지역 경제 활성화에 기여할 것이다. 지역 사회의 환경 보호 및 인식 향상과 지속 가능한 지역 사회가 기대된다.

# EnergyTajo-BE
BDIA Devton 2024 공모전 - 백엔드
https://energytajo.site (모바일 필수 - 사파리 권장)

# Version
- Java: 17
- Spring Boot: 3.1.4
- Spring Dependency Management: 1.1.5
- MySQL Connector
- Mysql 9.0.1
- Lombok
- Twilio SDK: 8.27.0
- Libphonenumber: 8.12.30
- JSON Web Token (jjwt): 0.11.5
- Jakarta Servlet API: 5.0.0

## 기술 스택
- Java 17
- Spring Boot 3.1.4
- MySQL
- Docker
- Nginx
- OCI (Oracle Cloud Infrastructure)

## API 문서

- `GET /api/auth/check-id`: ID 중복 체크
- `POST /api/auth/verify/send-code`: SMS 인증 코드 전송 (Twilio)
- `POST /api/auth/verify/check-code`: SMS 인증 코드 검증
- `POST /api/auth/sign-up`: 회원가입
- `POST /api/auth/sign-in`: 로그인
- `POST /api/auth/logout`: 로그아웃
- `POST /api/auth/refresh`: 토큰 갱신
- `GET /api/qr_bicycle/{bicycle_id}`: QR 인식 - 자전거 정보 조회
- `POST /api/qr_bicycle/start/{bicycle_id}`: 자가 발전 자전거 이용 시작
- `POST /api/qr_bicycle/end/{ride_id}`: 자가 발전 자전거 이용 종료
- `GET /api/ride`: 탑승 이용 내역
- `POST /api/account/create`: 계좌 등록
- `GET /api/account`: 계좌 조회
- `POST /api/account/point_to_cash`: 동백전 충전 (포인트 --전환--> 동백전)
- `GET /api/charge`: 포인트 이용 내역
