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
    "authorities" json NOT NULL,
    "created_at" TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
);
create unique index ix_users_username on users (username);
create unique index ix_users_email on users (email);
CREATE TABLE "category"
(
    "id"              UUID           NOT NULL,
    "name"            VARCHAR(50)   NOT NULL,
    "description"     TEXT           NOT NULL,
    "created_at"      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT "category_pkey" PRIMARY KEY ("id")
);