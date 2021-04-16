CREATE TABLE IF NOT EXISTS links
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    hash VARCHAR (20) NOT NULL UNIQUE,
    alias VARCHAR(50),
    url TEXT NOT NULL,
    status SMALLINT DEFAULT 0
);
CREATE INDEX IF NOT EXISTS link_hash_idx ON links (hash);


CREATE TABLE IF NOT EXISTS link_meta
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    link_id BIGINT,
    title VARCHAR(150),
    description VARCHAR(255),
    create_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    update_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_link_meta
      FOREIGN KEY(link_id)
        REFERENCES links(id)
        ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS link_analytics
(
    link_id BIGINT NOT NULL,
    platform VARCHAR (30),
    device VARCHAR (30),
    browser VARCHAR (30),
    location VARCHAR(5),
    ip VARCHAR(32),
    create_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS link_anal_idx ON link_analytics (link_id);


CREATE TABLE IF NOT EXISTS link_expiration
(
    link_id BIGINT NOT NULL,
    expire_at TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS link_exp_idx ON link_expiration (link_id);
CREATE INDEX IF NOT EXISTS link_exp_date_idx ON link_expiration (expire_at);


CREATE TABLE IF NOT EXISTS link_user
(
    user_id BIGINT NOT NULL,
    link_hash VARCHAR (20) NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS link_user_ul_idx ON link_user (user_id, link_hash);
