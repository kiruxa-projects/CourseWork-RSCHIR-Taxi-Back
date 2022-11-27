DO $$
BEGIN
create table IF NOT EXISTS users
(
    id           serial
    constraint users_pk
    primary key,
    first_name   text,
    last_name    text,
    type         text,
    active_order integer,
    login        text,
    password     text
);


create table IF NOT EXISTS orders
(
    id           serial
    constraint orders_pk
    primary key,
    client_id    integer,
    driver_id    integer,
    from_address text,
    to_address   text,
    cost         integer,
    start_time   integer,
    status       text,
    end_time     integer
);


create table IF NOT EXISTS cars
(
    id        serial
    constraint cars_pk
    primary key,
    number    text,
    model     text,
    color     text,
    driver_id integer
);
EXCEPTION
  WHEN others THEN
    RAISE NOTICE 'SQLSTATE: %', SQLSTATE;
    RAISE;
END $$;
