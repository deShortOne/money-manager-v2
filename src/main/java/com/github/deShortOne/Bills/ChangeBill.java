package com.github.deShortOne.Bills;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.deShortOne.DataObjects.Account;
import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.DataObjects.DataObjects;
import com.github.deShortOne.DataObjects.Payment;
import com.github.deShortOne.Recurrence.FrequencyType;
import com.github.deShortOne.Recurrence.Recurrence;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
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

public class ChangeBill {

	private static Stage changeBillStage;

	private static Label title = new Label();

	private static VBox root;

	private static Text payee;
	private static ComboBox<Account> payToAccount;
	private static Text payer;
	private static ComboBox<Account> payFromAccount;
	private static Text paymentMethod;
	private static ComboBox<Payment> paymentMethodCombo;
	private static Text amount;
	private static TextField amountField;
	private static Text categoryText;
	private static ComboBox<Category> categoryCombo;
	private static Text nextPaymentDateText;
	private static DatePicker nextPaymentDate;
	private static Text frequencyText;
	private static ComboBox<FrequencyType> frequency;

	private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

	private static boolean isSuccess;

	static {
		payee = new Text("Pay to: ");
		payToAccount = BillComboBox.createComboBox(DataObjects.getAllAccounts());
		Label accountNumber = new Label("Account number goes here");

		payer = new Text("Pay from: ");
		payFromAccount = BillComboBox.createComboBox(DataObjects.getAllAccounts());

		paymentMethod = new Text("Payment method: ");
		paymentMethodCombo = BillComboBox.createComboBox(DataObjects.getAllPaymentMethods());

		amount = new Text("Amount: ");
		amountField = new TextField();
		amountField.setEditable(true);
		StringConverter<Double> converter = new DoubleStringConverter() {
			@Override
			public String toString(Double object) {
				StringBuilder output = new StringBuilder();
				if (object < 0) {
					output.append("-");
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

		categoryText = new Text("Category: ");
		categoryCombo = BillComboBox.createComboBox(DataObjects.getAllCategories());

		nextPaymentDateText = new Text("Next payment date: ");
		nextPaymentDate = new DatePicker(LocalDate.now());
		nextPaymentDate.setDayCellFactory(d -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				setDisable(item.isBefore(LocalDate.now()));
			}
		});

		frequencyText = new Text("Frequency: ");
		frequency = BillComboBox.createComboBox(new ArrayList<>(Arrays.asList(FrequencyType.values())));

		GridPane gp = new GridPane();
		gp.add(payee, 0, 0);
		gp.add(payToAccount, 1, 0);
		gp.add(accountNumber, 2, 0);

		gp.add(payer, 0, 1);
		gp.add(payFromAccount, 1, 1);

		gp.add(paymentMethod, 0, 2);
		gp.add(paymentMethodCombo, 1, 2);

		gp.add(amount, 0, 3);
		gp.add(amountField, 1, 3);

		gp.add(categoryText, 0, 4);
		gp.add(categoryCombo, 1, 4, 2, 1);

		gp.add(nextPaymentDateText, 2, 1);
		gp.add(nextPaymentDate, 3, 1);

		gp.add(frequencyText, 2, 2);
		gp.add(frequency, 3, 2);

		Button save = new Button("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (allNecessaryFieldsDone()) {
					Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
					s.close();
				}
			}
		});
		Button closeWithoutSaving = new Button("Close without saving");

		HBox buttons = new HBox();
		buttons.getChildren().addAll(save, closeWithoutSaving);

		root = new VBox(title, gp, buttons);
		Scene s = new Scene(root);

		changeBillStage = new Stage();
		changeBillStage.setScene(s);
	}

	public static void getVisuals(BillInfo bi) {
		title.setText(String.format("Edit your %s bills", bi.getPayeeAccount().getAccountName()));
		payToAccount.getSelectionModel().select(bi.getPayerAccount());
		payFromAccount.getSelectionModel().select(bi.getPayeeAccount());
		amountField.setText(currencyFormat.format(bi.getAmount()));
		categoryCombo.getSelectionModel().select(bi.getCategory());
		paymentMethodCombo.getSelectionModel().select(bi.getPaymentMethod());
		frequency.getSelectionModel().select(bi.getFrequency().getFrequencyType());
		nextPaymentDate.setValue(bi.getDueDate());

		changeBillStage.showAndWait();

		if (isSuccess) {
			bi.updateBill(payToAccount.getValue(), payFromAccount.getValue(),
					Double.parseDouble(amountField.getText().replaceAll("£*,*", "")), categoryCombo.getValue(),
					paymentMethodCombo.getValue(), frequency.getValue(), nextPaymentDate.getValue(), null);
		}
	}

	public static BillInfo getVisuals() {
		title.setText("New bill");
		payToAccount.getSelectionModel().select(null);
		payFromAccount.getSelectionModel().select(null);
		amountField.setText("");
		categoryCombo.getSelectionModel().select(null);
		paymentMethodCombo.getSelectionModel().select(null);
		frequency.getSelectionModel().select(null);
		nextPaymentDate.setValue(null);

		changeBillStage.showAndWait();

		if (!isSuccess) {
			return null;
		}

		Recurrence r = new Recurrence(frequency.getValue(), nextPaymentDate.getValue(), null);
		return DataHandler.addNewBill(payToAccount.getValue(), payFromAccount.getValue(),
				Double.parseDouble(amountField.getText().replaceAll("£*,*", "")), r, categoryCombo.getValue(),
				paymentMethodCombo.getValue());
	}

	private static boolean allNecessaryFieldsDone() {
		isSuccess = true;
		if (payToAccount.getValue() == null) {
			payee.setFill(Color.RED);
			isSuccess = false;
		} else {
			payee.setFill(Color.BLACK);
		}
		if (payFromAccount.getValue() == null) {
			payer.setFill(Color.RED);
			isSuccess = false;
		} else {
			payer.setFill(Color.BLACK);
		}
		if (paymentMethodCombo.getValue() == null) {
			paymentMethod.setFill(Color.RED);
			isSuccess = false;
		} else {
			paymentMethod.setFill(Color.BLACK);
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
		if (nextPaymentDate.getValue() == null) {
			nextPaymentDateText.setFill(Color.RED);
			isSuccess = false;
		} else {
			nextPaymentDateText.setFill(Color.BLACK);
		}
		if (frequency.getValue() == null) {
			frequencyText.setFill(Color.RED);
			isSuccess = false;
		} else {
			frequencyText.setFill(Color.BLACK);
		}

		return isSuccess;
	}
}
