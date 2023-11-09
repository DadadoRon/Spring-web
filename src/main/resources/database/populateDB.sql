INSERT INTO users VALUES
                      (1, 'Dasha', 'Black', 'black@mail', '123', 'ADMIN'),
                      (2, 'Dasha', 'Green', 'greenk@mail', '124', 'USER')
ON CONFLICT (id) DO NOTHING;



INSERT INTO products ( name, description, price, image_name)
VALUES ( 'Good', 'Description', 100, 'img/n3.jpg')
ON CONFLICT (name) DO NOTHING;

