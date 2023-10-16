DROP TABLE IF EXISTS link_workspace;
CREATE TABLE IF NOT EXISTS link_workspace
(
    link_hash    VARCHAR(255) NOT NULL ,
    workspace_id BIGINT         NOT NULL ,
    create_at    TIMESTAMPTZ,
    link_id      BIGINT,
    PRIMARY KEY (link_hash, workspace_id)
);
CREATE INDEX IF NOT EXISTS link_workspace_wl_idx ON link_workspace (workspace_id);
CREATE INDEX IF NOT EXISTS link_workspace_create_at_idx ON link_workspace (create_at);
