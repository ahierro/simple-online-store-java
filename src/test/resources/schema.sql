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
    "authorities" json NOT NULL,
    "created_at" TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
);
create unique index ix_users_username on users (username);
create unique index ix_users_email on users (email);
--rollback drop table users;

-- changeset alejandro:1672328951074-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables where table_name = 'category'
-- comment: Initial creation of table category
CREATE TABLE "category"
(
    "id"              UUID           NOT NULL,
    "name"            VARCHAR(50)   NOT NULL,
    "description"     TEXT           NOT NULL,
    "created_at"      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT "category_pkey" PRIMARY KEY ("id")
);
--rollback drop table category;


-- changeset alejandro:1672328951075-1
-- preconditions onFail:HALT onError:HALT
-- comment: Initial creation of table category
ALTER TABLE "product" ADD COLUMN "id_category" UUID;
alter table "product" add constraint "p_category_fk" foreign key ("id_category") references "category" ("id");
--rollback ALTER TABLE "product" DROP COLUMN "id_category";

-- changeset alejandro:1672328951076-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables where table_name = 'product_view'
-- comment: Initial creation of table category
CREATE VIEW "product_view" AS
SELECT p.id as id,
       p.name as product_name,
       p.price as price,
       p.stock as stock,
       p.description as product_description,
       p.big_image_url as big_image_url,
       p.small_image_url as small_image_url,
       c.name as category_name,
       c.description as category_description,
       p.id_category as id_category,
       p.created_at as product_created_at,
       c.created_at as category_created_at
FROM product p INNER JOIN category c on c.id = p.id_category
--rollback drop view product_view;
