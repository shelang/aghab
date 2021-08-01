CREATE TABLE IF NOT EXISTS link_alternatives
(
    id      BIGSERIAL   NOT NULL PRIMARY KEY,
    link_id BIGINT      NOT NULL,
    key     VARCHAR(50) NOT NULL,
    url     TEXT        NOT NULL,

    CONSTRAINT fk_link_alternatives
        FOREIGN KEY (link_id)
            REFERENCES links (id)
            ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS link_alt_key_idx ON link_alternatives (key);

