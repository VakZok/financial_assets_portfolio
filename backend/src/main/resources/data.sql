INSERT INTO account (username, password, name, role)
VALUES ('john.doe', 'mypassword', 'John Doe', 'ADMIN');

INSERT INTO account (username, password, name, role)
VALUES ('npc', 'npcppassword', 'Npc', 'USER');

INSERT INTO share (wkn, name, description, category)
VALUES ('123456', 'Allianz', 'Versicherungsgesellschaft', 'Aktie');

INSERT INTO share (wkn, name, description, category)
VALUES ('987654', 'BASF', 'Chemie Unternehmen', 'Aktie');

INSERT INTO share (wkn, name, description, category)
VALUES ('BTC', 'Bitcoin', 'Kryptowährung', 'Crypto');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price, username)
VALUES ('0', '123456', '2023-11-01', '100', '100', 'john.doe');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price, username)
VALUES ('1', '123456', '2023-11-02', '200', '200', 'john.doe');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price, username)
VALUES ('2', '987654', '2023-11-02', '50', '50', 'john.doe');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price, username)
VALUES ('3', '987654', '2023-11-02', '100', '40', 'john.doe');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price, username)
VALUES ('4', '987654', '2023-11-03', '200', '30', 'john.doe');

INSERT INTO purchase (id, wkn, purchase_date, quantity, purchase_price, username)
VALUES ('5', 'BTC', '2023-11-03', '1', '32000', 'john.doe');



