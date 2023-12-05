ALTER TABLE links ADD COLUMN IF NOT EXISTS webhook_status SMALLINT NOT NULL DEFAULT 0;
-- 0 is equal to unknown, 1 is has not sent yet, 2 means the webhook is sent successfully
