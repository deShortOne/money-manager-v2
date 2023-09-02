package com.github.deShortOne.Budget;

import java.time.LocalDate;
import java.util.ArrayList;

import com.github.deShortOne.DataObjects.Account;
import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.DataObjects.DataObjects;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShowTransactions {

	private static Stage stage = new Stage();
    
	private static TableView<Transaction> table = new TableView<>();

    static {
        TableColumn<Transaction, Category> categoryCol = new TableColumn<>("Category");
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		categoryCol.setCellFactory(c -> new BudgetComboBoxEditingCell<>(DataObjects.getAllCategories()));

        TableColumn<Transaction, Account> payerCol = new TableColumn<>("Payer");
		payerCol.setCellValueFactory(new PropertyValueFactory<>("payerAccount"));
		payerCol.setCellFactory(c -> new BudgetComboBoxEditingCell<>(DataObjects.getAllAccounts()));

        TableColumn<Transaction, Account> payeeCol = new TableColumn<>("Payee");
		payeeCol.setCellValueFactory(new PropertyValueFactory<>("payeeAccount"));
		payeeCol.setCellFactory(c -> new BudgetComboBoxEditingCell<>(DataObjects.getAllAccounts()));
        
        TableColumn<Transaction, LocalDate> lastPaidCol = new TableColumn<>("Date Paid");
		lastPaidCol.setCellValueFactory(new PropertyValueFactory<>("datePaid"));
		lastPaidCol.setCellFactory(c -> new LocalDateTableEditingCell());

		TableColumn<Transaction, Double> amountPaidCol = new TableColumn<>("Amount Paid");
		amountPaidCol.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
		amountPaidCol.setCellFactory(c -> new MoneyEditingTableCell<>());

		table.getColumns().add(categoryCol);
		table.getColumns().add(payerCol);
		table.getColumns().add(payeeCol);
		table.getColumns().add(lastPaidCol);
		table.getColumns().add(amountPaidCol);

		VBox vbox = new VBox();
		vbox.getChildren().addAll(table);
		Scene s = new Scene(vbox);

		stage.setScene(s);
    }

	public static void showTransactions(ArrayList<Transaction> transactions) {
		table.setItems(FXCollections.observableArrayList(transactions));
		stage.show();
	}
}