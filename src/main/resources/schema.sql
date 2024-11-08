create table if not exists currency (
    code varchar(30) not null,
    label nvarchar(100),
    create_time datetime2 not null,
    modify_time datetime2,
    primary key (code)
);