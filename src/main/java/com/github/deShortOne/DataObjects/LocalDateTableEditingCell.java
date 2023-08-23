package com.github.deShortOne.DataObjects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.github.deShortOne.Bills.BillInfo;

import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.TextFieldTableCell;

public class LocalDateTableEditingCell extends TextFieldTableCell<BillInfo, LocalDate> {
	DatePicker datePicker;

	@Override
	public void startEdit() {
		if (!isEmpty()) {
			createDatePicker();
			super.startEdit();
			setText(null);
			setGraphic(datePicker);
		}
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();

		setText(getDate().toString());
		setGraphic(null);
	}

	@Override
	public void updateItem(LocalDate item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (datePicker != null) {
					datePicker.setValue(getDate());
				}
				setText(null);
				setGraphic(datePicker);
			} else {
				setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
				setGraphic(null);
			}
		}
	}

	private void createDatePicker() {
		datePicker = new DatePicker(getDate());
		datePicker.setOnAction((e) -> {
			commitEdit(datePicker.getValue());
		});
	}

	private LocalDate getDate() {
		return getItem() == null ? LocalDate.now() : getItem();
	}
}
