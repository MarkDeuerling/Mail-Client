package de.bht.fpa.mail.s791814.controller;

import de.bht.fpa.mail.s791814.model.datas.Account;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Mark Deuerling
 */
public class LoginController implements Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField hostTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Label errorLabel;

    protected Controller controller;
    protected String currentName;
    
    public LoginController(Controller controller, String currentName){
        this.controller = controller;
        this.currentName = currentName;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cancelButton.setOnAction(e -> unrenderWindow());
        
        if(currentName == null){
            controllerAsNewAccount();
        }else
            controllerAsEditAccount();
        
    }    
    
    protected void unrenderWindow(){
        Scene scene = cancelButton.getScene();
        Window window = scene.getWindow();
        window.hide();
    }
    
    protected void controllerAsNewAccount(){
        
        saveButton.setText("Save");
        saveButton.setOnAction((ActionEvent e) ->{            
            boolean isUsed = false;
            for(String acc : controller.appLogic.getAllAccounts()){
                if(nameTextField.getText().equals(acc)){
                    isUsed = true;
                    break;
                }
                isUsed = false;
            }
            
            if(nameTextField.getText().equals("")          ||
                    hostTextField.getText().equals("")     ||
                    passwordTextField.getText().equals("") ||
                    usernameTextField.getText().equals(""))
            {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("All textfields must contain data!");
                
                
            }else if(isUsed){
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Name is already used!");
            }else{
//                controller.appLogic = new FacadeMail(); // ---- delegate
                controller.appLogic.saveAccount(
                        new Account(nameTextField.getText(), 
                                hostTextField.getText(), 
                                usernameTextField.getText(), 
                                passwordTextField.getText()));
                
                unrenderWindow();
            }
            
            controller.openAccountMenu.getItems().remove(0, controller.openAccountMenu.getItems().size());
            controller.editAccountMenu.getItems().remove(0, controller.editAccountMenu.getItems().size());
            controller.configOpenAccount();
        });
    }
    
    protected void controllerAsEditAccount(){
        Account acc = controller.appLogic.getAccount(currentName);
        
        nameTextField.setText(acc.getName());
        hostTextField.setText(acc.getHost());
        passwordTextField.setText(acc.getPassword());
        usernameTextField.setText(acc.getUsername());
        
        saveButton.setText("Update");
        saveButton.setOnAction((ActionEvent e) -> {
            if(nameTextField.getText().equals("")          ||
                    hostTextField.getText().equals("")     ||
                    passwordTextField.getText().equals("") ||
                    usernameTextField.getText().equals(""))
            {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("All textfields must contain data!");
                
                
            }else{
                controller.appLogic.updateAccount(
                        new Account(nameTextField.getText(), 
                                hostTextField.getText(), 
                                usernameTextField.getText(), 
                                passwordTextField.getText()));
                
                unrenderWindow();
            }
            
            controller.openAccountMenu.getItems().remove(0, controller.openAccountMenu.getItems().size());
            controller.editAccountMenu.getItems().remove(0, controller.editAccountMenu.getItems().size());
            controller.configOpenAccount();
        });
    }
    
}
