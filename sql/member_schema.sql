CREATE TABLE t_member (
    member_id    VARCHAR2(50)   NOT NULL,
    password     VARCHAR2(200)  NOT NULL,
    nickname     VARCHAR2(50)   NOT NULL,
    email        VARCHAR2(100),
    role         VARCHAR2(20)   DEFAULT 'USER' NOT NULL,
    join_type    VARCHAR2(20)   DEFAULT 'LOCAL' NOT NULL,
    reg_date     DATE           DEFAULT SYSDATE NOT NULL,
    CONSTRAINT pk_t_member PRIMARY KEY (member_id),
    CONSTRAINT ck_t_member_role CHECK (role IN ('USER', 'ADMIN')),
    CONSTRAINT ck_t_member_join_type CHECK (join_type IN ('LOCAL', 'KAKAO', 'NAVER', 'FACE'))
);
