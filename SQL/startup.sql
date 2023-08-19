DROP DATABASE IF EXISTS money;
CREATE DATABASE money;
USE money;

CREATE TABLE account (
	ID INT AUTO_INCREMENT,
	AccountName VARCHAR(50),
	SortCode VARCHAR(8),
	AccountNumber VARCHAR(20),
	PRIMARY KEY(ID)
);
INSERT INTO account (AccountName) VALUES ('Acc 1 - payer'), ('Acc 2 - payee');

CREATE TABLE category (
	ID INT AUTO_INCREMENT,
	Name VARCHAR(50),
	IsIncome boolean,
	PRIMARY KEY(ID)
);
INSERT INTO category (Name, IsIncome) VALUES ('Automobile', false), ('Bank Charges', false), ('Bills', false), ('Cash Withdrawal', false), ('Childcare', false), ('Clothing', false), ('Credit Card Payments', false), ('Dining Out', false), ('Education', false), ('Entertainment', false), ('Fees', false), ('Gifts', false), ('Groceries', false), ('Healthcare', false), ('Hobbies/Leisure', false), ('Home Improvement', false), ('Household', false), ('Insurance', false), ('Job Expense', false), ('Loan', false), ('Miscellaneous', false), ('Mortgage/Rent', false), ('Personal Care', false), ('Pet Care', false), ('Taxes', false), ('Travel/Vacation', false), ('Utilities', false), ('Investment Income', true), ('Other Income', true), ('Wages & Salary', true);
-- will later add more categories and add subcategory ie for Automobile can have fuel and maintenance

CREATE TABLE payment_method (
	ID INT AUTO_INCREMENT,
	Name VARCHAR(50),
	PRIMARY KEY(ID)
);
INSERT INTO payment_method (Name) VALUES ('Cash'), ('Credit Card'), ('Debit Card'), ('Direct Debit'), ('Write Check'), ('Other');

CREATE TABLE bill (
	ID INT AUTO_INCREMENT,
	PayerAccount INT,
	PayeeAccount INT,
	Amount DOUBLE,
	Frequency VARCHAR(50),
	PaymentID INT,
	CategoryID INT,
	PRIMARY KEY(ID),
	FOREIGN KEY(PayerAccount) REFERENCES account(ID),
	FOREIGN KEY(PayeeAccount) REFERENCES account(ID),
	FOREIGN KEY(PaymentID) REFERENCES payment_method(ID),
	FOREIGN KEY(CategoryID) REFERENCES category(ID)
);
INSERT INTO bill (PayerAccount, PayeeAccount, Amount) VALUES (1, 2, 10), (1, 2, 20), (1, 2, 30), (1, 2, 40), (1, 2, 50), (1, 2, 60);

CREATE TABLE transaction (
	ID INT AUTO_INCREMENT,
	BillID INT,
	DatePayed DATE,
	AmountPayed double,
	PaymentID INT,
	PRIMARY KEY(ID),
	FOREIGN KEY(BillID) REFERENCES bill(ID),
	FOREIGN KEY(PaymentID) REFERENCES payment_method(ID)
);
