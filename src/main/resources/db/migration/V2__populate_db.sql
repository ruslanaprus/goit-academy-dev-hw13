-- Task 2

INSERT INTO clients (name, email)
VALUES ('Alice', 'alice@example.com'),
       ('Bob', 'bob@example.com'),
       ('Eve', 'eve@example.com'),
       ('Whiskers', 'whiskers@example.com'),
       ('Purrito', 'purrito@example.com'),
       ('Pawsters', 'pawsters@example.com'),
       ('Meowiarty', 'meowiarty@example.com'),
       ('Purrlock', 'purrlock@example.com'),
       ('Clawster', 'clawster@example.com'),
       ('Buttercup', 'buttercup@example.com')
ON CONFLICT DO NOTHING;

INSERT INTO planets (id, name)
VALUES ('MARS', 'Mars'),
       ('VEN', 'Venus'),
       ('JUP', 'Jupiter'),
       ('EAR', 'Earth'),
       ('SAT', 'Saturn'),
       ('NEP', 'Neptune'),
       ('URAN', 'Uranus'),
       ('PLU', 'Pluto'),
       ('MER', 'Mercury'),
       ('CER', 'Ceres')
ON CONFLICT DO NOTHING;

INSERT INTO tickets (client_id, from_planet_id, to_planet_id)
VALUES (1, 'EAR', 'MARS'),
       (2, 'MARS', 'VEN'),
       (3, 'JUP', 'EAR'),
       (4, 'VEN', 'JUP'),
       (5, 'SAT', 'EAR'),
       (6, 'NEP', 'SAT'),
       (7, 'URAN', 'NEP'),
       (8, 'PLU', 'URAN'),
       (9, 'MER', 'PLU'),
       (10, 'CER', 'MER')
ON CONFLICT DO NOTHING;