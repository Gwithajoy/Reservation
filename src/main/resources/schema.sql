-- 회원(Member) 테이블 생성
CREATE TABLE reservation_db.members (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         name VARCHAR(255),
                         phone VARCHAR(50),
                         role ENUM('USER', 'PARTNER') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 매장(Store) 테이블 생성
CREATE TABLE reservation_db.stores (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        store_name VARCHAR(255) NOT NULL,
                        description TEXT,
                        location VARCHAR(255) NOT NULL,
                        owner_id BIGINT NOT NULL,
                        created_at DATETIME NOT NULL,
                        updated_at DATETIME,
                        CONSTRAINT fk_store_owner FOREIGN KEY (owner_id) REFERENCES members(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 예약(Reservation) 테이블 생성
CREATE TABLE reservation_db.reservations (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              store_id BIGINT NOT NULL,
                              member_id BIGINT NOT NULL,
                              reservation_date_time DATETIME NOT NULL,
                              status ENUM('REQUESTED', 'APPROVED', 'REJECTED', 'COMPLETED') NOT NULL,
                              created_at DATETIME NOT NULL,
                              updated_at DATETIME,
                              CONSTRAINT fk_reservation_store FOREIGN KEY (store_id) REFERENCES stores(id) ON DELETE CASCADE,
                              CONSTRAINT fk_reservation_member FOREIGN KEY (member_id) REFERENCES members(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 리뷰(Review) 테이블 생성
CREATE TABLE reservation_db.reviews (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         store_id BIGINT NOT NULL,
                         member_id BIGINT NOT NULL,
                         rating INT NOT NULL,
                         content TEXT,
                         created_at DATETIME NOT NULL,
                         updated_at DATETIME,
                         CONSTRAINT fk_review_store FOREIGN KEY (store_id) REFERENCES stores(id) ON DELETE CASCADE,
                         CONSTRAINT fk_review_member FOREIGN KEY (member_id) REFERENCES members(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- 파트너 회원 생성 (이미 가입되어 있으면 생략)
INSERT INTO reservation_db.members (email, password, name, phone, role)
VALUES ('partner@exmaple.com', '암호화된비밀번호', '홍길동', '010-1111-2222', 'PARTNER');

-- 매장 등록 (홍길동 회원의 id가 1이라고 가정)
INSERT INTO reservation_db.stores (store_name, description, location, owner_id, created_at, updated_at)
VALUES ('맛있는 한식당', '정갈한 한식 전문점입니다.', '서울 강남구', 1, NOW(), NOW());

-- 예약 생성 (예약은 일반 회원에 의해 생성되어야 하므로 일반 회원도 필요합니다.)
-- 일반 회원 생성 예시:
INSERT INTO reservation_db.members (email, password, name, phone, role)
VALUES ('user@example.com', '암호화된비밀번호', '김영희', '010-3333-4444', 'USER');

-- 예약 생성 (일반 회원 id가 2라고 가정, 매장 id가 1)
INSERT INTO reservation_db.reservations (store_id, member_id, reservation_date_time, status, created_at, updated_at)
VALUES (1, 2, '2025-03-01 19:00:00', 'REQUESTED', NOW(), NOW());

-- 리뷰 생성 (예약 후 리뷰 작성, 예시: 일반 회원 id 2, 매장 id 1)
INSERT INTO reservation_db.reviews (store_id, member_id, rating, content, created_at, updated_at)
VALUES (1, 2, 5, '매우 만족스러운 경험이었습니다.', NOW(), NOW());
