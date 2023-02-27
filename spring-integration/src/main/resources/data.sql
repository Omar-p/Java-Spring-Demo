INSERT INTO ticket_types (ticket_type_code, ticket_type_name, description, includes_workshop)
VALUES ('P', 'Premium', 'Access to all conference events plus attend the workshop of your choice.', TRUE),
       ('S', 'Standard', 'Access to all conference keynotes, sessions, community open spaces and the exhibition hall.', FALSE),
       ('C', 'Community', 'Access to keynotes, community open spaces and the exhibition hall.', FALSE);

INSERT INTO pricing_categories (pricing_category_code, pricing_category_name, pricing_start_date, pricing_end_date)
VALUES ('E', 'Early Bird', '2020-07-01', '2020-12-31'),
       ('R', 'Regular', '2021-01-01', '2021-03-31'),
       ('L', 'Last Minute', '2021-04-01', '2021-12-31');

INSERT INTO ticket_prices (ticket_price_id, ticket_type_code, pricing_category_code, base_price)
VALUES (1, 'P', 'E', 800),
       (2, 'P', 'R', 1000),
       (3, 'P', 'L', 1200),
       (4, 'S', 'E', 500),
       (5, 'S', 'R', 700),
       (6, 'S', 'L', 1000),
       (7, 'C', 'E', 100),
       (8, 'C', 'R', 200),
       (9, 'C', 'L', 300);
