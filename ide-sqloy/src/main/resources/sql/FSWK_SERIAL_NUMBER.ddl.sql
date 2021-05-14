/*==============================================================*/
/* Table: FOWK_SERIAL_NUMBER                                    */
/*==============================================================*/
create table FSWK_SERIAL_NUMBER
(
  ID                   varchar(150) not null comment '标识号',
  CURSOR_VALUE         bigint comment '当前值',
  TEMPLATE             varchar(180) comment '字串模板',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (ID)
);

alter table FSWK_SERIAL_NUMBER comment '流水号记录表';