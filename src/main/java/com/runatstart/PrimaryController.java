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
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class PrimaryController implements Initializable{

    @FXML
    private ListView<File> allItems;
    @FXML
    private ListView<File> startItems;
    
    @FXML
    private void showItems(String pathname, ListView<File> listV) throws IOException {
        
        File f = new File(pathname);
        File[] fileList = f.listFiles();
        for (File file : fileList) {
            if(file.isFile() ){
                if(file.getName().contains(".txt")){//Only show shortcuts to applications
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
        showItems("C:/Users/Emanuel/Desktop/TestFolder/startItems/",startItems);
        showItems("C:/Users/Emanuel/Desktop/TestFolder/allItems/",allItems);
        
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
                    Path source = Paths.get("C:/Users/Emanuel/Desktop/TestFolder/startItems/"+currentItemSelected.getName());
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
                    Path destination = Paths.get("C:/Users/Emanuel/Desktop/TestFolder/startItems/"+currentItemSelected.getName());
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
        
    }

}
