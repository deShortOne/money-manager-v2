package com.github.deShortOne.Bills;

import java.util.ArrayList;

import com.github.deShortOne.DataObjects.TableCellDataValue;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.StringConverter;

public class BillComboBoxEditingCell<T extends TableCellDataValue> extends ComboBoxTableCell<BillInfo, T> {
	private ComboBox<T> comboBox = new ComboBox<>();

	public BillComboBoxEditingCell(ArrayList<T> list) {
		createComboBox(list);
	}

	@Override
	public void startEdit() {
		if (!isEmpty()) {
			super.startEdit();
			setText(null);
			setGraphic(comboBox);
			comboBox.valueProperty().set(getItem());
		}
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getItem().getTableCellValue());
		setGraphic(null);
	}

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (isEditing()) {
			comboBox.setValue(getItem());
			setGraphic(comboBox);
		} else {
			setText(empty ? null : getItem().getTableCellValue());
			setGraphic(null);
		}
	}

	private void createComboBox(ArrayList<T> dataList) {
		comboBox.getItems().addAll(dataList);
		comboBox.setCellFactory(c -> {
			return new ListCell<T>() {
				@Override
				protected void updateItem(T item, boolean empty) {
					super.updateItem(item, empty);
					setText(item == null || empty ? null : item.getTableCellValue());
				}
			};
		});
		comboBox.setConverter(new StringConverter<T>() {
			@Override
			public String toString(T data) {
				return data == null ? null : data.getTableCellValue();
			}

			@Override
			public T fromString(String dataName) {
				return dataList.stream().filter(i -> i.getTableCellValue().equals(dataName)).findFirst().get();
			}
		});
		comboBox.setOnAction(e -> {
			commitEdit(comboBox.getSelectionModel().getSelectedItem());
		});
	}
}
