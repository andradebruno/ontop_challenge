INSERT INTO user_data (user_id, created_at, first_name, last_name, national_id_number)
VALUES (1, CURRENT_TIMESTAMP(), 'TONY', 'STARK', '123456789');

INSERT
INTO bank_account (bank_account_id, account_number, bank_name, created_at, currency, routing_number, user_id)
VALUES (1, '123456789', 'Awesome Bank', CURRENT_TIMESTAMP(), 'USD', '987654321', 1);

INSERT
INTO bank_account (bank_account_id, account_number, bank_name, created_at, currency, routing_number, user_id)
VALUES (2, '987654321', 'Cool Bank', CURRENT_TIMESTAMP(), 'BRL', '123456789', 1);