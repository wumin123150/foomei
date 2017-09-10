/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/7/12 16:45:24                           */
/*==============================================================*/


drop table if exists core_annex;

drop table if exists core_area;

drop table if exists core_config;

drop table if exists core_data_dictionary;

drop table if exists core_data_type;

drop table if exists core_group_role;

drop table if exists core_log;

drop table if exists core_membership;

drop table if exists core_permission;

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
   id                   varchar(36) not null,
   object_id            varchar(36),
   object_type          varchar(32),
   path                 varchar(512),
   name                 varchar(128),
   type                 varchar(16),
   create_time          datetime,
   creator              bigint,
   primary key (id)
);

/*==============================================================*/
/* Table: core_area                                             */
/*==============================================================*/
create table core_area
(
   id                   varchar(36) not null,
   code                 varchar(8),
   name                 varchar(16),
   full_name            varchar(32),
   level                tinyint(1),
   type                 varchar(1),
   root_id              varchar(36),
   parent_id            varchar(36),
   map_code             varchar(32),
   weather_code         varchar(32),
   primary key (id)
);

/*==============================================================*/
/* Table: core_config                                           */
/*==============================================================*/
create table core_config
(
   id                   bigint not null auto_increment,
   code                 varchar(128),
   value                text,
   name                 varchar(64),
   type									tinyint(1),
   params								text,
   editable             tinyint(1),
   remark               varchar(128),
   primary key (id)
);

/*==============================================================*/
/* Table: core_data_dictionary                                  */
/*==============================================================*/
create table core_data_dictionary
(
   id                   bigint not null auto_increment,
   type_id              bigint,
   code                 varchar(64),
   name                 varchar(64),
   priority             int,
   level                smallint(6),
   is_item              tinyint(1),
   parent_id            bigint,
   remark               varchar(128),
   primary key (id)
);

/*==============================================================*/
/* Table: core_data_type                                        */
/*==============================================================*/
create table core_data_type
(
   id                   bigint not null auto_increment,
   code                 varchar(64),
   name                 varchar(64),
   level                smallint(6),
   editable             tinyint(1),
   remark               varchar(128),
   primary key (id)
);

/*==============================================================*/
/* Table: core_group_role                                       */
/*==============================================================*/
create table core_group_role
(
   group_id             bigint not null,
   role_id              bigint not null,
   primary key (role_id, group_id)
);

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

/*==============================================================*/
/* Table: core_permission                                       */
/*==============================================================*/
create table core_permission
(
   id                   bigint not null auto_increment,
   code                 varchar(64) not null,
   name                 varchar(64),
   priority             int,
   primary key (id)
);

/*==============================================================*/
/* Table: core_role                                             */
/*==============================================================*/
create table core_role
(
   id                   bigint not null auto_increment,
   code                 varchar(64) not null,
   name                 varchar(64),
   primary key (id)
);

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
   id                   varchar(36) not null,
   user_id              bigint,
   expire_time          datetime,
   terminal             varchar(16),
   remark               varchar(128),
   status               tinyint(1),
   create_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: core_user                                             */
/*==============================================================*/
create table core_user
(
   id                   bigint not null auto_increment,
   login_name           varchar(64) not null,
   password             varchar(255),
   salt                 varchar(64) comment '防止密码攻击，暂不使用',
   name                 varchar(64),
   question             varchar(255) comment '太多网站的需要问题找回密码，用户难以记住特定的答案，通过问题找回密码的方式不再适应潮流',
   answer               varchar(255) comment '暂不使用',
   mobile               varchar(16),
   email                varchar(32),
   avatar               varchar(255),
   open_id              varchar(32),
   register_time        datetime,
   register_ip          varchar(16),
   last_login_time      datetime,
   last_login_ip        varchar(16),
   login_count          int,
   status               varchar(1),
   primary key (id)
);

/*==============================================================*/
/* Table: core_user_group                                       */
/*==============================================================*/
create table core_user_group
(
   ID                   bigint not null auto_increment,
   code                 varchar(64),
   name                 varchar(64),
   type                 tinyint(1) comment '0：公司；1：部门',
   level                int,
   path                 varchar(512),
   parent_id            bigint,
   remark               varchar(128),
   del_flag             tinyint(1) comment '0：正常；1：停用',
   primary key (ID)
);

/*==============================================================*/
/* Table: core_user_role                                        */
/*==============================================================*/
create table core_user_role
(
   user_id              bigint not null,
   role_id              bigint not null,
   primary key (user_id, role_id)
);
