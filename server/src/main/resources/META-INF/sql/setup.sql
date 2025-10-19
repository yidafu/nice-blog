-- H2 数据库使用 MERGE INTO 替代 INSERT OR IGNORE
MERGE INTO B_CONFIGURATION (id, config_key, config_value) KEY(id) VALUES(1, 'appearance.site_title', 'Dov Yih');

MERGE INTO B_CONFIGURATION (id, config_key, config_value) KEY(id) VALUES(2, 'appearance.github_url', 'https://github.com/yidafu');

-- 测试每分钟执行 0 0/1 * * * ?
-- 正式每天凌晨1点执行 0 0 1 1/1 * ?
MERGE INTO B_CONFIGURATION (id, config_key, config_value) KEY(id) VALUES(3, 'synchronous.cron_expr', '0 0/1 * * * ?');

MERGE INTO B_CONFIGURATION (id, config_key, config_value) KEY(id) VALUES(4, 'data_source.type', 'git');
MERGE INTO B_CONFIGURATION (id, config_key, config_value) KEY(id) VALUES(5, 'data_source.url', 'https://github.com/yidafu/example-blog.git');
MERGE INTO B_CONFIGURATION (id, config_key, config_value) KEY(id) VALUES(6, 'data_source.token', '');
MERGE INTO B_CONFIGURATION (id, config_key, config_value) KEY(id) VALUES(7, 'data_source.branch', '');
MERGE INTO B_CONFIGURATION (id, config_key, config_value) KEY(id) VALUES(8, 'data_source.feishu_app_id', '');
MERGE INTO B_CONFIGURATION (id, config_key, config_value) KEY(id) VALUES(9, 'data_source.feishu_app_secret', '');

-- admin user  admin, password: admin123
MERGE INTO B_USER (id, username, password, email, status) KEY(id) VALUES (1, 'admin', 'AZICOnu9cyUFFvBp3xi1AA==', '', 1);

ALTER TABLE B_SYNC_TASK ADD COLUMN IF NOT EXISTS FORCE_SYNC INTEGER DEFAULT 0;
