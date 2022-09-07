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
import javafx.scene.input.MouseEvent;

public class PrimaryController implements Initializable{

    String allPath = "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/", startPath = "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/StartUp/";
    String allUserPath ="C:/Users/"+System.getProperty("user.name")+"/AppData/Roaming/Microsoft/Windows/Start Menu/Programs", startUserPath ="C:/Users/"+System.getProperty("user.name")+"/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/StartUp";
    
    @FXML
    private ListView<File> allItems,startItems;
    @FXML
    Label allItemsAmount,startItemsAmount;
    
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
    

    @FXML
    private void showItems(String pathname, ListView<File> listV) throws IOException {
        
        File f = new File(pathname);
        File[] fileList = f.listFiles();
        for (File file : fileList) {
            if(file.isFile() ){
                if(file.getName().contains(".lnk") && !file.getName().contains("Uninstall")){//Only show shortcuts to applications
                    listV.getItems().add(file);
                }
            }else{
                showItems(pathname+"/"+file.getName(),listV);
            }
        }
    }


    
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
        
        //Removing files from the AllItems ListView for UX
        List<File> toRemove = new ArrayList<>();
        for(File fS : startItems.getItems()){
            for(File fA : allItems.getItems()){
                if(fS.getName().compareTo(fA.getName())==0){
                    toRemove.add(fA);
                }
            }
        }
        allItems.getItems().removeAll(toRemove);
        allItemsAmount.setText(""+allItems.getItems().size()+" Item(s)");
        startItemsAmount.setText(""+startItems.getItems().size()+" Item(s)");
    }

    @FXML
    private void AddStartItem() throws IOException {//TODO: Open file explorer to add file
        
    }

    private void removeFromStart(){
        startItems.setOnMouseClicked((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {//Removes the clicked file to the startup folder
                if (click.getClickCount() >= 2) {
                    File currentItemSelected = startItems.getSelectionModel().getSelectedItem();
                    Path source = Paths.get(currentItemSelected.toURI());
                    try {
                        Files.delete(source);;
                        RefreshLists();
                        // Less Reliable but more cpu efficient UX vvv
                        //startItems.getItems().remove(currentItemSelected);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void moveToStart(){
        allItems.setOnMouseClicked((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {//Copies the clicked file to the startup folder
                if (click.getClickCount() == 2) {
                    File currentItemSelected = allItems.getSelectionModel().getSelectedItem();
                    Path source = Paths.get(currentItemSelected.toString());
                    Path destination = Paths.get(startPath+currentItemSelected.getName());
                    try {
                        Files.copy(source, destination);
                        RefreshLists();
                        // Less Reliable but more cpu efficient UX vvv
                        //startItems.getItems().add(currentItemSelected);allItems.getItems().remove(currentItemSelected);
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
