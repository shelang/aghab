CREATE TABLE IF NOT EXISTS scripts
(
    id      BIGSERIAL   NOT NULL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    timeout INTEGER     NOT NULL DEFAULT 10000,
    title   VARCHAR(140),
    content TEXT        NOT NULL
);
CREATE INDEX IF NOT EXISTS script_name_idx ON scripts (name);

CREATE TABLE IF NOT EXISTS script_user
(
    script_id BIGINT NOT NULL,
    user_id   BIGINT NOT NULL,

    CONSTRAINT fk_su_script_id FOREIGN KEY (script_id) REFERENCES scripts (id),
    CONSTRAINT fk_su_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS webhooks
(
    id   BIGSERIAL   NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    url  TEXT        NOT NULL
);
CREATE INDEX IF NOT EXISTS webhook_name_idx ON webhooks (name);

CREATE TABLE IF NOT EXISTS webhook_user
(
    script_id BIGINT NOT NULL,
    user_id   BIGINT NOT NULL,

    CONSTRAINT fk_su_script_id FOREIGN KEY (script_id) REFERENCES scripts (id),
    CONSTRAINT fk_su_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'redirect_type') THEN
            CREATE TYPE redirect_type AS ENUM ('REDIRECT', 'SCRIPT', 'IFRAME');
        END IF;
    END
$$;

ALTER TABLE links
    ADD COLUMN IF NOT EXISTS type       redirect_type,
    ADD COLUMN IF NOT EXISTS script_id  BIGINT,
    ADD COLUMN IF NOT EXISTS webhook_id BIGINT;