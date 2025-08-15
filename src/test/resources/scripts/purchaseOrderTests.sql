-- Create test categories
INSERT INTO public.category (ID, NAME, DESCRIPTION, created_at)
VALUES ('345e0ef3-b8da-4a7e-ab5d-6636d63614b1', 'Hardware', 'Computer Hardware', CURRENT_TIMESTAMP);

-- Create test products
INSERT INTO public.product (ID, NAME, DESCRIPTION, PRICE, STOCK, id_category, small_image_url, big_image_url,
                            created_at)
VALUES ('4ebeb473-435b-428c-aa4a-914ae472bc45', 'Keyboard', 'Mechanical Keyboard', 89.99, 10,
        '345e0ef3-b8da-4a7e-ab5d-6636d63614b1', 'keyboard_small.jpg', 'keyboard_big.jpg', CURRENT_TIMESTAMP),
       ('7486f916-1008-4611-98ef-746fe08197c6', 'Mouse', 'Gaming Mouse', 59.99, 15,
        '345e0ef3-b8da-4a7e-ab5d-6636d63614b1', 'mouse_small.jpg', 'mouse_big.jpg', CURRENT_TIMESTAMP);

-- Create test users
INSERT INTO public.users (ID, EMAIL, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, active, locked, authorities, created_at)
VALUES ('295ba273-ca1d-45bc-9818-f949223981f6', 'test@example.com', 'user',
        '$2a$10$X7.KmLlH23WAPFh.JGFkSeiDgBJ.cxZHyxxiLfEBGQIhYw.tBqiL2', 'Test', 'User', true, false, '["ROLE_USER"]',
        CURRENT_TIMESTAMP);

-- Create test purchase orders
INSERT INTO public.purchase_order (ID, id_user, status, total, created_at)
VALUES ('b451dafd-7c96-43b6-bf5f-ac522dd3026c', '295ba273-ca1d-45bc-9818-f949223981f6', 'PENDING', 149.98,
        CURRENT_TIMESTAMP);

-- Create purchase order lines
INSERT INTO public.purchase_order_line (ID, id_purchase_order, id_product, quantity)
VALUES ('27bd64d2-3859-4aad-8e54-a364bc981ad7', 'b451dafd-7c96-43b6-bf5f-ac522dd3026c',
        '4ebeb473-435b-428c-aa4a-914ae472bc45', 1),
       ('c54119bd-9653-4559-b2be-6cbd3e074289', 'b451dafd-7c96-43b6-bf5f-ac522dd3026c',
        '7486f916-1008-4611-98ef-746fe08197c6', 1);
