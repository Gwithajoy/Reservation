# 매장 예약 서비스 (Reservation Service)

> **Spring Boot** 기반으로 구현된 “매장 예약 서비스”입니다.  
> 사용자는 매장을 검색하고 예약을 진행할 수 있으며, 예약 내역을 확인하고 리뷰를 작성할 수 있습니다.  
> **파트너(점장) 계정**은 매장을 등록·관리하고, 들어온 예약을 승인·거절할 수 있습니다.

---

## 1. 프로젝트 개요

- **주요 기능**
  - **회원 가입/로그인**: 일반 회원(USER), 파트너 회원(PARTNER)
  - **매장 등록**: 파트너 회원만 등록 가능
  - **예약**: 일반 회원이 매장을 예약하면, 파트너가 승인/거절
  - **도착 확인**: 예약 시간 10분 전후에 도착을 확인하면 예약이 완료(COMPLETED) 상태가 됨
  - **리뷰 작성**: 예약을 완료한 사용자만 작성 가능, 본인과 점장만 리뷰 수정·삭제 가능

- **기술 스택**
  - **Java 17 이상**
  - **Spring Boot 3.x**
  - **Spring Data JPA (Hibernate)**
  - **Spring Security** (JWT 인증/인가)
  - **MariaDB or MySQL**
  - **Gradle**

### 1.1. ERD
![image](https://github.com/user-attachments/assets/dba41116-62e0-4206-8464-bb147fec5e97)


---

## 2. 폴더 구조

```bash
reservation
 ┣ src
 ┃ ┣ main
 ┃ ┃ ┣ java
 ┃ ┃ ┃ ┗ com
 ┃ ┃ ┃   ┗ zerobase
 ┃ ┃ ┃       ┗ reservation
 ┃ ┃ ┗ resources
 ┃ ┗ test
 ┃   ┗ java
 ┃       ┗ com
 ┃           ┗ zerobase
 ┃               ┗ reservation
 ┣ build.gradle
 ┣ README.md
 ┗ ...
```

- **com.zerobase.reservation.domain:** 엔티티 클래스 (예: Member, Store, Reservation, Review 등)
- **com.zerobase.reservation.repository:** JPA Repository 인터페이스
- **com.zerobase.reservation.service:** 비즈니스 로직 처리
- **com.zerobase.reservation.controller:** REST API 엔드포인트
- **com.zerobase.reservation.security:** JWT 토큰 인증/인가 로직
- **com.zerobase.reservation.dto:** Request/Response DTO
- **src/test/java/...:** 각 기능별 테스트 (ControllerTest, ServiceTest 등)

---
## 3. 환경 설정
- Java 17 이상이 설치되어 있어야 합니다.
- MySQL 데이터베이스 사용
- application.properties 혹은 application.yml에서 DB 연결 정보(host, port, username, password)를 설정하세요.
- Gradle 기반 빌드
-  Gradle Wrapper(gradlew)가 포함되어 있으므로, 별도 설치 없이 ./gradlew 명령어로 빌드 가능합니다.

---
## 4. 주요 API 가이드

### 4.1 회원(Member) 관련

| 메서드 | 경로                    | 설명            | 요청 예시                                                                                                                                               |
|--------|-------------------------|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST   | `/api/members/register` | 회원가입        | `{ "email": "test@test.com", "password": "password", "name": "홍길동", "phone": "010-1234-5678", "role": "USER" }`                                    |
| POST   | `/api/members/login`    | 로그인 (JWT 발급)| `{ "email": "test@test.com", "password": "password" }`                                                                                                  |

### 4.2 매장(Store) 관련

| 메서드 | 경로                                           | 설명                     | 요청 예시                                                                                                                       |
|--------|------------------------------------------------|--------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| POST   | `/api/stores/register?partnerId={id}`          | 매장 등록 (파트너만)      | `{ "storeName": "맛있는 한식당", "location": "서울 강남구", "description": "정갈한 한식 전문점입니다." }`                           |
| PUT    | `/api/stores/{storeId}?partnerId={id}`         | 매장 수정 (등록자만)      | `{ "storeName": "업데이트 매장", "location": "업데이트 위치", "description": "업데이트 설명" }`                                   |
| DELETE | `/api/stores/{storeId}?partnerId={id}`         | 매장 삭제 (등록자만)      | -                                                                                                                               |
| GET    | `/api/stores/{storeId}`                        | 매장 상세 조회           | -                                                                                                                               |

### 4.3 예약(Reservation) 관련

| 메서드 | 경로                                                         | 설명                    | 요청 예시                                                                              |
|--------|--------------------------------------------------------------|-------------------------|----------------------------------------------------------------------------------------|
| POST   | `/api/reservations?memberId={id}`                            | 예약 생성 (사용자)      | `{ "storeId": 1, "reservationDateTime": "2025-03-01T19:00:00" }`                         |
| POST   | `/api/reservations/{reservationId}/approve?partnerId={id}`   | 예약 승인 (파트너)      | -                                                                                      |
| POST   | `/api/reservations/{reservationId}/decline?partnerId={id}`   | 예약 거절 (파트너)      | -                                                                                      |
| POST   | `/api/reservations/{reservationId}/confirm`                | 도착 확인 (키오스크 등)  | -                                                                                      |

### 4.4 리뷰(Review) 관련

| 메서드 | 경로                                     | 설명                                        | 요청 예시                                                       |
|--------|------------------------------------------|---------------------------------------------|-----------------------------------------------------------------|
| POST   | `/api/reviews?memberId={id}`             | 리뷰 작성 (예약 완료 사용자만 가능)           | `{ "storeId": 1, "rating": 5, "content": "Excellent!" }`         |
| PUT    | `/api/reviews/{reviewId}?memberId={id}`  | 리뷰 수정 (작성자만)                         | `{ "rating": 4, "content": "조금 아쉬웠어요" }`                   |
| DELETE | `/api/reviews/{reviewId}?memberId={id}`  | 리뷰 삭제 (작성자 또는 매장 관리자 가능)     | -                                                               |



---
## 5.테스트
단위 테스트: 서비스 계층의 로직을 검증하기 위해 JUnit5와 Mockito를 사용합니다.
통합 테스트: REST API 엔드포인트 검증을 위해 Spring Boot의 MockMvc를 사용합니다.
예: MemberControllerTest, StoreControllerTest, ReservationControllerTest, ReviewControllerTest


---
## 6.JWT 인증 방식
- 사용자가 로그인에 성공하면, 서버는 JWT 토큰을 발급합니다.
- 클라이언트는 이후 API 호출 시 Authorization 헤더에 아래 형식으로 토큰을 포함시킵니다.
```Authorization: Bearer <JWT토큰>```
- 토큰 발급 및 검증은 JwtTokenProvider에서 관리하며, 비밀 키(JWT_SECRET)와 만료 기간(JWT_EXPIRATION)을 기반으로 합니다.


---
## 7.기여 방법
- Fork 저장소
- 새로운 브랜치를 생성하여 기능 추가 또는 버그 수정을 진행
- 변경 사항을 커밋한 후 Pull Request를 생성


---
## 8.문의
이메일: gleewithajoy@gmail.com
