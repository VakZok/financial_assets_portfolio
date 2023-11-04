INSERT INTO share (wkn, name, description, category)
VALUES ('123456', 'Allianz', 'Versicherungsgesellschaft', 'Aktie');

INSERT INTO share (wkn, name, description, category)
VALUES ('987654', 'BASF', 'Chemie Unternehmen', 'Aktie');

INSERT INTO share (wkn, name, description, category)
VALUES ('BTC', 'Bitcoin', 'Kryptowährung', 'Crypto');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price, total_price)
VALUES ('0', '123456', '2023-11-01', '100', '100', '10000');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price, total_price)
VALUES ('1', '123456', '2023-11-02', '200', '200', '40000');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price, total_price)
VALUES ('2', '987654', '2023-11-02', '50', '50', '2500');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price, total_price)
VALUES ('3', '987654', '2023-11-02', '100', '40', '4000');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price, total_price)
VALUES ('4', '987654', '2023-11-03', '200', '30', '6000');

INSERT INTO portfolio_item (id, wkn, purchase_date, quantity, purchase_price, total_price)
VALUES ('5', 'BTC', '2023-11-03', '1', '32000', '32000');

