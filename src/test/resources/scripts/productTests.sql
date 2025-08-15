INSERT INTO public.category (id,
                             name,
                             description,
                             created_at,
                             updated_at)
VALUES ('61fef552-17b1-46ef-9452-48b93ad51022',
        'Mouse',
        'Gamer Mouse',
        '2025-06-20 14:48:23.453723',
        '2025-06-20 14:48:59.511112');

INSERT INTO public.product (
    id,
    name,
    description,
    price,
    stock,
    small_image_url,
    big_image_url,
    id_category,
    created_at,
    updated_at
) VALUES (
             'a8f01033-6f92-4ef5-9437-7d54738c9b1a',
             'Gaming Mouse',
             'High precision gaming mouse',
             59.99,
             100,
             'https://example.com/small.jpg',
             'https://example.com/big.jpg',
             '61fef552-17b1-46ef-9452-48b93ad51022',
             '2025-06-20 14:48:23.453723',
             '2025-06-20 14:48:59.511112'
         );