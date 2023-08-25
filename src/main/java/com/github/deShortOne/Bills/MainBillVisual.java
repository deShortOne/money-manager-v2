package com.github.deShortOne.Bills;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.deShortOne.DataObjects.Account;
import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.DataObjects.DataObjects;
import com.github.deShortOne.DataObjects.LocalDateTableEditingCell;
import com.github.deShortOne.DataObjects.MoneyEditingCell;
import com.github.deShortOne.DataObjects.Payment;
import com.github.deShortOne.Recurrence.FrequencyType;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainBillVisual {

	private static TableView<BillInfo> table;

	public static Pane getVisuals() {
		Label title = new Label("Upcoming bills");

		// in future decide if {@code i -> new PropertyValueFactory("payeeAccount")}
		// would be better
		table = new TableView<>();
		TableColumn<BillInfo, Account> payeeCol = new TableColumn<>("Payee");
		payeeCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getPayeeAccount()));
		payeeCol.setCellFactory(c -> new BillComboBoxEditingCell<>(DataObjects.getAllAccounts()));

		TableColumn<BillInfo, Double> amountCol = new TableColumn<>("Amount");
		amountCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getAmount()));
		amountCol.setCellFactory(c -> new MoneyEditingCell());

		TableColumn<BillInfo, LocalDate> dueDateCol = new TableColumn<>("Due Date");
		dueDateCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getDueDate()));
		dueDateCol.setCellFactory(c -> new LocalDateTableEditingCell());

		TableColumn<BillInfo, FrequencyType> frequencyCol = new TableColumn<>("Frequency");
		frequencyCol
			.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getFrequency().getFrequencyType()));
		frequencyCol.setCellFactory(c -> new BillComboBoxEditingCell<>(
				new ArrayList<>(Arrays.asList(FrequencyType.values()))));

		TableColumn<BillInfo, Payment> paymentMethodCol = new TableColumn<>("Payment Method");
		paymentMethodCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getPaymentMethod()));
		paymentMethodCol.setCellFactory(c -> new BillComboBoxEditingCell<>(DataObjects.getAllPaymentMethods()));

		TableColumn<BillInfo, Account> payerCol = new TableColumn<>("Account");
		payerCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getPayerAccount()));
		payerCol.setCellFactory(c -> new BillComboBoxEditingCell<>(DataObjects.getAllAccounts()));

		TableColumn<BillInfo, LocalDate> lastPaidCol = new TableColumn<>("Last Paid");
		lastPaidCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getDueDate()));
		lastPaidCol.setCellFactory(c -> new LocalDateTableEditingCell());

		TableColumn<BillInfo, Category> categoryCol = new TableColumn<>("Category");
		categoryCol.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getCategory()));
		categoryCol.setCellFactory(c -> new BillComboBoxEditingCell<>(DataObjects.getAllCategories()));

		table.getColumns().add(payeeCol);
		table.getColumns().add(amountCol);
		table.getColumns().add(dueDateCol);
		table.getColumns().add(frequencyCol);
		table.getColumns().add(paymentMethodCol);
		table.getColumns().add(payerCol);
		table.getColumns().add(lastPaidCol);
		table.getColumns().add(categoryCol);
		table.setItems(FXCollections.observableArrayList(DataHandler.getBills()));
		// ---------

		Button enterInRegister = new Button("Enter in Register");
		Button newBill = new Button("New Bill");
		newBill.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				BillInfo bi = ChangeBill.getVisuals();
				addNewBillInfo(bi);
			}
		});
		Button editBill = new Button("Edit Bill");
		editBill.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				ChangeBill.getVisuals(table.getSelectionModel().getSelectedItem());
				refreshTable();
			}
		});
		Button skipBill = new Button("Skip Bill");
		skipBill.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				table.getSelectionModel().getSelectedItem().iterateDueDate();
				refreshTable();
			}
		});
		Button deleteBill = new Button("Delete Bill");
		deleteBill.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				table.getSelectionModel().getSelectedItem().deactivate();
				refreshTable();
			}
		});

		HBox actions = new HBox();
		actions.getChildren().addAll(enterInRegister, newBill, editBill, skipBill, deleteBill);

		VBox vbox = new VBox();
		vbox.getChildren().addAll(title, table, actions);

		return vbox;
	}

	public static void addNewBillInfo(BillInfo bi) {
		// get added to list
		table.setItems(FXCollections.observableArrayList(DataHandler.getBills()));
		refreshTable();
	}

	public static void refreshTable() {
		table.setItems(FXCollections.observableArrayList(DataHandler.getBills()));
	}
}
