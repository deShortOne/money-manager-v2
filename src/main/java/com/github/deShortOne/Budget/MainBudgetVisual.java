package com.github.deShortOne.Budget;

import java.util.ArrayList;

import com.github.deShortOne.DataObjects.MoneyEditingTreeTableCell;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainBudgetVisual {

	private static ArrayList<BudgetGroup> budgetGroupList = DataHandler.getCat();

	private static TreeTableView<BillDataValue> table;
	private static TreeItem<BillDataValue> budgetsAndCategories = new TreeItem<>();

	private static VBox box = new VBox();

	static {
		Text title = new Text("Budget");

		table = new TreeTableView<>();
		TreeTableColumn<BillDataValue, String> bgAndCategories = new TreeTableColumn<>("Budget groups and categories");
		bgAndCategories.setCellValueFactory(new TreeItemPropertyValueFactory<>("tableCellValue"));
		// i -> new SimpleObjectProperty<>(i.getValue().getValue().getPlanned()) instead
		// of TreeItemPropertyValueFactory

		TreeTableColumn<BillDataValue, Double> planned = new TreeTableColumn<>("Planned");
		planned.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getValue().getPlanned()));
		planned.setCellFactory(c -> {
			MoneyEditingTreeTableCell<BillDataValue> cell = new MoneyEditingTreeTableCell<>() {
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
			table.refresh();
		});

		TreeTableColumn<BillDataValue, Double> actual = new TreeTableColumn<>("Actual");
		actual.setCellValueFactory(new TreeItemPropertyValueFactory<>("actual"));

		TreeTableColumn<BillDataValue, Double> difference = new TreeTableColumn<>("Difference");
		difference.setCellValueFactory(new TreeItemPropertyValueFactory<>("difference"));

		table.getColumns().add(bgAndCategories);
		table.getColumns().add(planned);
		table.getColumns().add(actual);
		table.getColumns().add(difference);

		for (BudgetGroup bg : budgetGroupList) {
			TreeItem<BillDataValue> budgetGroup = new TreeItem<>(bg);
			for (BudgetCategory bc : bg.getCategoryList()) {
				budgetGroup.getChildren().add(new TreeItem<>(bc));
			}
			budgetsAndCategories.getChildren().add(budgetGroup);
		}

		table.setRoot(budgetsAndCategories);
		table.setShowRoot(false);
		table.setEditable(true);

		box.getChildren().addAll(title, table);
	}

	public static Pane getVisuals() {
		return box;
	}
}
