-- rename existing

BEGIN;

ALTER TABLE link_analytics
    RENAME COLUMN platform TO os;

ALTER TABLE link_analytics
    RENAME COLUMN browser TO agent;

COMMIT;

-- Add new columns

BEGIN;

ALTER TABLE link_analytics
    ADD COLUMN IF NOT EXISTS os_name VARCHAR (30),
    ADD COLUMN IF NOT EXISTS os_version VARCHAR (20),
    ADD COLUMN IF NOT EXISTS device_name VARCHAR (50),
    ADD COLUMN IF NOT EXISTS device_brand VARCHAR (50),
    ADD COLUMN IF NOT EXISTS agent_name VARCHAR (50),
    ADD COLUMN IF NOT EXISTS agent_version VARCHAR (20);
COMMIT;