ALTER TABLE webhook_user RENAME COLUMN script_id TO webhook_id;

ALTER TABLE script_user ADD PRIMARY KEY (user_id, script_id);
ALTER TABLE webhook_user ADD PRIMARY KEY (user_id, webhook_id);
