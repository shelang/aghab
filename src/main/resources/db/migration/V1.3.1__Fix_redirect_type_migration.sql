ALTER TABLE webhook_user
    RENAME COLUMN script_id TO webhook_id;

ALTER TABLE script_user
    ADD PRIMARY KEY (user_id, script_id);
ALTER TABLE webhook_user
    ADD PRIMARY KEY (user_id, webhook_id);
ALTER TABLE link_user
    ADD PRIMARY KEY (user_id, link_id);
ALTER TABLE link_expiration
    ADD PRIMARY KEY (link_id);
ALTER TABLE link_expiration
    ADD FOREIGN KEY (link_id) REFERENCES links (id);

ALTER TABLE link_alternatives
    DROP CONSTRAINT link_alternatives_pkey;
ALTER TABLE link_alternatives
    ADD CONSTRAINT link_alternatives_pkey PRIMARY KEY (link_id, key);
ALTER TABLE link_alternatives
    DROP COLUMN id;

ALTER TABLE webhook_user
    DROP CONSTRAINT fk_su_script_id;
ALTER TABLE webhook_user
    ADD CONSTRAINT fk_su_script_id FOREIGN KEY (webhook_id) REFERENCES webhooks (id);