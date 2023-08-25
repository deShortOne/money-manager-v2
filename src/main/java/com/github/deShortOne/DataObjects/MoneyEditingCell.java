package com.github.deShortOne.DataObjects;

import java.text.NumberFormat;

import com.github.deShortOne.Bills.BillInfo;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

// To be NOT BillInfo
public class MoneyEditingCell extends TextFieldTableCell<BillInfo, Double> {

	private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

	@Override
	public void startEdit() {
		if (getTableRow() != null && !getTableRow().isEditable()) {
			return;
		}

		StringConverter<Double> sc = new StringConverter<>() {
			@Override
			public String toString(Double object) {
				StringBuilder output = new StringBuilder();
				if (object < 0) {
					output.append("-");
					object *= -1;
				}
				return output.append(currencyFormat.format(object)).toString();
			}

			@Override
			public Double fromString(String string) {
				try {
					string = string.replace("£", "");
					return Double.parseDouble(string);
				} catch (NumberFormatException e) {
					cancelEdit();
					return Double.NaN;
				}

			}
		};
		super.setConverter(sc);
		super.startEdit();
	}

	@Override
	public void updateItem(Double item, boolean empty) {
		super.updateItem(item, empty);
		if (!empty) {
			StringBuilder output = new StringBuilder();
			if (item < 0) {
				output.append("-");
				item *= -1;
			}
			setText(output.append(currencyFormat.format(item)).toString());
		}
	}
}
