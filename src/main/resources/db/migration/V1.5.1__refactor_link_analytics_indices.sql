CREATE INDEX IF NOT EXISTS link_analytics_report_idx
    ON link_analytics (create_at, link_id, ip);

CREATE INDEX IF NOT EXISTS link_analytics_ip_idx
    ON link_analytics (ip, create_at, link_id);

CREATE INDEX IF NOT EXISTS link_analytics_os_idx
    ON link_analytics (os, create_at, link_id);

CREATE INDEX IF NOT EXISTS link_analytics_os_name_idx
    ON link_analytics (os_name, create_at, link_id);

CREATE INDEX IF NOT EXISTS link_analytics_os_version_idx
    ON link_analytics (os_version, create_at, link_id);

CREATE INDEX IF NOT EXISTS link_analytics_os_name_version_idx
    ON link_analytics (os_name, os_version, create_at, link_id);

CREATE INDEX IF NOT EXISTS link_analytics_device_idx
    ON link_analytics (device, create_at, link_id);

CREATE INDEX IF NOT EXISTS link_analytics_device_name_idx
    ON link_analytics (device_name, create_at, link_id);

CREATE INDEX IF NOT EXISTS link_analytics_device_brand_idx
    ON link_analytics (device_brand, create_at, link_id);

CREATE INDEX IF NOT EXISTS link_analytics_agent_name_idx
    ON link_analytics (agent_name, create_at, link_id);
