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
    private ListView<String> allItems;

    @FXML
    private void showAllItems() throws IOException {
        allItems.getItems().clear();
        String[] pathnames;
        File f = new File("C:/ProgramData/Microsoft/Windows/Start Menu/Programs");
        pathnames = f.list();

        for (String pathname : pathnames) {
            // Print the names of files and directories
            System.out.println(pathname);
            allItems.getItems().add(pathname);
        }
    }

    @FXML
    private void RefreshLists() throws IOException {
        allItems.getItems().clear();
        showAllItems();
        System.out.println("Lists cleared");
        
    }

    @FXML
    private void AddItem() throws IOException {
        
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            this.showAllItems();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
