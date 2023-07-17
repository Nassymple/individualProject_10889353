package com.example.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class dashboardController {

    @FXML
    private Button addProduct_btn;

    @FXML
    private Button addProducts_addBtn;

    @FXML
    private TextField addProducts_brand;

    @FXML
    private TableColumn<productData, String> addProducts_col_brand;

    @FXML
    private TableColumn<productData, String> addProducts_col_price;

    @FXML
    private TableColumn<productData, String> addProducts_col_productName;

    @FXML
    private TableColumn<productData, String> addProducts_col_productId;

    @FXML
    private TableColumn<productData, String> addProducts_col_status;

    @FXML
    private TableColumn<productData, String> addProducts_col_type;

    @FXML
    private Button addProducts_deleteBtn;

    @FXML
    private AnchorPane addProducts_form;

    @FXML
    private ImageView addProducts_imageView;

    @FXML
    private Button addProducts_importBtn;

    @FXML
    private TextField addProducts_price;

    @FXML
    private TextField addProducts_productName;

    @FXML
    private ComboBox<?> addProducts_productType;

    @FXML
    private TextField addProducts_productId;

    @FXML
    private Button addProducts_resetBtn;

    @FXML
    private TextField addProducts_search;

    @FXML
    private ComboBox<?> addProducts_status;

    @FXML
    private TableView<productData> addProducts_tableView;

    @FXML
    private Button addProducts_updateBtn;

    @FXML
    private Button close;

    @FXML
    private Label home_availableProducts;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private AreaChart<?, ?> home_incomeChart;

    @FXML
    private Label home_numberOder;

    @FXML
    private BarChart<?, ?> home_orderChart;

    @FXML
    private Label home_totalIncome;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private TextField orders_amount;

    @FXML
    private Label orders_balance;

    @FXML
    private ComboBox<?> orders_brand;

    @FXML
    private Button orders_btn;

    @FXML
    private TableColumn<CustomerData, String> orders_col_brand;

    @FXML
    private TableColumn<CustomerData, String> orders_col_price;

    @FXML
    private TableColumn<CustomerData, String> orders_col_productName;

    @FXML
    private TableColumn<CustomerData, String> orders_col_quantity;

    @FXML
    private TableColumn<CustomerData, String> orders_col_type;

    @FXML
    private AnchorPane orders_form;

    @FXML
    private Button orders_payBtn;

    @FXML
    private ComboBox<?> orders_productName;

    @FXML
    private ComboBox<?> orders_productType;

    @FXML
    private Spinner<?> orders_quantity;

    @FXML
    private Button orders_receiptBtn;

    @FXML
    private Button orders_resetBtn;

    @FXML
    private TableView<CustomerData> orders_tableView;

    @FXML
    private Label orders_total;

    @FXML
    private Label username;

    @FXML
    private Button orders_addBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private Image image;

    public void addProductsAdd(){
        String sql = "INSERT INTO product (product_id, type, brand, productName, price, status, image, date) "
                + "VALUES(?,?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try{

            Alert alert;
            if(addProducts_productId.getText().isEmpty() || addProducts_productType.getSelectionModel().getSelectedItem() == null
            || addProducts_brand.getText().isEmpty() || addProducts_productName.getText().isEmpty()
            || addProducts_price.getText().isEmpty() || addProducts_status.getSelectionModel().getSelectedItem() == null
            || getData.path == " "){

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Fill blank space");
                alert.showAndWait();

            }else{

            // this code checks if the product_id already exists
            String checkData = "SELECT product_id FROM product WHERE product_id = '"
                    +addProducts_productId.getText()+"' ";

            statement = connect.createStatement();
            result = statement.executeQuery(checkData);

            if(result.next()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Product_ID: " +addProducts_productId.getText()+ "already exists!");
                alert.showAndWait();


            }else {

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, addProducts_productId.getText());
                prepare.setString(2, (String) addProducts_productType.getSelectionModel().getSelectedItem());
                prepare.setString(3, addProducts_brand.getText());
                prepare.setString(4, addProducts_productName.getText());
                prepare.setString(5, addProducts_price.getText());
                prepare.setString(6, (String) addProducts_status.getSelectionModel().getSelectedItem());

                String uri = getData.path;
                uri = uri.replace("\\", "\\\\");
                prepare.setString(7, uri);

                Date date = new Date(0, 0, 0);
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(8, String.valueOf(sqlDate));

                prepare.executeUpdate();

                addProductsShowListData(); //to update the table view

                addProductReset();// this clears the fields in the table
            }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //the code to update the tableview
    public void addProductsUpdate(){
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        Date date = new Date(0, 0,0);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "UPDATE product SET type = '" +addProducts_productType.getSelectionModel().getSelectedItem()
                + "', brand = '"+addProducts_brand.getText()
                +"', productName = '"+addProducts_productName.getText()
                +"', price = '"+addProducts_price.getText()
                +"', status = '"+addProducts_status.getSelectionModel().getSelectedItem()
                +"', image = '"+uri+"', date = '"+sqlDate+"' WHERE product_id = '"
                + addProducts_productId.getText()+"' ";


        connect = database.connectDb();
        try{
            Alert alert;
            if(addProducts_productId.getText().isEmpty() || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty() || addProducts_productName.getText().isEmpty()
                    || addProducts_price.getText().isEmpty() || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || getData.path == " "){

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Fill blank space");
                alert.showAndWait();

            }else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Product ID: "+ addProducts_productId.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successful!!");
                    alert.showAndWait();

                    addProductsShowListData();
                    addProductReset();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //the code to delete from system
    public void addProductsDelete(){
        String sql = "DELETE FROM product WHERE product_id = '"+ addProducts_productId.getText() +"' ";

        connect = database.connectDb();
        try{
            Alert alert;
            if(addProducts_productId.getText().isEmpty() || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty() || addProducts_productName.getText().isEmpty()
                    || addProducts_price.getText().isEmpty() || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || getData.path == " "){

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Fill blank space");
                alert.showAndWait();

            }else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Product ID: "+ addProducts_productId.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("Deletion Success!!");
                    alert.showAndWait();

                    addProductsShowListData();
                    addProductReset();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Code for the reset button
    public void addProductReset(){
        addProducts_productId.setText("");
        addProducts_productType.getSelectionModel().clearSelection();
        addProducts_brand.setText("");
        addProducts_productName.setText("");
        addProducts_price.setText("");
        addProducts_status.getSelectionModel().clearSelection();
        addProducts_imageView.setImage(null);


        getData.path = " ";
    }

    public void addProductsImportImage(){

        FileChooser open = new FileChooser();
        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*jpg", "*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if(file != null){
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 146, 136, false, true);

            addProducts_imageView.setImage(image);
        }
    }

    private String[] listType = {"Beverages", "Bakery", "Canned Goods", "Dairy Products", "Baking Goods", "Frozen Products", "Meat", "Farm Produce", "Home Cleaners", "Paper Goods", "Home Care"};
    public void addProductsListType(){
        List<String> listT = new ArrayList<>();

        for(String data: listType){
            listT.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listT);
        addProducts_productType.setItems(listData);
    }

    private String [] listStatus = {"Available", "Not Available"};
    public void addProductsListStatus(){
        List<String> listS = new ArrayList<>();

        for(String data: listStatus){
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        addProducts_status.setItems(listData);
    }


    //this code is for the search bar
    public void addProductsSearch(){
        FilteredList<productData> filter = new FilteredList<>(addProductsList, e -> true);

        addProducts_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateproductData -> {

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String searchKey = newValue.toLowerCase();
                if(predicateproductData.getProductId().toString().contains(searchKey)){
                    return true;
                } else if (predicateproductData.getType().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateproductData.getBrand().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateproductData.getProductName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateproductData.getPrice().toString().contains(searchKey)) {
                    return true;
                } else if (predicateproductData.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else return false;
            });
        });

        SortedList<productData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(addProducts_tableView.comparatorProperty());
        addProducts_tableView.setItems(sortList);
    }


//displays items on the table
    public ObservableList<productData> addProductsListData(){

        ObservableList<productData> productList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product";
        connect = database.connectDb();
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            productData prodD;

            while (result.next()){
                prodD = new productData(result.getInt("product_id"),
                        result.getString("type"), result.getString("brand"),
                        result.getString("productName"), result.getDouble("price"),
                        result.getString("status"), result.getString("image"),
                        result.getDate("date"));

                productList.add(prodD);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return productList;
    }

    private ObservableList<productData> addProductsList;
    public void addProductsShowListData(){
        addProductsList = addProductsListData();

        addProducts_col_productId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        addProducts_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        addProducts_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addProducts_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addProducts_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProducts_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        addProducts_tableView.setItems(addProductsList);

    }

    // Adding product to the comboBox
    public void addProductsSelect(){
        productData prodD = addProducts_tableView.getSelectionModel().getSelectedItem();
        int num = addProducts_tableView.getSelectionModel().getSelectedIndex();

        if((num - 1) < -1){
            return;
        }
        addProducts_productId.setText(String.valueOf(prodD.getProductId()));
        addProducts_brand.setText(prodD.getBrand());
        addProducts_productName.setText(prodD.getProductName());
        addProducts_price.setText(String.valueOf(prodD.getPrice()));

        String uri = "file:" + prodD.getImage();

        image = new Image(uri, 146, 136, false, true);
        addProducts_imageView.setImage(image);

        getData.path = prodD.getImage();
    }


    public ObservableList<CustomerData> ordersListData(){
        customerId();
        ObservableList<CustomerData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer WHERE customer_id = '"+ customerid +"' ";

        connect =database.connectDb();

        try {
            CustomerData customerD;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while(result.next()){
                customerD = new CustomerData(result.getInt("customer_id"),
                        result.getString("type"), result.getString("brand"),
                        result.getString("productName"), result.getInt("quantity"),
                        result.getDouble("price"), result.getDate("date"));

                listData.add(customerD);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<CustomerData> ordersList;
    public void ordersShowListData(){
        ordersList = ordersListData();

        orders_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        orders_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        orders_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        orders_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orders_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        orders_tableView.setItems(ordersList);


    }


    private int customerid;
    public void customerId(){

        String customId = "SELECT * FROM customer ";
        connect = database.connectDb();

        try{
            prepare = connect.prepareStatement(customId);
            result = prepare.executeQuery();

            int checkId = 0;

            while (result.next()){
                customerid = result.getInt("customer_id"); // this gets the last customerId
            }

            String checkData = "SELECT * FROM customer_receipt";
            statement = connect.createStatement();
            result = statement.executeQuery(checkData);

            while(result.next()){
                checkId = result.getInt("customer_id");
            }

            if(customerid == 0){
                customerid += 1;
            } else if (checkId == customerid) {
                customerid += 1;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //this method switches the form on the dashboard
    public void switchForm(ActionEvent event){
        if(event.getSource() == home_btn){
            home_form.setVisible(true);
            addProducts_form.setVisible(false);
            orders_form.setVisible(false);

            home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #19b999,#09948d);");
            addProduct_btn.setStyle("-fx-background: transparent");
            orders_btn.setStyle("-fx-background: transparent");

        } else if (event.getSource() == addProduct_btn) {
            home_form.setVisible(false);
            addProducts_form.setVisible(true);
            orders_form.setVisible(false);

            addProduct_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #19b999,#09948d);");
            home_btn.setStyle("-fx-background: transparent");
            orders_btn.setStyle("-fx-background: transparent");

            addProductsShowListData();
            addProductsListStatus();
            addProductsListType();
            addProductsSearch();
            
        } else if (event.getSource() == orders_btn) {
            home_form.setVisible(false);
            addProducts_form.setVisible(false);
            orders_form.setVisible(true);

            orders_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #19b999,#09948d);");
            addProduct_btn.setStyle("-fx-background: transparent");
            home_btn.setStyle("-fx-background: transparent");
            
        }
    }

    private double x = 0;
    private double y = 0;
    public void logout(){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){

                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));//this links the login form.
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) ->{
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) ->{
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                    stage.setOpacity(0.8);
                });

                root.setOnMouseReleased((MouseEvent event) ->{
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }else return;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void minimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    public void close(){
        System.exit(0);
    }


    public void initialize(URL location, ResourceBundle resources){
        // this displays the information in the tableview
        addProductsShowListData();
        addProductsListStatus();
        addProductsListType();
    }
}


