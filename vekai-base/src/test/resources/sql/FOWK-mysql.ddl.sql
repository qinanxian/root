drop table if exists FOWK_DATAFORM;

drop table if exists FOWK_DATAFORM_ELEMENT;

drop table if exists FOWK_DATAFORM_FILTER;

drop table if exists FOWK_DATAFORM_VALIDATOR;

drop table if exists FOWK_DICT;

drop table if exists FOWK_DICT_ITEM;

drop table if exists FOWK_MENU;

drop table if exists FOWK_PARAM;

drop table if exists FOWK_PARAM_ITEM;

drop table if exists FOWK_SERIAL_NUMBER;

/*==============================================================*/
/* Table: FOWK_DATAFORM                                         */
/*==============================================================*/
create table FOWK_DATAFORM
(
  ID                   varchar(180) not null comment '模板标识',
  PACK                 varchar(180) not null comment '所在包',
  CODE                 varchar(120) comment '模板代码',
  NAME                 varchar(150) comment '模板名称',
  NAME_I18N_CODE       varchar(180) comment '模板名称国际化代码',
  DESCRIPTION          varchar(900) comment '模板描述',
  SORT_CODE            varchar(30) comment '排序码',
  CLASSIFICATION       varchar(180) comment '模板分类代码',
  TAGS                 varchar(180) comment '模板标签',
  DATA_MODEL_TYPE      varchar(120) comment '数据模型类别',
  DATA_MODEL           varchar(120) comment '数据容器',
  SQL_SELECT           varchar(120) comment 'SELECT',
  SQL_FROM             varchar(900) comment 'FROM',
  SQL_WHERE            varchar(900) comment 'WHERE',
  SQL_GROUP            varchar(900) comment 'GROUP',
  SQL_HAVING           varchar(900) comment 'HAVING',
  SQL_ORDER            varchar(900) comment 'ORDER',
  HANDLER              varchar(180) comment '数据处理器',
  COLUMN_NUMBER        int comment '总列数',
  FORM_STYLE           varchar(180) comment '页面组件类型',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (ID)
);

alter table FOWK_DATAFORM comment '显示模板';

/*==============================================================*/
/* Table: FOWK_DATAFORM_ELEMENT                                 */
/*==============================================================*/
create table FOWK_DATAFORM_ELEMENT
(
  DATAFORM_ID          varchar(180) not null comment '模板标识',
  CODE                 varchar(120) not null comment '元素标识码',
  PRIMARY_KEY          varchar(1) comment '是否为主键',
  PRIMARY_KEY_GENERATOR varchar(180) comment '主键生成器',
  SORT_CODE            varchar(30) comment '排序代码',
  NAME                 varchar(150) comment '要素名称',
  NAME_I18N_CODE       varchar(180) comment '要素名称国际化代码',
  COLUMN_              varchar(120) comment '数据列',
  TABLE_               varchar(120) comment '数据表名',
  UPDATEABLE           varchar(1) comment '是否可更新',
  SORTABLE             varchar(1) comment '是否可排序',
  PERSIST              varchar(1) comment '是否持久化',
  DATA_TYPE            varchar(120) comment '字段数据类型',
  DEFAULT_VALUE        varchar(120) comment '默认值',
  SUMMARY_EXPRESSION   varchar(180) comment '字段统计表达式',
  ENABLE               varchar(1) comment '字段是否可用',
  GROUP_               varchar(120) comment '分组',
  GROUP_I18N_CODE      varchar(180) comment '分组国际化代码',
  SUB_GROUP            varchar(120) comment '二级分组',
  SUB_GROUP_I18N_CODE  varchar(180) comment '二级分组国际化代码',
  LIMITED_LENGTH       int comment '最大长度',
  MULTIPLIER           numeric(32,8) comment '数字倍数',
  READONLY             varchar(1) comment '是否只读',
  REQUIRED             varchar(1) comment '是否必需',
  DATA_FORMAT          varchar(120) comment '数据格式',
  MASK_FORMAT          varchar(120) comment '掩码',
  TEXT_ALIGN           varchar(120) comment '对齐方式',
  EDIT_STYLE           varchar(120) comment '编辑方式',
  DICT_CODE_MODE       varchar(120) comment '数据字典取值模式',
  DICT_CODE_EXPR       varchar(180) comment '数据字典表达式',
  PREFIX               varchar(180) comment '前缀',
  SUFFIX               varchar(180) comment '后缀',
  TIPS                 varchar(180) comment '小贴示',
  TIPS_I18N_CODE       varchar(180) comment '小贴示国际化代码',
  NOTE                 varchar(180) comment '补充说明',
  NOTE_I18N_CODE       varchar(180) comment '补充说明国际化代码',
  VISIBLE              varchar(1) comment '是否可见',
  RANK                 varchar(120) comment '层级权重',
  MEDIA_QUERY          varchar(120) comment '终端媒体查询',
  HTML_STYLE           varchar(180) comment 'HTML样式',
  COLSPAN              int comment '所占列数',
  EVENT_EXPR           varchar(180) comment '事件表达式',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (DATAFORM_ID, CODE)
);

