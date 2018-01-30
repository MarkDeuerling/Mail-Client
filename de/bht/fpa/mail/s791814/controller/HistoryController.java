/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791814.controller;

import de.bht.fpa.mail.s791814.model.datas.TempData;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Mark Deuerling
 */
public class HistoryController implements Initializable {

    @FXML
    private ListView<String> listView;

    @FXML
    protected Button cancelButton;

    @FXML
    protected Button okButton;

    /**
     * The connected root conroller.
     */
    protected Controller controller;
    
    /**
     * The List to put the items on the tableView.
     */
    protected ObservableList<String> item;

    HistoryController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        item = FXCollections.observableArrayList();                
        
        if (TempData.getTempData().getData().size() > 0) {
            okButton.setDisable(false);             
            
            TempData.getTempData().getData().stream().forEach((path) -> {
                item.add(path);
            });
            
        } else {
            okButton.setDisable(true);
            item.add("no selected path yet");
        }

        listView.setItems(item);

        cancelButton.setOnAction(e -> {
            unrenderWindow();
        });

        okButton.setOnAction(e -> {  
            String selectedItem = listView.getSelectionModel().getSelectedItem();
//            controller.setFile(new File(selectedItem));
            controller.appLogic.changeDirectory(new File(selectedItem));
            controller.editRootItem();
            controller.createHierarchy();
            controller.refreshRootOnTreeView();
            controller.tableView.getItems().clear();
            unrenderWindow();
        });
    }

    /**
     * Hide the history view by clicking on the cancel button.
     */
    protected void unrenderWindow() {
        Scene scene = cancelButton.getScene();
        Window window = scene.getWindow();
        window.hide();
    }

}
