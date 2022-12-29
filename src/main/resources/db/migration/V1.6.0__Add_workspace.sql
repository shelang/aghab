CREATE TABLE IF NOT EXISTS workspaces
(
    id              BIGSERIAL   NOT NULL PRIMARY KEY,
    name            VARCHAR(50) NOT NULL,
    creator_user_id BIGINT  NOT NULL
);
CREATE INDEX IF NOT EXISTS workspace_name_idx ON workspaces (name);

CREATE TABLE IF NOT EXISTS workspace_user
(
    workspace_id BIGINT      NOT NULL,
    user_id      BIGINT      NOT NULL,
    create_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY (workspace_id, user_id),

    CONSTRAINT fk_wu_workspace_id FOREIGN KEY (workspace_id) REFERENCES workspaces (id),
    CONSTRAINT fk_wu_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE INDEX IF NOT EXISTS workspace_user_u_idx ON workspace_user (user_id);

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS need_change_password BOOLEAN NOT NULL DEFAULT FALSE;
