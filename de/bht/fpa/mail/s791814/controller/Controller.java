package de.bht.fpa.mail.s791814.controller;

import de.bht.fpa.mail.s791814.model.applicationLogic.ApplicationLogicIF;
import de.bht.fpa.mail.s791814.model.applicationLogic.Facade;
import de.bht.fpa.mail.s791814.model.datas.Component;
import de.bht.fpa.mail.s791814.model.datas.Email;
import de.bht.fpa.mail.s791814.model.datas.Folder;
import de.bht.fpa.mail.s791814.model.datas.TempData;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Controller implements Initializable {

    @FXML
    protected TreeView<Component> treeView;

    @FXML
    protected MenuBar menuBar;

    @FXML
    protected TableView<Email> tableView;

    @FXML
    protected TextField searchField;

    @FXML
    protected Label sizeLabel;

    @FXML
    protected Label sender;

    @FXML
    protected Label subject;

    @FXML
    protected Label received;

    @FXML
    protected Label receiver;

    @FXML
    protected TextArea textArea;

    @FXML
    protected Menu openAccountMenu;
    
    @FXML 
    protected Menu editAccountMenu;

    protected final static String FOLDER = "/de/bht/fpa/mail/s791814/images/ordner.png";
    protected final static String OPEN_FOLDER = "/de/bht/fpa/mail/s791814/images/open_folder.png";
    protected final static String ROOT_DIRECTION = "user.home";
    protected final static String MENU_ITEM_OPEN = "Open";
    protected final static String MENU_ITEM_HISTORY = "History";
    protected final static String MENU_ITEM_SAVE = "Save";
    protected final static String MENU_ITEM_NEW_ACCOUNT = "New Account";
    protected final static String MENU_OPEN_ACCOUNT = "Open Account";
    protected final static String MENU_EDIT_ACCOUNT = "Edit Account";
    protected final static String TABLE_VIEW_IMPORTANCE_CELL = "importance";
    protected final static String TABLE_VIEW_RECEIVED_CELL = "received";
    protected final static String TABLE_VIEW_READ_CELL = "read";
    protected final static String TABLE_VIEW_SENDER_CELL = "sender";
    protected final static String TABLE_VIEW_RECEIVER_TO_CELL = "receiverTo";
    protected final static String TABLE_VIEW_SUBJECT_CELL = "subject";

    protected TreeItem<Component> root;
    protected ApplicationLogicIF appLogic;
    protected Folder selectedFolder;    
    protected ObservableList<Email> data;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File file       = new File(System.getProperty(ROOT_DIRECTION));
        data            = FXCollections.observableArrayList();
        root            = new TreeItem<>();
        appLogic        = new Facade(file);
        selectedFolder  = appLogic.getTopFolder();

        configTreeView();
        configMenue();
        configTableView();
        refreshRootOnTreeView();  
    }
    
    final protected void configMenue() {
        configOpenAccount();
        configEditAccount();
        
        for(Menu m : menuBar.getMenus())
            m.setOnAction(e -> MenueItemsEvent(e));  
    }
    
    final protected void MenueItemsEvent(final ActionEvent e) {
        MenuItem mi = (MenuItem) e.getTarget();
        String cmd = mi.getText();
       
        DirectoryChooser dirChooser;
        File file;
        
        switch (cmd) {
            case MENU_ITEM_OPEN:
                dirChooser = new DirectoryChooser();
                
                if ((file = dirChooser.showDialog(new Stage())) != null) {
                    appLogic.changeDirectory(file);
                    
                    editRootItem();
                    createHierarchy();
                    refreshRootOnTreeView();

                    TempData.getTempData().addData(file.getAbsolutePath());
                    tableView.getItems().clear();
                }
                break;
            case MENU_ITEM_HISTORY:
                openHistoryWindow();
                break;
            case MENU_ITEM_SAVE:
                dirChooser = new DirectoryChooser();
                
                if ((file = dirChooser.showDialog(new Stage())) != null) {
                    appLogic.saveEmails(file);
                }
                break;              
            case MENU_ITEM_NEW_ACCOUNT:
                openNewAccountWindow();
                break;
            }
    }
    
    final protected void configOpenAccount(){
        for(final String acc : appLogic.getAllAccounts()){
            MenuItem accountItems = new MenuItem(acc);
            openAccountMenu.getItems().add(accountItems);
            
            if(accountItems.getParentMenu().getText().equals(MENU_OPEN_ACCOUNT)){    
                accountItems.setOnAction(e -> {
                    appLogic.openAccount(accountItems.getText());
                    editRootItem();
                    createHierarchy();
                    refreshRootOnTreeView();
                });
            }
        }
    }
    
    final protected void configEditAccount(){
        for(final String acc : appLogic.getAllAccounts()){
            MenuItem accountItems = new MenuItem(acc);
            editAccountMenu.getItems().add(accountItems);
            
            if(accountItems.getParentMenu().getText().equals(MENU_EDIT_ACCOUNT))
                accountItems.setOnAction(e -> openEditAccountWindow(accountItems));
            
        }
    }

    final protected void configTreeView() {
        editRootItem();
        createHierarchy();
        
        treeView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> loadEmailsInTableViewEvent(o, oldValue, newValue));
        root.addEventHandler(TreeItem.<Component>branchCollapsedEvent(), e -> collapsedEvent(e));
        root.addEventHandler(TreeItem.<Component>branchExpandedEvent(), e -> folderExpandedEvend(e));
    }

    final protected void editRootItem() { 
        final ImageView iv = scale(getClass().getResourceAsStream(OPEN_FOLDER), 28, 28);
        root.setGraphic(iv);
        root.setValue(appLogic.getTopFolder());
    }

    final protected void createHierarchy() {       
        appLogic.loadContent(selectedFolder);        
        TreeItem<Component> item;
        ImageView iv;
        
        for(Component comp : selectedFolder.getComponents()){            
            iv = scale(getClass().getResourceAsStream(FOLDER), 28, 28);
            
            if(comp.isExpandable()){                
                item = new TreeItem<>(comp, iv);
                addDummyToFolder(item, appLogic.getTopFolder());
            }else{                
                item = new TreeItem<>(comp, iv);
            }            
            root.getChildren().add(item);
        }
    }
    
    final protected void collapsedEvent(TreeItem.TreeModificationEvent<Component> e) {
        TreeItem<Component> eventItem = e.getTreeItem();
        ImageView iv = scale(getClass().getResourceAsStream(FOLDER), 28, 28);
        eventItem.setGraphic(iv);
        eventItem.getChildren().remove(0, eventItem.getChildren().size() - 1);
    }
    
    final protected void folderExpandedEvend(TreeItem.TreeModificationEvent<Component> e){
        TreeItem<Component> eventItem = e.getTreeItem();
        removeDummyInFolder(eventItem);
        root = eventItem;
        selectedFolder = new Folder(new File(eventItem.getValue().getPath()), true);
        createHierarchy();
    }

    final protected void refreshRootOnTreeView() {
        root.setExpanded(false);
        root.setExpanded(true);
        treeView.setRoot(root);
    }
    
    final protected void refreshTreeView(){
        treeView.setShowRoot(false);
        treeView.setShowRoot(true);
    }

    final protected void addDummyToFolder(final TreeItem<Component> item, final Folder folder) {
        TreeItem<Component> dummyItem = new TreeItem<>(folder);
        item.getChildren().add(dummyItem);
    }

    final protected void removeDummyInFolder(final TreeItem<Component> item) {
        item.getChildren().clear();
    }
    
    final protected void configTableView() {
        final TableColumn importanceCol   = (TableColumn) tableView.getColumns().get(0);
        final TableColumn receivedCol     = (TableColumn) tableView.getColumns().get(1);
        final TableColumn readCol         = (TableColumn) tableView.getColumns().get(2);
        final TableColumn senderCol       = (TableColumn) tableView.getColumns().get(3);
        final TableColumn recipientsCol   = (TableColumn) tableView.getColumns().get(4);
        final TableColumn subjectCol      = (TableColumn) tableView.getColumns().get(5);

        importanceCol.  setCellValueFactory(new PropertyValueFactory(TABLE_VIEW_IMPORTANCE_CELL));
        receivedCol.    setCellValueFactory(new PropertyValueFactory(TABLE_VIEW_RECEIVED_CELL));
        readCol.        setCellValueFactory(new PropertyValueFactory(TABLE_VIEW_READ_CELL));
        senderCol.      setCellValueFactory(new PropertyValueFactory(TABLE_VIEW_SENDER_CELL));
        recipientsCol.  setCellValueFactory(new PropertyValueFactory(TABLE_VIEW_RECEIVER_TO_CELL));
        subjectCol.     setCellValueFactory(new PropertyValueFactory(TABLE_VIEW_SUBJECT_CELL));
        
        receivedCol.setComparator((s1, s2) -> receivedColumnComparator(s1, s2));
        searchField.textProperty().addListener(o -> filterTableListener(o));
        tableView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> tableViewEmailInformationEvent(o, oldValue, newValue));
    }
    
    final protected void loadEmailsInTableViewEvent(Observable o, TreeItem oldValue, TreeItem newValue){
        if (oldValue != null) {
            if (newValue != null) {
                clearEmailInformations();
                TreeItem<Component> eventItem = newValue;
                Folder emailFolder = (Folder) eventItem.getValue();
                appLogic.loadEmails(emailFolder);
                data = FXCollections.observableArrayList();
                
                for(Email email : emailFolder.getEmails())
                    data.add(email);

                //add to tableview
                tableView.getItems().clear();
                tableView.setItems(data); 

                //on textfield
                sizeLabel.setText("(" + emailFolder.getEmails().size() + ")");
                setSizeCounterOn(emailFolder);
                refreshTreeView();
            }
        }
    }

    final protected void tableViewEmailInformationEvent(ObservableValue<? extends Email> o, Email oldValue, Email newValue){
        if (newValue != null) {        
            Email email = (Email) newValue;
            setEmailInformations(email); //
        }
    }
    
    final protected void filterTableListener(final Observable e){
        if (searchField.getText().equals("")) {
                tableView.setItems(data);
                return;
            }
            
            List<Email> emailList = appLogic.search(searchField.getText());
            data = FXCollections.observableArrayList();
            
            for(Email email : emailList)
                data.add(email);
            
            tableView.setItems(data);
            sizeLabel.setText(String.valueOf(emailList.size()));
    }
    
    final protected int receivedColumnComparator(final Object s1, final Object s2){
        try {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.GERMANY);
                Long d1 = format.parse((String)s1).getTime();
                Long d2 = format.parse((String)s2).getTime();
                
                return d1.compareTo(d2);
            } catch (ParseException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return -1;
    }

    final protected void clearEmailInformations() {
        sender.setText("");
        subject.setText("");
        received.setText("");
        receiver.setText("");
        textArea.setText("");
    }

    final protected void setEmailInformations(final Email email) {
        sender.setText(email.getSender());
        subject.setText(email.getSubject());
        received.setText(email.getReceived());
        receiver.setText(email.getReceiver());
        textArea.setText(email.getText());
    }

    final protected void setSizeCounterOn(Folder emailFolder) {
        String folderName = emailFolder.getName();
        char a = folderName.charAt(folderName.length() - 1);
        if(a != ')'){
            folderName += "(" + String.valueOf(emailFolder.getEmails().size()) + ")";
            emailFolder.setName(folderName);
        }
    }
    
    final protected void openHistoryWindow() {  
        Stage stage = new Stage();
        stage.setTitle("History");
        URL location = getClass().getResource("/de/bht/fpa/mail/s791814/view/History.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setController(new HistoryController(this));

        try {
            Pane pane = (Pane) loader.load();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    final protected void openNewAccountWindow(){
        Stage stage = new Stage();
        stage.setTitle("New Account");
        URL location = getClass().getResource("/de/bht/fpa/mail/s791814/view/Login.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setController(new LoginController(this, null)); //in LoginFXML NICHT controller angeben

        try {
            Pane pane = (Pane) loader.load();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    final protected void openEditAccountWindow(final MenuItem mi){
        Stage stage = new Stage();
        stage.setTitle("Edit Account");
        URL location = getClass().getResource("/de/bht/fpa/mail/s791814/view/Login.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
            loader.setController(new LoginController(this, mi.getText())); 

        try {
            Pane pane = (Pane) loader.load();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    final protected ImageView scale(final InputStream in, final int width, final int height) {
        Image img = new Image(in);
        ImageView iv = new ImageView(img);
        iv.setFitWidth(width);
        iv.setFitHeight(height);

        return iv;
    }
    
}