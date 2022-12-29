-- liquibase formatted sql

-- changeset alejandro:1672328951072-1
-- preconditions onFail:HALT onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables where table_name = 'product'
-- comment: Initial creation of table product
CREATE TABLE "product" ("id" UUID NOT NULL, "name" VARCHAR(255) NOT NULL, "description" TEXT NOT NULL, "stock" INTEGER NOT NULL, "price" numeric(16, 2) NOT NULL, "small_image_url" VARCHAR(255) NOT NULL, "big_image_url" VARCHAR(255) NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE, CONSTRAINT "product_pkey" PRIMARY KEY ("id"));
--rollback drop table product;
