--CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--  DEFAULT uuid_generate_v4 ()
CREATE TABLE IF NOT EXISTS PRODUCT (
    ID uuid primary key,
    NAME varchar(255) not null,
    DESCRIPTION text not null,
    STOCK integer not null,
    PRICE numeric(16, 2) not null,
    SMALL_IMAGE_URL varchar(255) not null,
    BIG_IMAGE_URL varchar(255) not null,
    CREATED_AT timestamp
);
