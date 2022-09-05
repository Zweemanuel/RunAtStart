package com.runatstart;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private void showItems(String pathname, ListView<File> listV) throws IOException { //TODO: do not add x to allItems if already in startItems
        Boolean found = false;
        File f = new File(pathname);
        File[] fileList = f.listFiles();
        for (File file : fileList) {
            
            if(file.isFile()){//FIX fix this, why is file not found???
                for(File fileS : allItems.getItems()){
                    if(file.getName()==fileS.getName()){
                        found=true;
                        System.out.println("FOUND");
                    }
                }
                if(!found){
                    listV.getItems().add(file);
                }
                
            }else{
                showItems(pathname+"/"+file.getName(),listV);
            }
        }
    }

    

    
    @FXML
    private void RefreshLists() throws IOException {
        allItems.getItems().clear();
        startItems.getItems().clear();
        
        showItems("C:/ProgramData/Microsoft/Windows/Start Menu/Programs",allItems);
        showItems("C:/ProgramData/Microsoft/Windows/Start Menu/Programs/startup",startItems);

        System.out.println("Lists refreshed");
        
    }

    @FXML
    private void AddStartItem() throws IOException {
        
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            RefreshLists();
        } catch (IOException e) {
            e.printStackTrace();
        }

        allItems.setOnMouseClicked((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {//Copies the clicked file to the startup folder and removes it from the list
                
                if (click.getClickCount() >= 2) {
                   //Get current selected item
                   File currentItemSelected = allItems.getSelectionModel().getSelectedItem();
                   
                    startItems.getItems().add(currentItemSelected);
                    allItems.getItems().remove(currentItemSelected);
                   
                   
                }
            }
        });
    }

}
