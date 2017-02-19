create sequence seq_api_order_info
increment by 1
  start with 1
  maxvalue 99999999
  nocache;

create sequence seq_api_notify_message
increment by 1
  start with 1
  maxvalue 99999999
  nocache;


/*==============================================================*/
/* Table: api_order_info                                      */
/*==============================================================*/
create table api_order_info
(
   id                 NUMBER               not null,
   gid                VARCHAR2(40)         not null,
   request_no         VARCHAR2(40)         not null,
   partner_id         VARCHAR2(40)         not null,
   order_no           VARCHAR2(40)         default NULL,
   oid                VARCHAR2(40)         default NULL,
   service            VARCHAR2(32)         not null,
   version            VARCHAR2(8)          not null,
   sign_type          VARCHAR2(16)         default NULL,
   charset            VARCHAR2(16)         default NULL,
   protocol           VARCHAR2(40)         default NULL,
   notify_url         VARCHAR2(256)        default NULL,
   return_url         VARCHAR2(256)        default NULL,
   business_info      VARCHAR2(1024)       default NULL,
   context            VARCHAR2(128)        default NULL,
   raw_add_time       DATE                 default NULL,
   raw_update_time    DATE                 default NULL,
   constraint UK_PARTNER_REQUEST unique (partner_id, request_no)
);

comment on table api_order_info is
'订单信息表';

comment on column api_order_info.id is
'ID';

comment on column api_order_info.gid is
'统一流水';

comment on column api_order_info.request_no is
'请求号';

comment on column api_order_info.partner_id is
'商户ID';

comment on column api_order_info.order_no is
'订单号';

comment on column api_order_info.oid is
'内部订单号';

comment on column api_order_info.service is
'服务名';

comment on column api_order_info.version is
'版本号';

comment on column api_order_info.sign_type is
'签名类型';

comment on column api_order_info.charset is
'请求编码';

comment on column api_order_info.protocol is
'协议';

comment on column api_order_info.notify_url is
'通知地址';

comment on column api_order_info.return_url is
'返回地址';

comment on column api_order_info.business_info is
'扩展信息';

comment on column api_order_info.context is
'会话信息';

comment on column api_order_info.raw_add_time is
'创建时间';

comment on column api_order_info.raw_update_time is
'更新时间';

/*==============================================================*/
/* Index: IDX_ORDER_GID                                         */
/*==============================================================*/
create index IDX_ORDER_GID on api_order_info (
   gid ASC
);

/*==============================================================*/
/* Index: IDX_PARTNER_ORDER                                     */
/*==============================================================*/
create index IDX_PARTNER_ORDER on api_order_info (
   partner_id ASC,
   order_no ASC,
   service ASC,
   version ASC
);

/*==============================================================*/
/* Index: IDX_ADD_TIME                                          */
/*==============================================================*/
create index IDX_ADD_TIME on api_order_info (
   raw_add_time ASC
);


/*==============================================================*/
/* Table: api_notify_message                                  */
/*==============================================================*/
create table api_notify_message
(
   id                 NUMBER               not null,
   gid                VARCHAR2(40)         default NULL,
   partner_id         VARCHAR2(40)         not null,
   request_no         VARCHAR2(40)         default null,
   merch_order_no     VARCHAR2(40)         default null,
   message_type       VARCHAR2(16)         not null,
   service            VARCHAR2(32)         default NULL,
   version            VARCHAR2(8)          default NULL,
   url                VARCHAR2(255)        not null,
   content            VARCHAR2(1024)       not null,
   resp_info          VARCHAR2(128)        default null,
   send_count         INTEGER              default 0 not null,
   next_send_time     DATE                 default NULL,
   status             VARCHAR2(16)         not null,
   execute_status     VARCHAR2(16)         not null,
   create_time        DATE                 default NULL,
   update_time        DATE                 default NULL
);

comment on table api_notify_message is
'异步通知消息';

/*==============================================================*/
/* Index: IDX_NOTIFY_STATUS                                     */
/*==============================================================*/
create index IDX_NOTIFY_STATUS on api_notify_message (
   status ASC,
   execute_status ASC
);

/*==============================================================*/
/* Index: IDX_NOTIFY_QUERY                                      */
/*==============================================================*/
create index IDX_NOTIFY_QUERY on api_notify_message (
   gid ASC,
   partner_id ASC
);