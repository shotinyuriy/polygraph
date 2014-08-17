/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.aksay.polygraph.desktop;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kz.aksay.polygraph.service.UserService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Юрий
 */
public class DesignerWorkFlow extends Application {
    
    static ApplicationContext context;
    static AnnotationConfigApplicationContext acaContext;
    
    
    
    @Override
    public void start(final Stage primaryStage) {
        
        context = kz.aksay.polygraph.util.ContextUtils.getApplicationContext();
        context.getBean(UserService.class);
        acaContext = new AnnotationConfigApplicationContext();
        acaContext.setParent(context);
        acaContext.scan("kz.aksay.polygraph.desktop");
        acaContext.refresh();
        
        LoginPane loginPane = acaContext.getBean(LoginPane.class);
        
        Scene scene = new Scene(loginPane,480,320);
        scene.getStylesheets().add(
            DesignerWorkFlow.class.getResource("login.css").toExternalForm());
        
        loginPane.setOnSignInSuccess(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                DirectorPane directorPane = acaContext.getBean(DirectorPane.class);
                Scene scene = new Scene(directorPane, 640, 480);
                primaryStage.setScene(scene);
            }
        });
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Welcome");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        launch(args);
    }
    
}
