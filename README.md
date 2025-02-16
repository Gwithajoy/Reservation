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