alter table FOWK_DATAFORM_ELEMENT comment '显示模板元素';

/*==============================================================*/
/* Table: FOWK_DATAFORM_FILTER                                  */
/*==============================================================*/
create table FOWK_DATAFORM_FILTER
(
  DATAFORM_ID          varchar(180) not null comment '模板标识',
  CODE                 varchar(120) not null comment '过滤器代码',
  NAME                 varchar(150) comment '名称',
  NAME_I18N_CODE       varchar(180) comment '名称国际化代码',
  BIND_FOR             varchar(120) comment '绑定目标',
  ENABLE               varchar(1) comment '是否可用',
  QUICK                varchar(1) comment '是否支持快速查询',
  COMPARE_PATTERN      varchar(120) comment '匹配模式',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (DATAFORM_ID, CODE)
);

alter table FOWK_DATAFORM_FILTER comment '显示模板过滤器';

/*==============================================================*/
/* Table: FOWK_DATAFORM_VALIDATOR                               */
/*==============================================================*/
create table FOWK_DATAFORM_VALIDATOR
(
  DATAFORM_ID          varchar(180) not null comment '模板标识',
  ELEMENT_CODE         varchar(120) not null comment '元素标识码',
  CODE                 varchar(120) not null comment '规则代码',
  RUN_AT               varchar(120) comment '运行方式',
  MODE                 varchar(120) comment '校验方式',
  EXPR                 varchar(180) comment '表达式',
  TRIGGER_EVENT        varchar(120) comment '触发事件',
  MESSAGE              varchar(180) comment '默认消息',
  MESSAGE_I18N_CODE    varchar(180) comment '默认消息国际化',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (DATAFORM_ID, ELEMENT_CODE, CODE)
);

alter table FOWK_DATAFORM_VALIDATOR comment '显示模板校验规则';

/*==============================================================*/
/* Table: FOWK_DICT                                             */
/*==============================================================*/
create table FOWK_DICT
(
  CODE                 varchar(32) not null comment '字典代码',
  NAME                 varchar(150) comment '字典名称',
  NAME_I18N_CODE       varchar(180) comment '名称国际化代码',
  CATEGORY_CODE        varchar(32) comment '分类代码',
  SORT_CODE            varchar(32) comment '排序代码',
  INTRO                varchar(900) comment '摘要描述',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (CODE)
);

alter table FOWK_DICT comment '数据字典';

