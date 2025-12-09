-- Add role column to users table for proper role-based access control
-- Security fix: Roles should be stored in database, not derived from username

ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(20) DEFAULT 'USER';

-- Update any existing user named 'BOSS' to have the BOSS role (migration from old behavior)
UPDATE users SET role = 'BOSS' WHERE LOWER(username) = 'boss' AND role = 'USER';

-- Add index for role queries
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
