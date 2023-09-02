package com.github.deShortOne.Budget;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.github.deShortOne.DataObjects.Account;
import com.github.deShortOne.DataObjects.BaseDataValue;
import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.DataObjects.DataObjects;

public class Transaction implements BaseDataValue, MoneyTableCell {
    private Account payerAccount;
    private Account payeeAccount;
    private LocalDate datePaid;
    private double amountPaid;
    private Category category;

    public Transaction(ResultSet rs) throws SQLException {
        this.payerAccount = DataObjects.getAccount(rs.getInt("PayerAccount"));
        this.payeeAccount = DataObjects.getAccount(rs.getInt("PayeeAccount"));
        this.datePaid = rs.getDate("DatePaid").toLocalDate();
        this.amountPaid = rs.getDouble("AmountPaid");
        this.category = DataObjects.getCategory(rs.getInt("CategoryID"));
    }

    public Account getPayerAccount() {
        return payerAccount;
    }

    public Account getPayeeAccount() {
        return payeeAccount;
    }

    public LocalDate getDatePaid() {
        return datePaid;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String getTableCellValue() {
        return "Help me! HELP ME!!";
    }
}