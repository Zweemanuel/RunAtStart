# runatstart

## JavaFX Application

Manage programs that run at startup of machine.

![image](https://user-images.githubusercontent.com/79989883/189085687-2c006ea0-45f7-4bc1-acdb-59ca34a1db91.png)

## Run the application with the .bat file [(JavaFX needed)](https://gluonhq.com/products/javafx/)

java -jar --module-path **"C:\Program Files\Java\javafx-sdk-18.0.2\lib"** --add-modules javafx.controls,javafx.fxml runatstart.jar

- Replace **bold text** with your specific path

## Permissions

Placing programs in the StartUp Folder may require write permissions.
- (C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp)

![image](https://user-images.githubusercontent.com/79989883/188822359-0fac58ba-6bb8-4948-bd4a-7cf182a9529c.png)

## Distribution possible by 
- exporting .jar 
- using Jlink to create a customized run-time image
- using the prior points as input for JPackage
