DELETE * from `nice-blog`.b_configuration
INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(1, 'appearance.site_title', 'Dov Yih');

INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(2, 'appearance.github_url', 'https://github.com/yidafu');

INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(3, 'synchronous.cron_expr', '0 0 1 1/1 * ?')

INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(4, 'data_source.type', 'git')
INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(5, 'data_source.url', 'https://github.com/yidafu/simple-blog')
INSERT INTO `nice-blog`.b_configuration (id, configKey, configValue) VALUES(6, 'data_source.token', '')
