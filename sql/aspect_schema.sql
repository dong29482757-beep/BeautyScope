CREATE TABLE t_aspect_sentiment (
    platform    VARCHAR2(20)    NOT NULL,
    product_id  VARCHAR2(50)    NOT NULL,
    aspect      VARCHAR2(30)    NOT NULL,
    sentiment   VARCHAR2(20)    NOT NULL,
    cnt         NUMBER(10)      NOT NULL,
    CONSTRAINT pk_t_aspect_sentiment PRIMARY KEY (platform, product_id, aspect, sentiment),
    CONSTRAINT fk_t_aspect_sentiment_product FOREIGN KEY (platform, product_id)
        REFERENCES t_product (platform, product_id)
);

CREATE INDEX idx_t_aspect_sentiment_product ON t_aspect_sentiment (platform, product_id);
