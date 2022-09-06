# runatstart

## JavaFX Application

Manage programs that run at startup of machine.

![image](https://user-images.githubusercontent.com/79989883/188753489-1e1e84f4-3e28-4145-ac4c-7193e094af8f.png)

## Run the application by editing the .bat file

java -jar --module-path **"C:\Program Files\Java\javafx-sdk-18.0.2\lib"** --add-modules javafx.controls,javafx.fxml runatstart.jar

## Permissions

Writing to the startUp Folder requires admin permissions.

## Distribution possible by 
- exporting .jar 
- using Jlink to create a customized run-time image
- using the prior points as input for JPackage
