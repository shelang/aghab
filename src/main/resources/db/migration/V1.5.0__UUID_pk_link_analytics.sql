ALTER TABLE link_analytics
    ADD COLUMN IF NOT EXISTS id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid();