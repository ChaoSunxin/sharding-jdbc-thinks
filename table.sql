create table t_order_0
(
    order_id     varchar(16),
    user_id      varchar(16),
    gmt_create   timestamp,
    gmt_modified timestamp
);


create table t_order_1
(
    order_id     varchar(16),
    user_id      varchar(16),
    gmt_create   timestamp,
    gmt_modified timestamp
);


create table t_order_2
(
    order_id     varchar(16),
    user_id      varchar(16),
    gmt_create   timestamp,
    gmt_modified timestamp
);

create table t_order_item_0
(
    item_id      varchar(16),
    order_id     varchar(16),
    product_id   varchar(16),
    item_nums    int,
    gmt_create   timestamp,
    gmt_modified timestamp
);

create table t_order_item_1
(
    item_id      varchar(16),
    order_id     varchar(16),
    product_id   varchar(16),
    item_nums    int,
    gmt_create   timestamp,
    gmt_modified timestamp
);

create table t_order_item_2
(
    item_id      varchar(16),
    order_id     varchar(16),
    product_id   varchar(16),
    item_nums    int,
    gmt_create   timestamp,
    gmt_modified timestamp
);