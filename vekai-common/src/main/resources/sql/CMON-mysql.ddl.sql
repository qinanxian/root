drop table if exists CMON_FILE;

drop table if exists CMON_FILE_HIST;

/*==============================================================*/
/* Table: CMON_FILE                                             */
/*==============================================================*/
create table CMON_FILE
(
  FILE_ID              varchar(32) not null comment '文件标识号',
  NAME                 varchar(150) comment '文件名称',
  VERSION_CODE         varchar(30) comment '版本代号',
  INTRO                varchar(900) comment '文件描述',
  MIME_TYPE            varchar(180) comment 'MIME类型',
  CONTENT_TYE          varchar(180) comment '文件ContentType',
  SIZE                 bigint comment '文件大小',
  FILE_TYPE            varchar(32) comment '文件类型',
  BAR_CODE             varchar(180) comment '条形码',
  QR_CODE              varchar(180) comment '二维码',
  KEY_WORDS            varchar(180) comment '关键字',
  STORED_CONTENT       varchar(900) comment '文件存储内容,文件URL或地址或其他存储相关的ID',
  PUBLIC_KEY           varchar(180) comment '加密公钥',
  PRIVATE_KEY          varchar(180) comment '加密私钥',
  SECRET_KEY           varchar(180) comment '加密密钥',
  SIGNATURE            varchar(180) comment '签名值',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (FILE_ID)
);

/*==============================================================*/
/* Table: CMON_FILE_HIST                                        */
/*==============================================================*/
create table CMON_FILE_HIST
(
  FILE_HIST_ID         varchar(32) not null comment '文件历史标识号',
  FILE_ID              varchar(32) not null comment '文件标识号',
  NAME                 varchar(150) comment '文件名称',
  VERSION_CODE         varchar(30) comment '版本代号',
  INTRO                varchar(900) comment '文件描述',
  MIME_TYPE            varchar(180) comment 'MIME类型',
  CONTENT_TYE          varchar(180) comment '文件ContentType',
  SIZE                 bigint comment '文件大小',
  FILE_TYPE            varchar(32) comment '文件类型',
  BAR_CODE             varchar(180) comment '条形码',
  QR_CODE              varchar(180) comment '二维码',
  KEY_WORDS            varchar(180) comment '关键字',
  STORED_CONTENT       varchar(900) comment '文件存储内容,文件URL或地址或其他存储相关的ID',
  PUBLIC_KEY           varchar(180) comment '加密公钥',
  PRIVATE_KEY          varchar(180) comment '加密私钥',
  SECRET_KEY           varchar(180) comment '加密密钥',
  SIGNATURE            varchar(180) comment '签名值',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (FILE_HIST_ID)
);
