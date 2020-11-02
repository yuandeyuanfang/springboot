DROP TABLE IF EXISTS "PARAM_INFO";
CREATE TABLE "PARAM_INFO" (
  "PARAM_NAME" varchar(100) NOT NULL,
  "PARAM_VALUE" varchar(255) DEFAULT NULL,
  primary key ("PARAM_NAME")
);

DROP TABLE IF EXISTS TRADE_LOG;
CREATE TABLE TRADE_LOG (
  ID NUMBER(16) NOT NULL,
  TRADE_ID VARCHAR2(30),
  TRADE_TYPE VARCHAR2(50) NOT NULL,
  INPUT CLOB,
  OUTPUT CLOB,
  EXTRA CLOB,
  DES CLOB,
  TRADE_RESULT VARCHAR2(10),
  TRADE_BEGAIN VARCHAR2(30),
  TRADE_END VARCHAR2(30),
  TRADE_COST NUMBER(16),
  TRADE_DATE VARCHAR2(8) NOT NULL,
  MODEL VARCHAR2(30) NOT NULL
);
alter table TRADE_LOG add constraint PK_TRADE_LOG primary key (ID);
create sequence TRADE_LOG_SEQ
minvalue 1
maxvalue 999999999
start with 100001
increment by 1
nocache;
comment on table TRADE_LOG is '日志记录表';
comment on column TRADE_LOG.TRADE_ID is '交易发送报文ID';
comment on column TRADE_LOG.MODEL is '交易渠道';
comment on column TRADE_LOG.TRADE_TYPE is '交易类型';
comment on column TRADE_LOG.INPUT is '交易入口报文';
comment on column TRADE_LOG.OUTPUT is '交易出口报文';
comment on column TRADE_LOG.EXTRA is '交易附加报文';
comment on column TRADE_LOG.DES is '交易解密报文';
comment on column TRADE_LOG.TRADE_RESULT is '交易结果';
comment on column TRADE_LOG.TRADE_BEGAIN is '交易开始时间';
comment on column TRADE_LOG.TRADE_END is '交易结束时间';
comment on column TRADE_LOG.TRADE_COST is '交易花费时间（毫秒）';
comment on column TRADE_LOG.TRADE_DATE is '交易日期';