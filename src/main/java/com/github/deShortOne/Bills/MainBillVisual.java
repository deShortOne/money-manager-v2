package com.github.deShortOne.Bills;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.deShortOne.DataObjects.Account;
import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.DataObjects.DataObjects;
import com.github.deShortOne.DataObjects.MoneyEditingCell;
import com.github.deShortOne.DataObjects.Payment;
import com.github.deShortOne.DataObjects.TableEditingCell;
import com.github.deShortOne.Recurrence.FrequencyType;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainBillVisual {

	public static Pane getVisuals() {
		Label title = new Label("Upcoming bills");

		// in future decide if {@code i -> new PropertyValueFactory("payeeAccount")} would be better
		TableView<BillInfo> table = new TableView<>();
		TableColumn<BillInfo, Account> payeeCol = new TableColumn<>("Payee");
		payeeCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getPayeeAccount()));
		payeeCol.setCellFactory(c -> new BillComboBoxEditingCell<>(DataObjects.getAllAccounts()));
		payeeCol.setEditable(true);
		payeeCol.setOnEditCommit(event -> {
			BillInfo row = event.getRowValue();
			Account account = event.getNewValue();
			row.setPayeeAccount(account);
		});
		
		TableColumn<BillInfo, Double> amountCol = new TableColumn<>("Amount");
		amountCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getAmount()));
		amountCol.setCellFactory(c -> new MoneyEditingCell());
		amountCol.setEditable(true);
		amountCol.setOnEditCommit(event -> {
			BillInfo row = event.getRowValue();
			double amount = event.getNewValue();
			row.setAmount(amount);
		});
		
		TableColumn<BillInfo, LocalDate> dueDateCol = new TableColumn<>("Due Date");
		dueDateCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getDueDate()));
		dueDateCol.setCellFactory(c -> new TableEditingCell());
		dueDateCol.setEditable(false);
		
		TableColumn<BillInfo, FrequencyType> frequencyCol = new TableColumn<>("Frequency");
		frequencyCol
			.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getFrequency().getFrequencyType()));
		frequencyCol.setCellFactory(
				c -> new BillComboBoxEditingCell<>(new ArrayList<FrequencyType>(Arrays.asList(FrequencyType.values()))));
		frequencyCol.setEditable(true);
		
		TableColumn<BillInfo, Payment> paymentMethodCol = new TableColumn<>("Payment Method");
		paymentMethodCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getPaymentMethod()));
		paymentMethodCol.setCellFactory(c -> new BillComboBoxEditingCell<>(DataObjects.getAllPaymentMethods()));
		paymentMethodCol.setEditable(true);
		paymentMethodCol.setOnEditCommit(event -> {
			BillInfo row = event.getRowValue();
			Payment payment = event.getNewValue();
			row.setPaymentMethod(payment);
		});

		TableColumn<BillInfo, Account> payerCol = new TableColumn<>("Account");
		payerCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getPayerAccount()));
		payerCol.setCellFactory(c -> new BillComboBoxEditingCell<>(DataObjects.getAllAccounts()));
		payerCol.setEditable(true);
		payerCol.setOnEditCommit(event -> {
			BillInfo row = event.getRowValue();
			Account account = event.getNewValue();
			row.setPayeeAccount(account);
		});

		TableColumn<BillInfo, LocalDate> lastPaidCol = new TableColumn<>("Last Paid");
		lastPaidCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getDueDate()));
		lastPaidCol.setCellFactory(c -> new TableEditingCell());
		lastPaidCol.setEditable(false);

		TableColumn<BillInfo, Category> categoryCol = new TableColumn<>("Category");
		categoryCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getCategory()));
		categoryCol.setCellFactory(c -> new BillComboBoxEditingCell<>(DataObjects.getAllCategories()));
		categoryCol.setEditable(true);
		categoryCol.setOnEditCommit(event -> {
			BillInfo row = event.getRowValue();
			Category category = event.getNewValue();
			row.setCategory(category);
		});

		table.getColumns().add(payeeCol);
		table.getColumns().add(amountCol);
		table.getColumns().add(dueDateCol);
		table.getColumns().add(frequencyCol);
		table.getColumns().add(paymentMethodCol);
		table.getColumns().add(payerCol);
		table.getColumns().add(lastPaidCol);
		table.getColumns().add(categoryCol);
		table.setItems(FXCollections.observableArrayList(DataHandler.getBills()));
		table.setEditable(true);

		VBox vbox = new VBox();
		vbox.getChildren().addAll(title, table);

		return vbox;
	}
}
