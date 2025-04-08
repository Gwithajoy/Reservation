# 🏪 매장 예약 서비스 (Reservation Service)

Spring Boot 기반의 매장 예약 서비스입니다.  
사용자는 매장을 검색하고 예약 및 리뷰를 작성할 수 있으며,  
파트너(점장)는 매장을 등록하고 들어온 예약을 승인/거절할 수 있습니다.

---

## 🧭 프로젝트 개요

### ✅ 주요 기능
- 👥 회원 가입 및 로그인 (일반 사용자 / 파트너 구분)
- 🏬 매장 등록 및 수정/삭제 (파트너만 가능)
- 📅 예약 생성 및 승인/거절 (사용자 ↔ 파트너)
- ✅ 도착 확인 기능 (예약 완료 처리)
- ⭐ 리뷰 작성 및 수정/삭제 (예약 완료자만 가능)

### ⚙️ 기술 스택
- Java 17
- Spring Boot 3.x
- Spring Data JPA (Hibernate)
- Spring Security + JWT 인증/인가
- MariaDB or MySQL
- Gradle

---

## 🗂️ 프로젝트 구조

```
reservation
 ┣ src
 ┃ ┣ main
 ┃ ┃ ┣ java
 ┃ ┃ ┃ ┗ com.zerobase.reservation
 ┃ ┃ ┗ resources
 ┃ ┗ test
 ┃   ┗ java
 ┃       ┗ com.zerobase.reservation
 ┣ build.gradle
 ┗ README.md
```

### 📦 주요 패키지 구성

| 패키지 | 설명 |
|--------|------|
| `domain` | 엔티티 클래스 (Member, Store, Reservation, Review 등) |
| `repository` | JPA Repository 인터페이스 |
| `service` | 비즈니스 로직 처리 |
| `controller` | REST API 엔드포인트 |
| `security` | JWT 인증/인가 처리 |
| `dto` | Request / Response DTO |

---

## 🔧 환경 설정

- Java 17 이상
- MySQL or MariaDB 사용
- `application.properties` 또는 `application.yml`에 DB 연결 정보 설정

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/reservation
spring.datasource.username=root
spring.datasource.password=your_password

jwt.secret=your_jwt_secret_key
jwt.expiration=3600000
```

- Gradle 기반 빌드 (`./gradlew`로 실행 가능)

---

## 🗃️ ERD


---

## 🔐 JWT 인증 방식

- 로그인 시 JWT 토큰을 발급
- 이후 API 호출 시 `Authorization: Bearer <JWT>` 형식으로 헤더에 포함
- JWT 발급 및 검증은 `JwtTokenProvider`에서 관리

---

## 📘 주요 API 요약

### 👤 회원(Member) API

| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | `/api/members/register` | 회원가입 |
| POST | `/api/members/login` | 로그인 (JWT 발급) |

### 🏬 매장(Store) API

| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | `/api/stores/register?partnerId={id}` | 매장 등록 |
| PUT | `/api/stores/{storeId}?partnerId={id}` | 매장 수정 |
| DELETE | `/api/stores/{storeId}?partnerId={id}` | 매장 삭제 |
| GET | `/api/stores/{storeId}` | 매장 상세 조회 |

### 📅 예약(Reservation) API

| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | `/api/reservations?memberId={id}` | 예약 생성 |
| POST | `/api/reservations/{id}/approve?partnerId={id}` | 예약 승인 |
| POST | `/api/reservations/{id}/decline?partnerId={id}` | 예약 거절 |
| POST | `/api/reservations/{id}/confirm` | 도착 확인 |

### ✍️ 리뷰(Review) API

| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | `/api/reviews?memberId={id}` | 리뷰 작성 |
| PUT | `/api/reviews/{id}?memberId={id}` | 리뷰 수정 |
| DELETE | `/api/reviews/{id}?memberId={id}` | 리뷰 삭제 |

---

## 🧪 테스트

- **단위 테스트**: JUnit5 + Mockito  
- **통합 테스트**: MockMvc를 이용한 REST API 테스트  
- **예시**: `MemberControllerTest`, `StoreControllerTest`, `ReservationControllerTest`, `ReviewControllerTest`

---

## 📬 문의

해당 프로젝트에 대한 문의는 아래 이메일로 연락주세요.

📧 gleewithajoy@gmail.com

