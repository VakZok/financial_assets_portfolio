INSERT INTO share (wkn, name, description, category)
VALUES ('123456', 'Allianz', 'Versicherungsgesellschaft', 'Aktie');

INSERT INTO share (wkn, name, description, category)
VALUES ('987654', 'BASF', 'Chemie Unternehmen', 'Aktie');

INSERT INTO share (wkn, name, description, category)
VALUES ('BTC', 'Bitcoin', 'Kryptowährung', 'Crypto');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('0', '123456', '2023-11-01', '100', '100');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('1', '123456', '2023-11-02', '200', '200');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('2', '987654', '2023-11-02', '50', '50');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('3', '987654', '2023-11-02', '100', '40');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('4', '987654', '2023-11-03', '200', '30');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price)
VALUES ('5', 'BTC', '2023-11-03', '1', '32000');

