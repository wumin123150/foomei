/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/10/15 13:50:36                          */
/*==============================================================*/


drop table if exists core_annex;

drop table if exists core_area;

drop table if exists core_config;

drop table if exists core_data_dictionary;

drop table if exists core_data_type;

drop table if exists core_group_role;

drop table if exists core_log;

drop table if exists core_membership;

drop table if exists core_message;

drop table if exists core_message_content;

drop table if exists core_permission;

drop table if exists core_relationship;

drop table if exists core_role;

drop table if exists core_role_permission;

drop table if exists core_token;

drop table if exists core_user;

drop table if exists core_user_group;

drop table if exists core_user_role;

/*==============================================================*/
/* Table: core_annex                                            */
/*==============================================================*/
create table core_annex
(
   id                   varchar(36) not null comment '编号',
   object_id            varchar(36) comment '对象ID',
   object_type          varchar(32) comment '对象类型',
   path                 varchar(512) comment '存储路径',
   name                 varchar(128) comment '文件名称',
   type                 varchar(16) comment '文件类型',
   create_time          datetime comment '创建时间',
   creator              bigint comment '创建人',
   primary key (id)
);

alter table core_annex comment '附件';

/*==============================================================*/
/* Table: core_area                                             */
/*==============================================================*/
create table core_area
(
   id                   varchar(36) not null comment '编号',
   code                 varchar(8) comment '代码',
   name                 varchar(16) comment '名称',
   full_name            varchar(32) comment '全称',
   grade                tinyint(1) comment '层级',
   type                 varchar(1) comment '类型(1:省,2:直辖市,3:地级市,4:县级市,5:县,6:区)',
   root_id              varchar(36) comment '根ID',
   parent_id            varchar(36) comment '父ID',
   primary key (id)
);

alter table core_area comment '地区';

/*==============================================================*/
/* Table: core_config                                           */
/*==============================================================*/
create table core_config
(
   id                   bigint not null auto_increment comment '编号',
   code                 varchar(128) comment '键',
   value                text comment '值',
   name                 varchar(64) comment '名称',
   type                 tinyint(1) comment '类型(0:Input输入框,1:Textarea文本框,2:Radio单选框,3:Checkbox多选框,4:Select单选框,5:Select多选框)',
   params               text comment '参数',
   editable             tinyint(1) comment '是否可修改 (0:不可修改,1:可修改)',
   remark               varchar(128) comment '备注',
   primary key (id)
);

alter table core_config comment '系统配置';

/*==============================================================*/
/* Table: core_data_dictionary                                  */
/*==============================================================*/
create table core_data_dictionary
(
   id                   bigint not null auto_increment comment '编号',
   type_id              bigint comment '类型',
   code                 varchar(64) comment '代码',
   name                 varchar(64) comment '名称',
   priority             int comment '序号',
   grade                smallint(6) comment '层级',
   parent_id            bigint comment '父ID',
   remark               varchar(128) comment '备注',
   primary key (id)
);

alter table core_data_dictionary comment '数据字典';

/*==============================================================*/
/* Table: core_data_type                                        */
/*==============================================================*/
create table core_data_type
(
   id                   bigint not null auto_increment comment '编号',
   code                 varchar(64) comment '代码',
   name                 varchar(64) comment '名称',
   grade                smallint(6) comment '层级',
   editable             tinyint(1) comment '是否可修改 (0:不可修改,1:可修改)',
   remark               varchar(128) comment '备注',
   primary key (id)
);

alter table core_data_type comment '数据类型';

/*==============================================================*/
/* Table: core_group_role                                       */
/*==============================================================*/
create table core_group_role
(
   group_id             bigint not null,
   role_id              bigint not null,
   primary key (role_id, group_id)
);

alter table core_group_role comment '用户组和角色';

/*==============================================================*/
/* Table: core_log                                              */
/*==============================================================*/
create table core_log
(
   id                   varchar(36) not null comment '编号',
   description          varchar(128) comment '操作描述',
   username             varchar(64) comment '操作用户',
   log_time             datetime comment '操作时间',
   spend_time           int comment '消耗时间',
   ip                   varchar(16) comment 'IP地址',
   url                  varchar(256) comment 'URL',
   method               varchar(8) comment '请求类型',
   user_agent           varchar(256) comment '用户标识',
   parameter            text comment '请求参数',
   result               text comment '响应结果',
   primary key (id)
);

alter table core_log comment '操作日志';

/*==============================================================*/
/* Table: core_membership                                       */
/*==============================================================*/
create table core_membership
(
   user_id              bigint not null,
   group_id             bigint not null,
   primary key (user_id, group_id)
);

alter table core_membership comment '用户和用户组';

