package com.runatstart;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class PrimaryController implements Initializable{

    String allPath = "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/", startPath = "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/StartUp/";
    String allUserPath ="C:/Users/"+System.getProperty("user.name")+"/AppData/Roaming/Microsoft/Windows/Start Menu/Programs", startUserPath ="C:/Users/"+System.getProperty("user.name")+"/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/StartUp";
    
    @FXML
    private ListView<File> allItems,startItems;
    @FXML
    Label allItemsAmount,startItemsAmount;
    @FXML
    TextField searchAllItems;
    
    // Only show the NAME of the Program and not the Path
    public void changeListView(ListView<File> list){ 
        list.setCellFactory(lv -> new ListCell<File>() {
            @Override
            public void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    //Add a file using the file explorer
    @FXML
    private void AddStartItem() throws IOException {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(null);
        System.out.println(selectedFile.getAbsolutePath());
        if(selectedFile!=null && (selectedFile.getName().contains(".lnk") || selectedFile.getName().contains(".exe")|| selectedFile.getName().contains(".EXE"))){
            
            Path source = Paths.get(selectedFile.toString());
            Path destination = Paths.get(startPath+selectedFile.getName());
            try {
                Files.copy(source, destination);
                RefreshLists();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Invalid file");
        }
    }
    
    //Populate the ListView
    @FXML
    private void showItems(String pathname, ListView<File> listV) throws IOException {
        
        File f = new File(pathname);
        File[] fileList = f.listFiles();
        for (File file : fileList) {
            if(file.isFile() ){
                //Only show shortcuts to applications
                if(!file.getName().contains("Uninstall") && (file.getName().contains(".lnk") || file.getName().contains(".exe")|| file.getName().contains(".EXE"))){
                    listV.getItems().add(file);
                }
            }else{
                showItems(pathname+"/"+file.getName(),listV);
            }
        }
    }

    
    
    //Refresh the ListView & Remove unnecessary files (Only on the View)
    @FXML
    private void RefreshLists() throws IOException {
        startItems.getItems().clear();
        allItems.getItems().clear();
        //All Users
        showItems(startPath,startItems);
        showItems(allPath,allItems);
        //User specific
        showItems(startUserPath,startItems);
        showItems(allUserPath,allItems);
        //Gathering files to remove (already in startItems)
        List<File> toRemove = new ArrayList<>();
        for(File fS : startItems.getItems()){
            for(File fA : allItems.getItems()){
                if(fS.getName().compareTo(fA.getName())==0){
                    toRemove.add(fA);
                }
            }
        }
        //Gathering files to remove (Based on the search textfield)
        if(searchAllItems!=null){
            for(File fS : allItems.getItems()){
                if(!fS.getName().toLowerCase().contains(searchAllItems.getText().toLowerCase())){
                    toRemove.add(fS);
                }
            }
        }
        //Removing gathered files
        allItems.getItems().removeAll(toRemove);
        allItemsAmount.setText(""+allItems.getItems().size()+" Item(s)");
        startItemsAmount.setText(""+startItems.getItems().size()+" Item(s)");
    }

    
    //Remove files from the StartUp Folder
    private void removeFromStart(){
        startItems.setOnMouseClicked((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {//Removes the clicked file to the startup folder
                if (click.getClickCount() >= 2) {
                    File currentItemSelected = startItems.getSelectionModel().getSelectedItem();
                    Path source = Paths.get(currentItemSelected.toURI());
                    try {
                        Files.delete(source);
                        RefreshLists();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //Pushes Programs to start on ALL Users
    private void moveToStart(){
        allItems.setOnMouseClicked((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    File currentItemSelected = allItems.getSelectionModel().getSelectedItem();
                    Path source = Paths.get(currentItemSelected.toString());
                    Path destination = Paths.get(startPath+currentItemSelected.getName());
                    try {
                        Files.copy(source, destination);
                        RefreshLists();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            RefreshLists();
        } catch (IOException e) {
            e.printStackTrace();
        }
        moveToStart();
        removeFromStart();
        changeListView(startItems);
        changeListView(allItems);
    }

}
