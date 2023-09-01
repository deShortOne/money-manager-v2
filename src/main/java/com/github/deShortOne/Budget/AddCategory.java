package com.github.deShortOne.Budget;

import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.DataObjects.DataObjects;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddCategory {

	private static Stage addCategoryStage = new Stage();
	
	private static Text budgetGroupName = new Text("Budget Group: ");
	private static ComboBox<BudgetGroup> budgetGroupComboBox = BudgetComboBox
		.createComboBox(MainBudgetVisual.budgetGroupList);
	private static Text categoryName = new Text("Category:");
	private static ComboBox<Category> categoryComboBox = BudgetComboBox.createComboBox(DataObjects.getAllCategories());

	private static boolean isSuccess;
	
	static {
		Button save = new Button("Save");
		save.setOnAction(i -> {
			if (allNecessaryFieldsDone()) {
				addCategoryStage.close();
			}
		});
		
		GridPane gp = new GridPane();
		gp.add(budgetGroupName, 0, 0);
		gp.add(budgetGroupComboBox, 1, 0);
		
		gp.add(categoryName, 0, 1);
		gp.add(categoryComboBox, 1, 1);

		VBox root = new VBox();
		root.getChildren().addAll(gp);
		
		Scene s = new Scene(root);
		addCategoryStage.setScene(s);
	}
	
	public static void getVisuals() {
		budgetGroupComboBox.getSelectionModel().select(null);
		categoryComboBox.getSelectionModel().select(null);
		
		addCategoryStage.showAndWait();
		
		if (isSuccess) {
			
		}
	}
	
	private static boolean allNecessaryFieldsDone() {
		isSuccess = true;
		isSuccess &= checkField(budgetGroupComboBox, budgetGroupName);
		isSuccess &= checkField(categoryComboBox, categoryName);
		return isSuccess;
	}
	
	private static boolean checkField(ComboBox<?> cb, Text text) {
		if (cb.getValue() == null) {
			text.setFill(Color.RED);
			return false;
		} else {
			text.setFill(Color.BLACK);
		}
		return true;
	}
}
