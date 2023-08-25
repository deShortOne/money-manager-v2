package com.github.deShortOne.Bills;

import java.util.ArrayList;

import com.github.deShortOne.DataObjects.BaseDataValue;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

public class BillComboBox {

	public static <T extends BaseDataValue> ComboBox<T> createComboBox(ArrayList<T> dataList) {
		ComboBox<T> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(dataList);
		comboBox.setCellFactory(c -> {
			return new ListCell<>() {
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
		return comboBox;
	}
}
