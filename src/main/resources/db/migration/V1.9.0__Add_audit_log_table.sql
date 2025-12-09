CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    workspace_id BIGINT,
    event_type VARCHAR(50) NOT NULL,
    event_data TEXT,
    ip_address VARCHAR(45),
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_log_user_id ON audit_log(user_id);
CREATE INDEX idx_audit_log_workspace_id ON audit_log(workspace_id);
CREATE INDEX idx_audit_log_event_type ON audit_log(event_type);
CREATE INDEX idx_audit_log_create_at ON audit_log(create_at);
