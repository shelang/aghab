CREATE TABLE IF NOT EXISTS link_workspace
(
    link_hash VARCHAR(20) NOT NULL,
    user_id   BIGINT      NOT NULL,
    create_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    link_id   BIGINT,
    PRIMARY KEY (link_hash, user_id)
);