INSERT INTO menu_items (
    name,
    description,
    price,
    category,
    preparation_time,
    weight,
    image_url,
    attributes,
    created_at,
    updated_at
) VALUES
    (
        'Flat White',
        'Espresso with velvety microfoam milk',
        12.50,
        'DRINKS',
        180,
        220,
        'https://images.cloudmarketplace.dev/flat-white.png',
        '{"ingredients":[{"name":"espresso","calories":5},{"name":"milk","calories":60}]}',
        '2024-03-01 09:00:00',
        '2024-03-01 09:00:00'
    ),
    (
        'Berry Smoothie',
        'Refreshing smoothie with mixed forest berries',
        15.00,
        'DRINKS',
        120,
        300,
        'https://images.cloudmarketplace.dev/berry-smoothie.png',
        '{"ingredients":[{"name":"strawberry","calories":30},{"name":"blueberry","calories":25},{"name":"yogurt","calories":80}]}',
        '2024-03-01 09:05:00',
        '2024-03-01 09:05:00'
    ),
    (
        'Matcha Latte',
        'Japanese green tea latte with creamy milk',
        13.75,
        'DRINKS',
        150,
        250,
        'https://images.cloudmarketplace.dev/matcha-latte.png',
        '{"ingredients":[{"name":"matcha","calories":10},{"name":"milk","calories":70}]}',
        '2024-03-01 09:10:00',
        '2024-03-01 09:10:00'
    ),
    (
        'Quinoa Avocado Salad',
        'Healthy salad with quinoa, avocado, and fresh vegetables',
        22.00,
        'SALADS',
        600,
        350,
        'https://images.cloudmarketplace.dev/quinoa-avocado-salad.png',
        '{"ingredients":[{"name":"quinoa","calories":120},{"name":"avocado","calories":160},{"name":"tomatoes","calories":20}]}',
        '2024-03-01 09:15:00',
        '2024-03-01 09:15:00'
    ),
    (
        'Mediterranean Chickpea Salad',
        'Chickpeas with feta cheese, olives, and herbs',
        21.50,
        'SALADS',
        500,
        320,
        'https://images.cloudmarketplace.dev/chickpea-salad.png',
        '{"ingredients":[{"name":"chickpeas","calories":150},{"name":"feta","calories":100},{"name":"olives","calories":50}]}',
        '2024-03-01 09:20:00',
        '2024-03-01 09:20:00'
    ),
    (
        'Gourmet Beef Burger',
        'Juicy grilled beef burger with cheddar and caramelized onions',
        28.90,
        'SNACKS',
        900,
        450,
        'https://images.cloudmarketplace.dev/beef-burger.png',
        '{"ingredients":[{"name":"beef patty","calories":350},{"name":"cheddar","calories":120},{"name":"bun","calories":200}]}',
        '2024-03-01 09:25:00',
        '2024-03-01 09:25:00'
    );
