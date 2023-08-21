DROP DATABASE IF EXISTS money;
CREATE DATABASE money;
USE money;

CREATE TABLE accounts (
	ID INT AUTO_INCREMENT,
	AccountName VARCHAR(50),
	SortCode VARCHAR(8),
	AccountNumber VARCHAR(20),
	PRIMARY KEY(ID)
);
INSERT INTO accounts (AccountName) VALUES ('Acc 1 - payer'), ('Acc 2 - payee');

CREATE TABLE categories (
	ID INT AUTO_INCREMENT,
	Name VARCHAR(50),
	IsIncome boolean,
	PRIMARY KEY(ID)
);
INSERT INTO categories (Name, IsIncome) VALUES ('Automobile', false), ('Bank Charges', false), ('Bills', false), ('Cash Withdrawal', false), ('Childcare', false), ('Clothing', false), ('Credit Card Payments', false), ('Dining Out', false), ('Education', false), ('Entertainment', false), ('Fees', false), ('Gifts', false), ('Groceries', false), ('Healthcare', false), ('Hobbies/Leisure', false), ('Home Improvement', false), ('Household', false), ('Insurance', false), ('Job Expense', false), ('Loan', false), ('Miscellaneous', false), ('Mortgage/Rent', false), ('Personal Care', false), ('Pet Care', false), ('Taxes', false), ('Travel/Vacation', false), ('Utilities', false), ('Investment Income', true), ('Other Income', true), ('Wages & Salary', true);
-- will later add more categories and add subcategory ie for Automobile can have fuel and maintenance

CREATE TABLE payment_methods (
	ID INT AUTO_INCREMENT,
	Name VARCHAR(50),
	PRIMARY KEY(ID)
);
INSERT INTO payment_methods (Name) VALUES ('Cash'), ('Credit Card'), ('Debit Card'), ('Direct Debit'), ('Write Check'), ('Other');

CREATE TABLE bills (
	ID INT AUTO_INCREMENT,
	PayerAccount INT,
	PayeeAccount INT,
	Amount DOUBLE,
	Frequency VARCHAR(50),
	PaymentID INT,
	CategoryID INT,
	PRIMARY KEY(ID),
	FOREIGN KEY(PayerAccount) REFERENCES accounts(ID),
	FOREIGN KEY(PayeeAccount) REFERENCES accounts(ID),
	FOREIGN KEY(PaymentID) REFERENCES payment_methods(ID),
	FOREIGN KEY(CategoryID) REFERENCES categories(ID)
);
INSERT INTO bills (PayerAccount, PayeeAccount, Amount, PaymentID, CategoryID, Frequency) VALUES 
(1, 2, 10, 1, 1, '1;2023-04-10;;2027-04-10;;EOF'), 
(1, 2, 20, 1, 1, '1;2023-04-10;;2027-04-10;;EOF'), 
(1, 2, 30, 1, 1, '1;2023-04-10;;2027-04-10;;EOF'), 
(1, 2, 40, 1, 1, '1;2023-04-10;;2027-04-10;;EOF'), 
(1, 2, 50, 1, 1, '1;2023-04-10;;2027-04-10;;EOF'), 
(1, 2, 60, 1, 1, '1;2023-04-10;;2027-04-10;;EOF');

CREATE TABLE transactions (
	ID INT AUTO_INCREMENT,
	BillID INT,
	DatePaid DATE,
	AmountPaid double,
	PaymentID INT,
	PRIMARY KEY(ID),
	FOREIGN KEY(BillID) REFERENCES bills(ID),
	FOREIGN KEY(PaymentID) REFERENCES payment_methods(ID)
);
INSERT INTO transactions (BillID, DatePaid, AmountPaid, PaymentID) VALUES 
(1, str_to_date('2023-05-10', '%Y-%m-%d'), 11, 1), 
(1, str_to_date('2023-05-11', '%Y-%m-%d'), 9, 1), 
(1, str_to_date('2023-05-12', '%Y-%m-%d'), 10, 1), 
(2, str_to_date('2023-05-10', '%Y-%m-%d'), 20, 1), 
(2, str_to_date('2023-05-17', '%Y-%m-%d'), 20, 1);
