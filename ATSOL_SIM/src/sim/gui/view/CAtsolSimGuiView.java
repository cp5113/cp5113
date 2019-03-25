package sim.gui.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sim.gui.control.CAtsolSimGuiControl;

public class CAtsolSimGuiView extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private CAtsolSimGuiControl controller;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("ATSOL SIM");
		initRootLayout();
			
		
	}
	private void initRootLayout() {
        try {
            // fxml 파일에서 상위 레이아웃을 가져온다.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CAtsolSimGuiView.class.getResource("CAtsolSimGuiView.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Add Controller
            CAtsolSimGuiControl  controller = loader.getController();            
            controller.addview(this);
            controller.initialize();
            
            
            
            // 상위 레이아웃을 포함하는 scene을 보여준다.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	

	public CAtsolSimGuiView() {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");		
	}
	

	public static void main(String[] args) {
		launch(args);	
		
	}
}
