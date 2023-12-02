INSERT INTO account (username, password, name, role)
VALUES ('john.doe', 'mypassword', 'John Doe', 'admin');

INSERT INTO share (wkn, name, description, category, username)
VALUES ('123456', 'Allianz', 'Versicherungsgesellschaft', 'Aktie', 'john.doe');

INSERT INTO share (wkn, name, description, category, username)
VALUES ('987654', 'BASF', 'Chemie Unternehmen', 'Aktie', 'john.doe');

INSERT INTO share (wkn, name, description, category, username)
VALUES ('BTC', 'Bitcoin', 'Kryptowährung', 'Crypto', 'john.doe');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('0', '123456', '2023-11-01', '100', '100');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('1', '123456', '2023-11-02', '200', '200');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('2', '987654', '2023-11-02', '50', '50');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('3', '987654', '2023-11-02', '100', '40');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('4', '987654', '2023-11-03', '200', '30');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('5', 'BTC', '2023-11-03', '1', '32000');



