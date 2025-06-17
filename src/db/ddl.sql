-- 用户信息表
CREATE TABLE user_info (
    id BIGSERIAL PRIMARY KEY,
    user_number VARCHAR(50) UNIQUE NOT NULL,
    user_name VARCHAR(50)  NOT NULL,
    create_time timestamp DEFAULT CURRENT_TIMESTAMP,
	  update_time timestamp DEFAULT CURRENT_TIMESTAMP
);
create index idx_user_number on user_info(user_number);
comment on table  user_info is '用户信息表';
comment on column user_info.user_number is '用户号码';
comment on column user_info.user_name is '用户名称';
comment on column user_info.create_time is '创建时间';
comment on column user_info.update_time is '更新时间';
-- 用户账户余额表
CREATE TABLE user_account (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'CNY',
    set_dt VARCHAR(8) NOT NULL DEFAULT '99991231',
    create_time timestamp DEFAULT CURRENT_TIMESTAMP,
	  update_time timestamp DEFAULT CURRENT_TIMESTAMP
);
create index idx_user_account_user_id on user_account(user_id);
create index idx_user_account_set_dt on user_account(set_dt);
comment on table  user_account is '用户账户余额表';
comment on column user_account.user_id is '用户id';
comment on column user_account.balance is '账户余额';
comment on column user_account.currency is '币种';
comment on column user_account.set_dt is '结算日期';
comment on column user_account.create_time is '创建时间';
comment on column user_account.update_time is '更新时间';
-- 用户账户流水表
CREATE TABLE user_account_flow (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    trd_type int2 NOT NULL,
    order_id BIGINT,
    create_dt VARCHAR(8) NOT NULL,
    create_time timestamp DEFAULT CURRENT_TIMESTAMP,
	  update_time timestamp DEFAULT CURRENT_TIMESTAMP
);
create index idx_user_account_flow_account_id on user_account_flow(account_id);
create index idx_user_account_flow_order_id on user_account_flow(order_id);
create index idx_user_account_flow_create_dt on user_account_flow(create_dt);
comment on table  user_account_flow is '用户账户流水表';
comment on column user_account_flow.account_id is '用户账户id';
comment on column user_account_flow.amount is '金额';
comment on column user_account_flow.trd_type is '交易类型';
comment on column user_account_flow.order_id is '订单id';
comment on column user_account_flow.create_dt is '交易日期';
comment on column user_account_flow.create_time is '创建时间';
comment on column user_account_flow.update_time is '更新时间';
-- 商家信息表
CREATE TABLE merchant_info (
    id BIGSERIAL PRIMARY KEY,
    merchant_number VARCHAR(50) UNIQUE NOT NULL,
    merchant_name VARCHAR(50)  NOT NULL,
    create_time timestamp DEFAULT CURRENT_TIMESTAMP,
	  update_time timestamp DEFAULT CURRENT_TIMESTAMP
);
create index idx_merchant_number on merchant_info(merchant_number);
comment on table  merchant_info is '商家信息表';
comment on column merchant_info.merchant_number is '商家号码';
comment on column merchant_info.merchant_name is '商家名称';
comment on column merchant_info.create_time is '创建时间';
comment on column merchant_info.update_time is '更新时间';
-- 商家账户余额表
CREATE TABLE merchant_account (
    id BIGSERIAL PRIMARY KEY,
    merchant_id BIGINT NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'CNY',
    set_dt VARCHAR(8) NOT NULL DEFAULT '99991231',
    create_time timestamp DEFAULT CURRENT_TIMESTAMP,
	  update_time timestamp DEFAULT CURRENT_TIMESTAMP
);
create index idx_merchant_account_merchant_id on merchant_account(merchant_id);
create index idx_merchant_account_set_dt on merchant_account(set_dt);
comment on table  merchant_account is '商家账户余额表';
comment on column merchant_account.merchant_id is '商家id';
comment on column merchant_account.balance is '账户余额';
comment on column merchant_account.currency is '币种';
comment on column merchant_account.set_dt is '结算日期';
comment on column merchant_account.create_time is '创建时间';
comment on column merchant_account.update_time is '更新时间';
-- 商家账户流水表
CREATE TABLE merchant_account_flow (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    trd_type int2 NOT NULL,
    order_id BIGINT,
    create_dt VARCHAR(8) NOT NULL,
    create_time timestamp DEFAULT CURRENT_TIMESTAMP,
	  update_time timestamp DEFAULT CURRENT_TIMESTAMP
);
create index idx_merchant_account_flow_account_id on merchant_account_flow(account_id);
create index idx_merchant_account_flow_order_id on merchant_account_flow(order_id);
create index idx_merchant_account_flow_create_dt on merchant_account_flow(create_dt);
comment on table  merchant_account_flow is '商家账户流水表';
comment on column merchant_account_flow.account_id is '商家账户id';
comment on column merchant_account_flow.amount is '金额';
comment on column merchant_account_flow.trd_type is '交易类型';
comment on column merchant_account_flow.order_id is '订单id';
comment on column merchant_account_flow.create_dt is '交易日期';
comment on column merchant_account_flow.create_time is '创建时间';
comment on column merchant_account_flow.update_time is '更新时间';
-- 商品库存表
CREATE TABLE product_stock (
    id BIGSERIAL PRIMARY KEY,
    merchant_id BIGINT NOT NULL,
    sku VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    create_time timestamp DEFAULT CURRENT_TIMESTAMP,
	  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (merchant_id, sku)
);
create index idx_product_stock_merchant_id on product_stock(merchant_id);
create index idx_product_stock_sku on product_stock(sku);
comment on table  product_stock is '商品库存表';
comment on column product_stock.merchant_id is '商家id';
comment on column product_stock.sku is '商品sku';
comment on column product_stock.name is '商品名称';
comment on column product_stock.price is '商品单价';
comment on column product_stock.stock is '商品库存数量';
comment on column product_stock.create_time is '创建时间';
comment on column product_stock.update_time is '更新时间';
-- 订单信息表
CREATE TABLE order_info (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    status int2 NOT NULL,
    create_dt VARCHAR(8) NOT NULL,
    create_time timestamp DEFAULT CURRENT_TIMESTAMP,
	  update_time timestamp DEFAULT CURRENT_TIMESTAMP
);
create index idx_order_info_user_id on order_info(user_id);
create index idx_order_info_merchant_id on order_info(merchant_id);
create index idx_order_info_create_dt on order_info(create_dt);
comment on table  order_info is '订单信息表';
comment on column order_info.user_id is '用户id';
comment on column order_info.merchant_id is '商家id';
comment on column order_info.total_amount is '总金额';
comment on column order_info.status is '订单状态';
comment on column order_info.create_dt is '创建日期';
comment on column order_info.create_time is '创建时间';
comment on column order_info.update_time is '更新时间';
-- 订单明细表
CREATE TABLE order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    create_time timestamp DEFAULT CURRENT_TIMESTAMP,
	  update_time timestamp DEFAULT CURRENT_TIMESTAMP
);
create index idx_order_item_order_id on order_item(order_id);
create index idx_order_item_product_id on order_item(product_id);
comment on table  order_item is '订单明细表';
comment on column order_item.order_id is '订单id';
comment on column order_item.product_id is '商品id';
comment on column order_item.quantity is '商品数量';
comment on column order_item.price is '商品单价';
comment on column order_item.create_time is '创建时间';
comment on column order_item.update_time is '更新时间';


