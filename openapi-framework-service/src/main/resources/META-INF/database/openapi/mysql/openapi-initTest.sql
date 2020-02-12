insert into `api_partner` (`id`, `partner_id`, `partner_name`, `merchant_no`, `tenant_no`, `create_time`, `update_time`, `comments`)
values ( 1,'test', '测试帐号', 'test', 'test', '2018-08-21 15:36:56', '2018-08-21 15:36:56', '');
insert into `api_auth` ( `id`,`auth_no`,`partner_id`,`secret_type`,`sign_type`,`access_key`, `secret_key`, `permissions`, `create_time`, `update_time`, `comments`)
values ( 1, 'O200212004846016E0007', 'test', 'digest', 'MD5','test', '06f7aab08aa2431e6dae6a156fc9e0b4', '*:*', '2018-08-21 15:38:54', '2018-08-21 15:38:54', '');