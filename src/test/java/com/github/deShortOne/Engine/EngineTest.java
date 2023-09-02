package com.github.deShortOne.Engine;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import com.github.deShortOne.Bills.BillInfo;

public class EngineTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(MoneyManager.getScene());
        stage.show();
        stage.toFront();
        this.stage = stage;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void addARecurringBill() {
        clickOn("#billTab");
        TabPane tp = (TabPane) stage.getScene().lookup("#tabPane");
        Tab tab = tp.getTabs().get(1);
        TableView<BillInfo> table = (TableView<BillInfo>) tab.getContent().lookup("#table");
        int prev = table.getItems().size();

        clickOn("#newBill");
        clickOn("#payeeId").type(KeyCode.DOWN).type(KeyCode.ENTER);
        clickOn("#payerId").type(KeyCode.DOWN).type(KeyCode.ENTER);
        clickOn("#paymentMethodId").type(KeyCode.DOWN).type(KeyCode.ENTER);
        clickOn("#amountFieldId").push(KeyCode.CONTROL, KeyCode.A).write("40");
        clickOn("#categoryId").type(KeyCode.DOWN).type(KeyCode.ENTER);
        clickOn("#nextPaymentId").write("01/01/2023");
        clickOn("#frequencyId").type(KeyCode.DOWN).type(KeyCode.ENTER);
        clickOn("#save");
        
        table = (TableView<BillInfo>) tab.getContent().lookup("#table");
        int next = table.getItems().size();

        assertTrue(prev + 1 == next);
    }
}