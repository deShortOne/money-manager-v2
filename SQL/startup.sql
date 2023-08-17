DROP DATABASE IF EXISTS money;
CREATE DATABASE money;
USE money;

CREATE TABLE accounts (
	ID INT AUTO_INCREMENT,
	AccountName VARCHAR(50),
	PRIMARY KEY(ID)
);
INSERT INTO accounts (AccountName) VALUES ('Acc 1 - payer'), ('Acc 2 - payee');

CREATE TABLE bills (
	ID INT AUTO_INCREMENT,
	PayerAccount INT,
	PayeeAccount INT,
	Amount DOUBLE,
	PRIMARY KEY(ID),
	FOREIGN KEY(PayerAccount) REFERENCES accounts(ID),
	FOREIGN KEY(PayeeAccount) REFERENCES accounts(ID)
);
INSERT INTO bills (PayerAccount, PayeeAccount, Amount) VALUES (1, 2, 10), (1, 2, 20), (1, 2, 30), (1, 2, 40), (1, 2, 50), (1, 2, 60) 