/*==============================================================*/
/* Table: FOWK_DICT_ITEM                                        */
/*==============================================================*/
create table FOWK_DICT_ITEM
(
  DICT_CODE            varchar(32) not null comment '字典代码',
  CODE                 varchar(32) not null comment '条目代码',
  NAME                 varchar(150) comment '条目名称',
  NAME_I18N_CODE       varchar(180) comment '条目名称国际化代码',
  SORT_CODE            varchar(120) comment '排序代码',
  HOTSPOT              int comment '热点值',
  CORRELATION          varchar(120) comment '关联对象',
  STATUS               varchar(32) comment '状态',
  SUMMARY              varchar(900) comment '摘要描述',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (DICT_CODE, CODE)
);

alter table FOWK_DICT_ITEM comment '数据字典条目';

/*==============================================================*/
/* Table: FOWK_MENU                                             */
/*==============================================================*/
create table FOWK_MENU
(
  ID                   varchar(32) not null comment '唯一标识号',
  NAME                 varchar(150) comment '名称',
  NAME_I18N_CODE       varchar(180) comment '名称国际化代码',
  SUMMARY              varchar(900) comment '摘要说明',
  SORT_CODE            varchar(30) comment '排序代码',
  ICON                 varchar(30) comment '图标',
  URL                  varchar(180) comment 'URL地址',
  PARAM                varchar(180) comment '参数',
  ENABLE               varchar(1) comment '启用',
  CONTAINER            varchar(180) comment '打开目标容器',
  STYLE                varchar(180) comment '样式',
  TEMPLATE             varchar(180) comment 'HTML模板',
  PERMIT_CODE          varchar(180) comment '权限代码',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (ID)
);

alter table FOWK_MENU comment '系统菜单';

/*==============================================================*/
/* Table: FOWK_PARAM                                            */
/*==============================================================*/
create table FOWK_PARAM
(
  CODE                 varchar(32) not null comment '参数代码',
  NAME                 varchar(150) comment '参数名称',
  NAME_I18N_CODE       varchar(180) comment '名称国际化代码',
  SORT_CODE            varchar(32) comment '排序代码',
  INTRO                varchar(900) comment '摘要描述',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (CODE)
);

alter table FOWK_PARAM comment '基础参数';

/*==============================================================*/
/* Table: FOWK_PARAM_ITEM                                       */
/*==============================================================*/
create table FOWK_PARAM_ITEM
(
  PARAM_CODE           varchar(32) not null comment '参数代码,字典代码',
  CODE                 varchar(180) not null comment '参数条目代码',
  NAME                 varchar(150) comment '条目名称',
  NAME_I18N_CODE       varchar(180) comment '名称国际化代码',
  SORT_CODE            varchar(120) comment '排序代码',
  VALUE                varchar(900) comment '配置值',
  ENABLE               varchar(1) comment '是否启用',
  SUMMARY              varchar(900) comment '摘要描述',
  VALUE1               varchar(900) comment '配置值1',
  VALUE2               varchar(900) comment '配置值2',
  VALUE3               varchar(900) comment '配置值3',
  VALUE4               varchar(900) comment '配置值4',
  VALUE5               varchar(900) comment '配置值5',
  VALUE6               varchar(900) comment '配置值6',
  VALUE7               varchar(900) comment '配置值7',
  VALUE8               varchar(900) comment '配置值8',
  VALUE9               varchar(900) comment '配置值9',
  VALUE10              varchar(900) comment '配置值10',
  REVISION             int comment '乐观锁版本',
  CREATED_BY           varchar(32) comment '创建人',
  CREATED_TIME         datetime comment '创建时间',
  UPDATED_BY           varchar(32) comment '更新人',
  UPDATED_TIME         datetime comment '更新时间',
  primary key (PARAM_CODE, CODE)
);

alter table FOWK_PARAM_ITEM comment '基础参数明细';

/*==============================================================*/
/* Table: FOWK_SERIAL_NUMBER                                    */
/*==============================================================*/
create table FOWK_SERIAL_NUMBER
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

alter table FOWK_SERIAL_NUMBER comment '流水号记录表';
