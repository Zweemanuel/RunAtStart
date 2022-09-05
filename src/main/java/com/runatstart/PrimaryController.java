package com.runatstart;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

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
            
            if(file.isFile()){
                System.out.println(file.getName());
                listV.getItems().add(file);
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
        showItems("C:/ProgramData/Microsoft/Windows/Start Menu/Programs",startItems);
        System.out.println("Lists cleared");
        
    }

    @FXML
    private void AddStartItem() throws IOException {
        
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            this.showItems("C:/ProgramData/Microsoft/Windows/Start Menu/Programs",allItems);
            this.showItems("C:/ProgramData/Microsoft/Windows/Start Menu/Programs/StartUp",startItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
