CREATE TABLE IF NOT EXISTS webhook_link
(
    webhook_id BIGINT,
    link_id    BIGINT,
    count      BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (link_id, webhook_id)
);