/*==============================================================*/
/* Table: core_message                                          */
/*==============================================================*/
create table core_message
(
   id                   varchar(36) not null comment '编号',
   text_id              varchar(36) comment '内容ID',
   receiver             bigint comment '接收人',
   send_status          tinyint(1) comment '发送状态(0:发送中,1:已发送,2:发送失败)',
   send_time            datetime comment '发送时间',
   read_status          tinyint(1) comment '阅读状态(0:未读,1:已读)',
   read_time            datetime comment '阅读时间',
   primary key (id)
);

alter table core_message comment '消息';

/*==============================================================*/
/* Table: core_message_text                                     */
/*==============================================================*/
create table core_message_text
(
   id                   varchar(36) not null comment '编号',
   content              text comment '内容',
   sender               bigint comment '发送人',
   create_time          datetime comment '创建时间',
   creator              bigint comment '创建人',
   primary key (id)
);

alter table core_message_text comment '消息内容';

/*==============================================================*/
/* Table: core_permission                                       */
/*==============================================================*/
create table core_permission
(
   id                   bigint not null auto_increment comment '编号',
   code                 varchar(64) not null comment '代码',
   name                 varchar(64) comment '名称',
   priority             int comment '序号',
   primary key (id)
);

alter table core_permission comment '权限';

/*==============================================================*/
/* Table: core_relationship                                     */
/*==============================================================*/
create table core_relationship
(
   user_id              bigint not null comment '用户ID',
   group_id             bigint not null comment '用户组ID',
   duty                 varchar(64) comment '职务',
   relation             tinyint(1) comment '主从关系',
   primary key (user_id, group_id)
);

/*==============================================================*/
/* Table: core_role                                             */
/*==============================================================*/
create table core_role
(
   id                   bigint not null auto_increment comment '编号',
   code                 varchar(64) not null comment '代码',
   name                 varchar(64) comment '名称',
   primary key (id)
);

alter table core_role comment '角色';

/*==============================================================*/
/* Table: core_role_permission                                  */
/*==============================================================*/
create table core_role_permission
(
   role_id              bigint not null,
   permission_id        bigint not null,
   primary key (role_id, permission_id)
);

/*==============================================================*/
/* Table: core_token                                            */
/*==============================================================*/
create table core_token
(
   id                   varchar(36) not null comment '编号',
   user_id              bigint comment '用户ID',
   expire_time          datetime comment '过期时间',
   terminal             varchar(16) comment '终端',
   remark               varchar(128) comment '备注',
   status               tinyint(1) comment '状态(0:有效,1:失效)',
   create_time          datetime comment '创建时间',
   primary key (id)
);

alter table core_token comment '访问令牌';

/*==============================================================*/
/* Table: core_user                                             */
/*==============================================================*/
create table core_user
(
   id                   bigint not null auto_increment comment '编号',
   login_name           varchar(64) not null comment '账号',
   password             varchar(255) comment '密码',
   salt                 varchar(64) comment '散列',
   name                 varchar(64) comment '姓名',
   question             varchar(255) comment '问题',
   answer               varchar(255) comment '答案',
   sex                  tinyint(1) comment '性别(0:未知,1:男,2:女)',
   birthday             date comment '出生日期',
   mobile               varchar(16) comment '手机',
   email                varchar(32) comment '邮箱',
   avatar               varchar(255) comment '头像',
   open_id              varchar(32) comment '微信身份ID',
   register_time        datetime comment '注册时间',
   register_ip          varchar(16) comment '注册IP',
   last_login_time      datetime comment '最后登录时间',
   last_login_ip        varchar(16) comment '最后登录IP',
   login_count          int comment '登录次数',
   status               varchar(1) comment '状态(I:未激活,A:正常,E:过期,L:锁定,T:终止)',
   primary key (id)
);

alter table core_user comment '系统用户';

/*==============================================================*/
/* Index: idx_user_name                                         */
/*==============================================================*/
create unique index idx_user_name on core_user
(
   login_name
);

/*==============================================================*/
/* Table: core_user_group                                       */
/*==============================================================*/
create table core_user_group
(
   ID                   bigint not null auto_increment comment '编号',
   code                 varchar(64) comment '代码',
   name                 varchar(64) comment '名称',
   type                 tinyint(1) comment '类型(0:公司,1:部门,2:小组,3:其他)',
   grade                tinyint(1) comment '层级',
   director             bigint comment '负责人',
   path                 varchar(512) comment '路径',
   parent_id            bigint comment '父ID',
   remark               varchar(128) comment '备注',
   del_flag             tinyint(1) comment '删除标志(0:正常,1:停用)',
   primary key (ID)
);

alter table core_user_group comment '组织机构';

/*==============================================================*/
/* Table: core_user_role                                        */
/*==============================================================*/
create table core_user_role
(
   user_id              bigint not null,
   role_id              bigint not null,
   primary key (user_id, role_id)
);

alter table core_user_role comment '用户和角色';
