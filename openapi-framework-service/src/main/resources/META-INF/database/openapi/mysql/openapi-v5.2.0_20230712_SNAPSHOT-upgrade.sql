INSERT INTO `sys_resource` (`id`, `parentid`, `name`, `type`, `show_state`, `order_time`, `value`, `show_mode`, `icon`, `descn`, `create_time`, `update_time`)
VALUES
    (201808211422, 2018082114, '动态秘钥', 'URL', 0, '2019-02-03 00:26:30', '/manage/openapi/apiAuth/dynamic.html', 1, 'fa-circle-o', NULL, '2023-07-10 16:23:54', '2023-07-10 16:34:31');

INSERT INTO `sys_role_resc` (`role_id`, `resc_id`)
VALUES
    (1, 201808211422);
