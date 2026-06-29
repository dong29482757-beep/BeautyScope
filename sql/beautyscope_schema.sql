-- BeautyScope 1차 스키마 (Oracle, hr 계정)
-- t_board / t_reply와 이름이 겹치지 않게 t_product / t_review로 분리

DROP TABLE t_review;
DROP TABLE t_product;
DROP SEQUENCE seq_t_review_no;

CREATE TABLE t_product (
    platform        VARCHAR2(20)    NOT NULL,
    product_id      VARCHAR2(20)    NOT NULL,
    product_name    VARCHAR2(300)   NOT NULL,
    brand_name      VARCHAR2(100),
    category        VARCHAR2(30),
    review_count    NUMBER          DEFAULT 0,
    avg_rating      NUMBER(4,2),
    n_negative      NUMBER          DEFAULT 0,
    n_neutral       NUMBER          DEFAULT 0,
    n_positive      NUMBER          DEFAULT 0,
    CONSTRAINT pk_t_product PRIMARY KEY (platform, product_id)
);

CREATE SEQUENCE seq_t_review_no START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE t_review (
    review_no       NUMBER          NOT NULL,
    platform        VARCHAR2(20)    NOT NULL,
    product_id      VARCHAR2(20)    NOT NULL,
    rating          NUMBER(2,1),
    sentiment       VARCHAR2(10),
    review_content  CLOB,
    review_date     VARCHAR2(10),
    nickname        VARCHAR2(50),
    CONSTRAINT pk_t_review PRIMARY KEY (review_no),
    CONSTRAINT fk_t_review_product FOREIGN KEY (platform, product_id)
        REFERENCES t_product (platform, product_id)
);

-- 상세페이지 리뷰 목록/페이징/검색에서 product 기준 조회가 압도적으로 많음
CREATE INDEX idx_t_review_product ON t_review (platform, product_id);

COMMIT;
