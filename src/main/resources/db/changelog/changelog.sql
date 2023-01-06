-- liquibase formatted sql

-- changeset alejandro:1672328951072-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables where table_name = 'product'
-- comment: Initial creation of table product
CREATE TABLE "product"
(
    "id"              UUID           NOT NULL,
    "name"            VARCHAR(255)   NOT NULL,
    "description"     TEXT           NOT NULL,
    "stock"           INTEGER        NOT NULL,
    "price"           numeric(16, 2) NOT NULL,
    "small_image_url" VARCHAR(255)   NOT NULL,
    "big_image_url"   VARCHAR(255)   NOT NULL,
    "created_at"      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT "product_pkey" PRIMARY KEY ("id")
);
--rollback drop table product;

-- changeset alejandro:1672328951073-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables where table_name = 'users'
-- comment: Initial creation of table users
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
create table "users"
(
    "id"         UUID        NOT NULL DEFAULT uuid_generate_v4(),
    "username"   VARCHAR(50) not null,
    "password"   VARCHAR(500) not null,
    "email"      VARCHAR(50) not null,
    "first_name"  VARCHAR(100) not null,
    "last_name"   VARCHAR(100) not null,
    "active"     boolean     not null default true,
    "locked"     boolean     not null default false,
    "roles"      json NOT NULL,
    "created_at" TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
);
create unique index ix_users_username on users (username);
--rollback drop table users;



