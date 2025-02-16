USE reservation_db;

# member 테이블 생성
# id: 자동 증가하는 기본 키
# email: 중복 불가능한 이메일
# password: 암호화된 비밀번호
# role: 'USER' 또는 'PARTNER' 값만 가질 수 있도록 ENUM으로 정의

CREATE TABLE members (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         name VARCHAR(255),
                         phone VARCHAR(50),
                         role ENUM('USER', 'PARTNER') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# store 테이블 생성
# storeName: 매장 이름
# description: 매장 설명
# location: 매장 위치
# owner_id: 매장 등록한 파트너(회원)의 id를 참조하는 외래키
# createdAt, updatedAt: 생성/수정 시각
CREATE TABLE stores (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        storeName VARCHAR(255) NOT NULL,
                        description TEXT,
                        location VARCHAR(255) NOT NULL,
                        owner_id BIGINT NOT NULL,
                        createdAt DATETIME NOT NULL,
                        updatedAt DATETIME,
                        CONSTRAINT fk_store_owner FOREIGN KEY (owner_id) REFERENCES members(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# Reservations 테이블
# store_id: 예약할 매장 id (stores 테이블의 외래키)
# member_id: 예약을 진행한 사용자 id (members 테이블의 외래키)
# reservationDateTime: 예약 일시
# status: 예약 상태('REQUESTED', 'APPROVED', 'REJECTED', 'COMPLETED')
CREATE TABLE reservations (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              store_id BIGINT NOT NULL,
                              member_id BIGINT NOT NULL,
                              reservationDateTime DATETIME NOT NULL,
                              status ENUM('REQUESTED', 'APPROVED', 'REJECTED', 'COMPLETED') NOT NULL,
                              createdAt DATETIME NOT NULL,
                              updatedAt DATETIME,
                              CONSTRAINT fk_reservation_store FOREIGN KEY (store_id) REFERENCES stores(id),
                              CONSTRAINT fk_reservation_member FOREIGN KEY (member_id) REFERENCES members(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


# Reviews 테이블
# store_id: 리뷰가 작성된 매장의 id (stores 테이블의 외래키)
# member_id: 리뷰 작성자 id (members 테이블의 외래키)
# rating: 별점 (정수)
# content: 리뷰 내용
CREATE TABLE reviews (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         store_id BIGINT NOT NULL,
                         member_id BIGINT NOT NULL,
                         rating INT NOT NULL,
                         content TEXT,
                         createdAt DATETIME NOT NULL,
                         updatedAt DATETIME,
                         CONSTRAINT fk_review_store FOREIGN KEY (store_id) REFERENCES stores(id),
                         CONSTRAINT fk_review_member FOREIGN KEY (member_id) REFERENCES members(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
