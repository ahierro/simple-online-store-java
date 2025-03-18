-- liquibase formatted sql

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
    "updated_at" TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT "user_pkey" PRIMARY KEY ("id")
);
create unique index ix_users_username on "users" (username);
create unique index ix_users_email on "users" (email);
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
    "updated_at" TIMESTAMP WITHOUT TIME ZONE,
    "deleted" boolean NOT NULL DEFAULT false,
    CONSTRAINT "category_pkey" PRIMARY KEY ("id")
);
--rollback drop table category;

-- changeset alejandro:1672328951075-1
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
    "id_category"     UUID NOT NULL,
    "deleted" boolean NOT NULL DEFAULT false,
    "updated_at" TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT "product_pkey" PRIMARY KEY ("id")
);
alter table "product" add constraint "p_category_fk" foreign key ("id_category") references "category" ("id");
--rollback drop table product;

-- changeset alejandro:1672328951076-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables where table_name = 'product'
-- comment: Initial creation of ix_product_id_category
CREATE INDEX ix_product_id_category ON product (id_category);
--rollback DROP INDEX ix_product_id_category;

-- changeset alejandro:1672328951078-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables where table_name = 'purchase_order'
-- comment: Initial creation of table purchase_order
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
create table "purchase_order"
(
    "id"         UUID NOT NULL DEFAULT uuid_generate_v4(),
    "id_user"    UUID not null,
    "status"     VARCHAR(50) NOT NULL,
    "total"      numeric(16, 2) NOT NULL,
    "created_at" TIMESTAMP WITHOUT TIME ZONE,
    "updated_at" TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT "purchase_order_pkey" PRIMARY KEY ("id")
);
alter table "purchase_order" add constraint "po_id_user_fk" foreign key ("id_user") references "users" ("id");
--rollback drop table purchase_order;

-- changeset alejandro:1672328951079-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables where table_name = 'purchase_order_lines'
-- comment: Initial creation of table purchase_order_lines
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
create table "purchase_order_line"
(
    "id"         UUID NOT NULL DEFAULT uuid_generate_v4(),
    "id_purchase_order" UUID not null,
    "id_product" UUID not null,
    "quantity"   INTEGER not null,
    CONSTRAINT "purchase_order_lines_pkey" PRIMARY KEY ("id")
);
alter table "purchase_order_line" add constraint "po_id_product_fk" foreign key ("id_product") references "product" ("id");
alter table "purchase_order_line" add constraint "po_id_purchase_order_fk" foreign key ("id_purchase_order") references "purchase_order" ("id");
create unique index uix_purchase_order_line on purchase_order_line (id_purchase_order, id_product);
--rollback drop table purchase_order_line;

-- changeset alejandro:1672328951179-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables where table_name = 'purchase_order_view'
-- comment: Initial creation of view purchase_order_view
CREATE VIEW "purchase_order_view" AS
SELECT
    po.id as id,
    po.id_user as id_user,
    po.total as total,
    po.status as status,
    po.created_at as created_at,
    u.email as email,
    u.username as username,
    u.first_name as first_name,
    u.last_name as last_name
FROM purchase_order po INNER JOIN "users" u on u.id = po.id_user;
--rollback drop view purchase_order_view;

-- changeset alejandro:1672328951181-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables where table_name = 'product_view'
-- comment: Initial creation of view product_view
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
       c.created_at as category_created_at,
       p.deleted as deleted
FROM product p INNER JOIN category c on c.id = p.id_category;
--rollback drop view product_view;

-- changeset alejandro:1672328951182-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables where table_name = 'purchase_order_line_view'
-- comment: Initial creation of view purchase_order_line_view
CREATE VIEW "purchase_order_line_view" AS
SELECT
    po.id as id,
    po.id_purchase_order as id_purchase_order,
    po.quantity as quantity,
    p.id as id_product,
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
    c.created_at as category_created_at,
    p.deleted as deleted
FROM purchase_order_line po INNER JOIN product p ON po.id_product = p.id INNER JOIN category c on c.id = p.id_category;
--rollback drop view purchase_order_line_view;

-- changeset alejandro:1672328951182-2
-- preconditions onFail:HALT onError:HALT
-- comment: Initial Admin User Creation
INSERT INTO public.users (id, username, password, email, first_name, last_name, active, locked, authorities, created_at) VALUES ('130b1b88-5850-4d25-b81f-786925d09ab7', 'admin', '$2a$10$7EVF8hBxswNOWMPfpIImruKVkUbNcL51KK.TueUqUPjnfdAghhJmC', 'admin@gmail.com', 'Alejandro', 'Admin', true, false, '["ROLE_ADMIN"]', '2023-01-06 18:47:26.046147');
