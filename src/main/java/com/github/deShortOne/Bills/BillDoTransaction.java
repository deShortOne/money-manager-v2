package com.github.deShortOne.Bills;

import java.text.NumberFormat;

import com.github.deShortOne.DataObjects.Account;
import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.DataObjects.DataObjects;
import com.github.deShortOne.DataObjects.Payment;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

public class BillDoTransaction {

	private static Stage doBillTransactionStage;

	private static Label title = new Label("Record payment in account register");

	private static VBox root;

	private static Text userAccount = new Text("Account: ");
	private static ComboBox<Account> userAccountCombo = BillComboBox.createComboBox(DataObjects.getAllAccounts());
	private static Text toFromAccount = new Text("");
	private static ComboBox<Account> toFromAccountCombo = BillComboBox.createComboBox(DataObjects.getAllAccounts());
	private static Text amount = new Text("Amount: ");
	private static TextField amountField = new TextField();
	private static Text categoryText = new Text("Category: ");
	private static ComboBox<Category> categoryCombo = BillComboBox.createComboBox(DataObjects.getAllCategories());
	private static Text paymentDateText = new Text("Date: ");
	private static DatePicker paymentDate = new DatePicker();
	private static Text paymentMethodText = new Text("Payment: ");
	private static ComboBox<Payment> paymentMethodCombo = BillComboBox
		.createComboBox(DataObjects.getAllPaymentMethods());

	private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

	private static boolean isSuccess;

	static {
		StringConverter<Double> converter = new DoubleStringConverter() {
			@Override
			public String toString(Double object) {
				StringBuilder output = new StringBuilder();
				if (object < 0) {
					object *= -1;
				}
				return output.append(currencyFormat.format(object)).toString();
			}

			@Override
			public Double fromString(String s) {
				s = s.replace("£", "");
				if (s.isEmpty())
					return 0.0;
				return super.fromString(s);
			}
		};
		TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0);
		amountField.setTextFormatter(textFormatter);

		GridPane gp = new GridPane();
		gp.add(userAccount, 0, 0);
		gp.add(userAccountCombo, 1, 0);

		gp.add(toFromAccount, 0, 1);
		gp.add(toFromAccountCombo, 1, 1);

		gp.add(categoryText, 0, 2);
		gp.add(categoryCombo, 1, 2);

		gp.add(paymentDateText, 2, 0);
		gp.add(paymentDate, 3, 0);

		gp.add(amount, 2, 1);
		gp.add(amountField, 3, 1);

		gp.add(paymentMethodText, 2, 2);
		gp.add(paymentMethodCombo, 3, 2);

		Button save = new Button("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				doBillTransactionStage.close();
			}
		});
		Button closeWithoutSaving = new Button("Cancel");

		HBox buttons = new HBox();
		buttons.getChildren().addAll(save, closeWithoutSaving);

		root = new VBox(title, gp, buttons);
		Scene s = new Scene(root);

		doBillTransactionStage = new Stage();
		doBillTransactionStage.setScene(s);
	}

	public static void withdrawMoney(BillInfo bi) {
		userAccount.setText("Account: ");
		toFromAccount.setText("Pay to: ");
		setFields(bi);

		doBillTransactionStage.showAndWait();
		if (allNecessaryFieldsDone()) {
			double amount = Double.parseDouble(amountField.getText().replaceAll("£*,*", ""));
			if (amount > 0) {
				amount *= -1;
			}
			bi.doTransaction(paymentDate.getValue(), amount, categoryCombo.getValue(), paymentMethodCombo.getValue());
		}
	}

	public static void depositMoney(BillInfo bi) {
		userAccount.setText("Account: ");
		toFromAccount.setText("Pay to: ");
		setFields(bi);

		doBillTransactionStage.showAndWait();
		if (allNecessaryFieldsDone()) {
			double amount = Double.parseDouble(amountField.getText().replaceAll("£*,*", ""));
			if (amount < 0) {
				amount *= -1;
			}
			bi.doTransaction(paymentDate.getValue(), amount, categoryCombo.getValue(), paymentMethodCombo.getValue());
		}
	}

	private static void setFields(BillInfo bi) {
		isSuccess = false;
		toFromAccountCombo.getSelectionModel().select(bi.getPayerAccount());
		userAccountCombo.getSelectionModel().select(bi.getPayeeAccount());

		categoryCombo.getSelectionModel().select(bi.getCategory());
		amountField.setText(currencyFormat.format(bi.getAmount()));
		paymentDate.setValue(bi.getDueDate());
	}

	private static boolean allNecessaryFieldsDone() {
		isSuccess = true;
		if (userAccountCombo.getValue() == null) {
			userAccount.setFill(Color.RED);
			isSuccess = false;
		} else {
			userAccount.setFill(Color.BLACK);
		}
		if (toFromAccountCombo.getValue() == null) {
			toFromAccount.setFill(Color.RED);
			isSuccess = false;
		} else {
			toFromAccount.setFill(Color.BLACK);
		}
		if (amountField.getText() == "") {
			amount.setFill(Color.RED);
			isSuccess = false;
		} else {
			amount.setFill(Color.BLACK);
		}
		if (categoryCombo.getValue() == null) {
			categoryText.setFill(Color.RED);
			isSuccess = false;
		} else {
			categoryText.setFill(Color.BLACK);
		}
		if (paymentDate.getValue() == null) {
			paymentDateText.setFill(Color.RED);
			isSuccess = false;
		} else {
			paymentDateText.setFill(Color.BLACK);
		}
		if (paymentMethodCombo.getValue() == null) {
			paymentMethodText.setFill(Color.RED);
			isSuccess = false;
		} else {
			paymentMethodText.setFill(Color.BLACK);
		}
		return isSuccess;
	}
}
