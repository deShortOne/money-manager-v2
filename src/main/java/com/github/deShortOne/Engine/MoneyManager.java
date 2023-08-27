package com.github.deShortOne.Engine;

import com.github.deShortOne.Bills.MainBillVisual;
import com.github.deShortOne.Budget.MainBudgetVisual;
import com.github.deShortOne.DataObjects.DataObjects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MoneyManager extends Application {

	public static final String MONEY_FORMAT = "£%.2f";

	@Override
	public void start(Stage primaryStage) throws Exception {
		DataObjects.getAccount(0);

		TabPane tabPane = new TabPane();

		Tab home = new Tab("Home", new Label("Put something here! Anything!"));
		Tab bills = new Tab("Bills", MainBillVisual.getVisuals());
		Tab budget = new Tab("Budget", MainBudgetVisual.getVisuals());

		tabPane.getTabs().addAll(home, bills, budget);

		VBox vbox = new VBox(tabPane);
		Scene scene = new Scene(vbox);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Budget tracker");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}
