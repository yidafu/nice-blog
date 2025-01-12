
INSERT OR IGNORE INTO B_CONFIGURATION (id, config_key, config_value) VALUES(1, 'appearance.site_title', 'Dov Yih');

INSERT OR IGNORE INTO B_CONFIGURATION (id, config_key, config_value) VALUES(2, 'appearance.github_url', 'https://github.com/yidafu');
-- 测试每分钟执行 0 0/1 * * * ?
-- 正式每天凌晨1点执行 0 0 1 1/1 * ?
INSERT OR IGNORE INTO B_CONFIGURATION (id, config_key, config_value) VALUES(3, 'synchronous.cron_expr', '0 0/1 * * * ?');

INSERT OR IGNORE INTO B_CONFIGURATION (id, config_key, config_value) VALUES(4, 'data_source.type', 'git');
INSERT OR IGNORE INTO B_CONFIGURATION (id, config_key, config_value) VALUES(5, 'data_source.url', 'https://github.com/yidafu/example-blog.git');
INSERT OR IGNORE INTO B_CONFIGURATION (id, config_key, config_value) VALUES(6, 'data_source.token', '');

-- admin user  admin, password: admin123
INSERT OR IGNORE INTO B_USER (id, username, password, email, status) VALUES (1, "admin", "AZICOnu9cyUFFvBp3xi1AA==","", 1);
