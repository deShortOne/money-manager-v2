package com.github.deShortOne.Budget;

import java.util.ArrayList;

import com.github.deShortOne.DataObjects.MoneyEditingTableCell;
import com.github.deShortOne.DataObjects.MoneyEditingTreeTableCell;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class MainBudgetVisual {

	public static ArrayList<BudgetGroup> budgetGroupList = DataHandler.getCat();

	private static TreeTableView<BudgetDataValue> mainTable = new TreeTableView<>();
	private static TreeItem<BudgetDataValue> budgetsAndCategories = new TreeItem<>();

	private static TableView<SumData> sumTable = new TableView<>();
	private static SumData totalIncome = new SumData("Total Income:", -1, -1);
	private static SumData totalExpense = new SumData("Total Expenses:", -1, -1);
	private static SumData remainder = new SumData("Remainder", -1, -1);

	private static VBox box = new VBox();

	static {
		Text title = new Text("Budget");

		Button viewTransactions = new Button("View Transactions");
		viewTransactions.setOnAction(i -> {
			BudgetDataValue bdv = mainTable.getSelectionModel().getSelectedItem().getValue();
			ArrayList<Transaction> transactions = DataHandler.getTransactions(bdv);
			ShowTransactions.showTransactions(transactions);
		});

		Button addCategory = new Button("Add Category");
		addCategory.setOnAction(i -> {
			ChangeCategory.addCategory();
			refreshTable();
		});
		
		Button moveCategory = new Button("Move Category");
		moveCategory.setOnAction(i -> {
			BudgetDataValue bdv = mainTable.getSelectionModel().getSelectedItem().getValue();
			if (bdv instanceof BudgetGroup) {
				return;
			}

			BudgetCategory bc = (BudgetCategory) bdv;
			ChangeCategory.categoryChangeBudgetGroup(bc);
			refreshTable();
		});

		Button removeCategory = new Button("Remove Category");
		removeCategory.setOnAction(i -> {
			BudgetDataValue bdv = mainTable.getSelectionModel().getSelectedItem().getValue();
			if (bdv instanceof BudgetGroup) {
				return;
			}

			BudgetCategory bc = (BudgetCategory) bdv;
			DataHandler.removeCategoryFromBudgetGroup(bc.getBudgetGroup(), bc.getCategory());
			refreshTable();
		});

		HBox buttons = new HBox();
		buttons.getChildren().addAll(viewTransactions, addCategory, moveCategory, removeCategory);

		TreeTableColumn<BudgetDataValue, String> bgAndCategories = new TreeTableColumn<>("Budget groups and categories");
		bgAndCategories.setCellValueFactory(new TreeItemPropertyValueFactory<>("tableCellValue"));
		// i -> new SimpleObjectProperty<>(i.getValue().getValue().getPlanned()) instead
		// of TreeItemPropertyValueFactory
		bgAndCategories.setSortable(false);

		TreeTableColumn<BudgetDataValue, Double> planned = new TreeTableColumn<>("Planned");
		planned.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getValue().getPlanned()));
		planned.setCellFactory(c -> {
			MoneyEditingTreeTableCell<BudgetDataValue> cell = new MoneyEditingTreeTableCell<>() {
				@Override
				public void startEdit() {
					if (getTableRow().getItem() instanceof BudgetGroup) {
						return;
					}
					super.startEdit();
				}
			};
			return cell;
		});
		planned.setEditable(true);
		planned.setOnEditCommit(event -> {
			BudgetCategory row = (BudgetCategory) event.getRowValue().getValue();
			row.updatePlanned(event.getNewValue());

			refreshAllTables();
		});
		planned.setSortable(false);

		TreeTableColumn<BudgetDataValue, Double> actual = new TreeTableColumn<>("Actual");
		actual.setCellValueFactory(new TreeItemPropertyValueFactory<>("actual"));
		actual.setCellFactory(c -> new MoneyEditingTreeTableCell<>());
		actual.setSortable(false);

		TreeTableColumn<BudgetDataValue, Double> difference = new TreeTableColumn<>("Difference");
		difference.setCellValueFactory(new TreeItemPropertyValueFactory<>("difference"));
		difference.setCellFactory(c -> new MoneyEditingTreeTableCell<>());
		difference.setSortable(false);

		mainTable.getColumns().add(bgAndCategories);
		mainTable.getColumns().add(planned);
		mainTable.getColumns().add(actual);
		mainTable.getColumns().add(difference);
		mainTable.setRowFactory(new Callback<TreeTableView<BudgetDataValue>, TreeTableRow<BudgetDataValue>>() {
			@Override
			public TreeTableRow<BudgetDataValue> call(TreeTableView<BudgetDataValue> param) {
				return new TreeTableRow<>() {
					@Override
					public void updateItem(BudgetDataValue item, boolean empty) {
						super.updateItem(item, empty);
						if (item instanceof BudgetGroup) {
							super.setStyle("-fx-font-weight: bold;");
						} else {
							super.setStyle("-fx-font-weight: normal;");
						}
					}
				};
			}
		});

		for (BudgetGroup bg : budgetGroupList) {
			TreeItem<BudgetDataValue> budgetGroup = new TreeItem<>(bg);
			budgetGroup.setExpanded(true);
			for (BudgetCategory bc : bg.getCategoryList()) {
				budgetGroup.getChildren().add(new TreeItem<>(bc));
			}
			budgetsAndCategories.getChildren().add(budgetGroup);
		}

		mainTable.setRoot(budgetsAndCategories);
		mainTable.setShowRoot(false);
		mainTable.setEditable(true);

		// SUMMARY
		TableColumn<SumData, String> titleCol = new TableColumn<>("Budget groups and categories");
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

		TableColumn<SumData, Double> summaryPlanned = new TableColumn<>("Planned");
		summaryPlanned.setCellValueFactory(new PropertyValueFactory<>("planned"));
		summaryPlanned.setCellFactory(c -> new MoneyEditingTableCell<>());

		TableColumn<SumData, Double> summaryActual = new TableColumn<>("Actual");
		summaryActual.setCellValueFactory(new PropertyValueFactory<>("actual"));
		summaryActual.setCellFactory(c -> new MoneyEditingTableCell<>());

		TableColumn<SumData, Double> summaryDifference = new TableColumn<>("Difference");
		summaryDifference.setCellValueFactory(new PropertyValueFactory<>("difference"));
		summaryDifference.setCellFactory(c -> new MoneyEditingTableCell<>());

		sumTable.getColumns().add(titleCol);
		sumTable.getColumns().add(summaryPlanned);
		sumTable.getColumns().add(summaryActual);
		sumTable.getColumns().add(summaryDifference);

		summaryValues();
		ObservableList<SumData> sumData = FXCollections.observableArrayList(totalIncome, totalExpense, remainder);
		sumTable.setItems(sumData);
		sumTable.setMaxHeight(98);
		
		box.getChildren().addAll(title, buttons, mainTable, sumTable);
	}

	public static Pane getVisuals() {
		return box;
	}

	private static void refreshAllTables() {
		summaryValues();

		mainTable.refresh();
		sumTable.refresh();
	}

	private static void summaryValues() {
		double plannedIncome = 0;
		double plannedExpense = 0;
		double actualIncome = 0;
		double actualExpense = 0;

		for (BudgetGroup bg : budgetGroupList) {
			if (bg.getName().equals("Income")) {
				plannedIncome += bg.getPlanned();
				actualIncome += bg.getActual();
			} else {
				plannedExpense += bg.getPlanned();
				actualExpense += bg.getActual();
			}
		}

		totalIncome.updateValue(plannedIncome, actualIncome);
		totalExpense.updateValue(plannedExpense, actualExpense);
		remainder.updateValue(plannedIncome - plannedExpense, actualIncome - actualExpense);
	}

	public static void refreshTable() {
		budgetGroupList = DataHandler.getCat();
		budgetsAndCategories.getChildren().clear();
		for (BudgetGroup bg : budgetGroupList) {
			TreeItem<BudgetDataValue> budgetGroup = new TreeItem<>(bg);
			budgetGroup.setExpanded(true);
			for (BudgetCategory bc : bg.getCategoryList()) {
				budgetGroup.getChildren().add(new TreeItem<>(bc));
			}
			budgetsAndCategories.getChildren().add(budgetGroup);
		}
		mainTable.setRoot(budgetsAndCategories);
		refreshAllTables();
	}
}
