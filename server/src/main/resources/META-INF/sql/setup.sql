DELETE from `nice-blog`.b_configuration
INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(1, 'appearance.site_title', 'Dov Yih');

INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(2, 'appearance.github_url', 'https://github.com/yidafu');
-- 测试每分钟执行 0 0/1 * * * ?
-- 正式每天凌晨1点执行 0 0 1 1/1 * ?
INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(3, 'synchronous.cron_expr', '0 0/1 * * * ?')

INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(4, 'data_source.type', 'git')
INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(5, 'data_source.url', 'https://github.com/yidafu/example-blog.git')
INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(6, 'data_source.token', '')

-- admin user  admin, password: admin123
INSERT INTO `nice-blog`.b_user (id, username, password, email, status) value (1, "admin", "AZICOnu9cyUFFvBp3xi1AA==","", 1)
