drop table if exists CONF_FNASTAT;

/*==============================================================*/
/* Table: CONF_FNASTAT                                          */
/*==============================================================*/
create table CONF_FNASTAT
(
   FNASTAT_DEF_ID       VARCHAR(32) not null comment '报表定义号',
   FNASTAT_UNIFIED_CODE VARCHAR(120) comment '报表统一识别码',
   FNASTAT_DEF_NAME     VARCHAR(150) comment '报表名称',
   FNASTAT_TYPE         VARCHAR(32) comment '报表类别',
   FNASTAT_CLASS        VARCHAR(32) comment '报表分类,代码表：FinstatClass',
   FNASTAT_DESCRIBE     VARCHAR(900) comment '报表说明',
   FNASTAT_COLUMNS      VARCHAR(3000) comment '数据列',
   FNASTAT_COLUMN_STYLES VARCHAR(3000) comment '数据列样式',
   REMARK               VARCHAR(900) comment '备注说明',
   CREATED_BY           VARCHAR(32) comment '创建人',
   CREATED_TIME         datetime comment '创建时间',
   UPDATED_BY           VARCHAR(32) comment '更新人',
   UPDATED_TIME         datetime comment '更新时间',
   primary key (FNASTAT_DEF_ID)
);

alter table CONF_FNASTAT comment '财务报表模型目录';




drop table if exists CONF_FNASTAT_SUBJECT;

/*==============================================================*/
/* Table: CONF_FNASTAT_SUBJECT                                  */
/*==============================================================*/
create table CONF_FNASTAT_SUBJECT
(
   FNASTAT_DEF_ID       VARCHAR(32) not null comment '报表定义号',
   SUBJECT_ID           VARCHAR(32) not null comment '科目号',
   SUBJECT_NAME         VARCHAR(150) comment '科目名',
   SUBJECT_UNIFIED_CODE VARCHAR(120) comment '科目统一识别码',
   DISPLAY_ORDER        VARCHAR(30) comment '显示顺序号',
   SUBJECT_STYLE        VARCHAR(900) comment '样式',
   VALUE_1_EXPRESSION   VARCHAR(800) comment '值1的表达式',
   VALUE_2_EXPRESSION   VARCHAR(800) comment '值2的表达式',
   VALUE_3_EXPRESSION   VARCHAR(800) comment '值3的表达式',
   VALUE_4_EXPRESSION   VARCHAR(800) comment '值4的表达式',
   VALUE_5_EXPRESSION   VARCHAR(800) comment '值5的表达式',
   VALUE_6_EXPRESSION   VARCHAR(800) comment '值6的表达式',
   VALUE_7_EXPRESSION   VARCHAR(800) comment '值7的表达式',
   VALUE_8_EXPRESSION   VARCHAR(800) comment '值8的表达式',
   VALUE_9_EXPRESSION   VARCHAR(800) comment '值9的表达式',
   VALUE_10_EXPRESSION  VARCHAR(800) comment '值10的表达式',
   VALUE_11_EXPRESSION  VARCHAR(800) comment '值11的表达式',
   VALUE_12_EXPRESSION  VARCHAR(800) comment '值12的表达式',
   VALUE_13_EXPRESSION  VARCHAR(800) comment '值13的表达式',
   VALUE_14_EXPRESSION  VARCHAR(800) comment '值14的表达式',
   VALUE_15_EXPRESSION  VARCHAR(800) comment '值15的表达式',
   VALUE_16_EXPRESSION  VARCHAR(800) comment '值16的表达式',
   VALUE_17_EXPRESSION  VARCHAR(800) comment '值17的表达式',
   VALUE_18_EXPRESSION  VARCHAR(800) comment '值18的表达式',
   CREATED_BY           VARCHAR(32) comment '创建人',
   CREATED_TIME         datetime comment '创建时间',
   UPDATED_BY           VARCHAR(32) comment '更新人',
   UPDATED_TIME         datetime comment '更新时间',
   primary key (FNASTAT_DEF_ID, SUBJECT_ID)
);

alter table CONF_FNASTAT_SUBJECT comment '财务报表科目明细';
