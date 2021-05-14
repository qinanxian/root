drop table if exists AUTH_AUTHORIZE;

drop table if exists AUTH_ORG;

drop table if exists AUTH_ORG_PROPERTY;

drop table if exists AUTH_PERMIT;

drop table if exists AUTH_ROLE;

drop table if exists AUTH_ROLE_PERMIT;

drop table if exists AUTH_USER;

drop table if exists AUTH_USER_ACCOUNT;

drop table if exists AUTH_USER_BEHAVIOR;

drop table if exists AUTH_USER_PERMIT;

drop table if exists AUTH_USER_PROPERTY;

drop table if exists AUTH_USER_ROLE;

drop table if exists AUTH_USER_SESSION;

/*==============================================================*/
/* Table: AUTH_AUTHORIZE                                        */
/*==============================================================*/
create table AUTH_AUTHORIZE
(
  AUTHORIZE_ID         varchar(32) not null comment '授权ID',
  AUTHORIZER           varchar(32) comment '授权人',
  ASSIGNEE             varchar(32) comment '被授权人',
  BEGIN_DATE           datetime comment '授权生效日',
  END_DATE             datetime comment '授权到期日',
  STATUS               varchar(32) comment '授权状态',
  OBJECT_TYPE          varchar(32) comment '授权对象类型',
  OBJECT_ID            varchar(32) comment '授权对象ID',
  AUTHORIZE_TYPE       varchar(32) comment '授权类型',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_AUTHORIZE comment '用户操作授权';

alter table AUTH_AUTHORIZE
  add primary key (AUTHORIZE_ID);

/*==============================================================*/
/* Table: AUTH_ORG                                              */
/*==============================================================*/
create table AUTH_ORG
(
  ID                   varchar(32) not null comment '机构ID',
  CODE                 varchar(32) not null comment '机构代号',
  NAME                 varchar(150) comment '机构名称',
  FULL_NAME            varchar(900) comment '部门路径全称',
  SORT_CODE            varchar(120) comment '排序代码',
  PARENT_ID            varchar(32) comment '上级机构',
  LEVEL                varchar(120) comment '级别',
  ORG_TYPE             varchar(32) comment '机构类型',
  LEADER               varchar(180) comment '负责人',
  REMARK               varchar(900) comment '说明',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_ORG comment '部门机构';

alter table AUTH_ORG
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_ORG_PROPERTY                                     */
/*==============================================================*/
create table AUTH_ORG_PROPERTY
(
  ID                   varchar(32) not null comment '属性ID',
  ORG_ID               varchar(32) not null comment '机构ID',
  NAME                 varchar(32) not null comment '属性名',
  VALUE                varchar(900) comment '属性值',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_ORG_PROPERTY comment '机构属性';

alter table AUTH_ORG_PROPERTY
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_PERMIT                                           */
/*==============================================================*/
create table AUTH_PERMIT
(
  ID                   varchar(32) not null comment '权限ID',
  CODE                 varchar(180) not null comment '权限代号',
  NAME                 varchar(150) comment '权限名称',
  SUMMARY              varchar(900) comment '权限说明',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_PERMIT comment '权限信息';

alter table AUTH_PERMIT
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_ROLE                                             */
/*==============================================================*/
create table AUTH_ROLE
(
  ID                   varchar(32) not null comment '角色ID',
  CODE                 varchar(32) not null comment '角色代号',
  NAME                 varchar(150) comment '角色名',
  TYPE                 varchar(32) comment '角色类型',
  STATUS               varchar(32) comment '角色状态',
  SUMMARY              varchar(900) comment '角色摘要描述',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_ROLE comment '角色信息';

alter table AUTH_ROLE
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_ROLE_PERMIT                                      */
/*==============================================================*/
create table AUTH_ROLE_PERMIT
(
  ID                   varchar(32) not null comment '角色权限ID',
  ROLE_ID              varchar(32) comment '角色ID',
  PERMIT_ID            varchar(32) comment '权限ID',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_ROLE_PERMIT comment '角色权限';

alter table AUTH_ROLE_PERMIT
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_USER                                             */
/*==============================================================*/
create table AUTH_USER
(
  ID                   varchar(32) not null comment '用户ID',
  CODE                 varchar(32) not null comment '用户代号',
  NAME                 varchar(150) comment '用户名',
  PASSWORD             varchar(120) comment '密码',
  AVATAR               varchar(180) comment '头像',
  NICK                 varchar(150) comment '昵称',
  ORG_ID               varchar(32) comment '机构ID',
  EMAIL                varchar(120) comment '电子邮件',
  PHONE                varchar(120) comment '移动电话',
  STATUS               varchar(32) comment '用户状态',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_USER comment '用户信息';

alter table AUTH_USER
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_USER_ACCOUNT                                     */
/*==============================================================*/
create table AUTH_USER_ACCOUNT
(
  ID                   varchar(32) not null comment '账号ID',
  USER_ID              varchar(32) comment '用户ID',
  ACCOUNT_CODE         varchar(32) not null comment '账户代号',
  PASSWORD             varchar(120) comment '账号密码',
  ACCOUNT_TYPE         varchar(32) comment '账号类型',
  STATUS               varchar(32) comment '账号状态',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_USER_ACCOUNT comment '用户账号';

alter table AUTH_USER_ACCOUNT
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_USER_BEHAVIOR                                    */
/*==============================================================*/
create table AUTH_USER_BEHAVIOR
(
  ID                   varchar(32) not null comment '记录流水号',
  USER_ID              varchar(32) comment '用户ID',
  OBJECT_TYPE          varchar(32) comment '关联对象类型',
  OBJECT_ID            varchar(32) comment '关联对象号',
  TYPE                 varchar(32) comment '行为类型',
  VALUE                varchar(180) comment '行为数值',
  INTRO                varchar(900) comment '行为说明',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_USER_BEHAVIOR comment '用户行为';

alter table AUTH_USER_BEHAVIOR
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_USER_PERMIT                                      */
/*==============================================================*/
create table AUTH_USER_PERMIT
(
  ID                   varchar(32) not null comment '用户权限ID',
  USER_ID              varchar(32) not null comment '用户ID',
  PERMIT_ID            varchar(32) comment '权限ID',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_USER_PERMIT comment '用户直接权限';

alter table AUTH_USER_PERMIT
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_USER_PROPERTY                                    */
/*==============================================================*/
create table AUTH_USER_PROPERTY
(
  ID                   varchar(32) not null comment '属性ID',
  USER_ID              varchar(32) not null comment '用户ID',
  NAME                 varchar(32) not null comment '属性名',
  VALUE                varchar(900) comment '属性值',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_USER_PROPERTY comment '用户属性';

alter table AUTH_USER_PROPERTY
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_USER_ROLE                                        */
/*==============================================================*/
create table AUTH_USER_ROLE
(
  ID                   varchar(32) not null comment '职责ID',
  USER_ID              varchar(32) not null comment '用户ID',
  ROLE_ID              varchar(32) comment '角色ID',
  ORG_ID               varchar(32) comment '机构ID',
  POSITION_TYPE        varchar(32) comment '岗位类型',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_USER_ROLE comment '用户角色职责';

alter table AUTH_USER_ROLE
  add primary key (ID);

/*==============================================================*/
/* Table: AUTH_USER_SESSION                                     */
/*==============================================================*/
create table AUTH_USER_SESSION
(
  ID                   varchar(32) not null comment '记录流水号',
  USER_ID              varchar(32) not null comment '用户ID',
  SESSION_ID           varchar(120) not null comment '会话ID',
  SESSION_STATUS       varchar(32) comment '会话状态',
  START_TIME           datetime comment '会话开始时间',
  END_TIME             datetime comment '会话结束时间',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间'
);

alter table AUTH_USER_SESSION comment '用户登录会话';

alter table AUTH_USER_SESSION
  add primary key (ID);
