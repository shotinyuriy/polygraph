/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.aksay.polygraph.desktop;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Юрий
 */
@Component
@Scope(value = "prototype")
public class LoginPane extends GridPane {
    
    @Autowired
    UserService userService; 
    
    private EventHandler<ActionEvent> onSignInSuccess;
    
    private Text sceneTitle;
    private Label userName;
    private TextField userTextField;
    private Label password;
    private PasswordField passwordTextBox;
    private Button signIn;
    
    public LoginPane() {
        GridPane grid = this;
        
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        
        sceneTitle = new Text("Добро пожаловать");
        sceneTitle.setId("welcome-text");
        grid.add(sceneTitle, 0, 0, 2, 1);
        
        userName = new Label("Логин:");
        grid.add(userName, 0, 1);
        
        userTextField = new TextField();
        grid.add(userTextField, 1, 1);
        
        password = new Label("Пароль:");
        grid.add(password, 0, 2);
        
        passwordTextBox = new PasswordField();
        grid.add(passwordTextBox, 1, 2);
        
        signIn = new Button("Войти");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(signIn);
        grid.add(hbBtn, 1, 4);
        
        final Text actionTarget = new Text();
        actionTarget.setId("action-target");
        actionTarget.setWrappingWidth(300);
        grid.add(actionTarget, 0, 6, 2, 1);
        
        signIn.setOnAction( new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent e) {
               if(isSignInSuccess()) {
                   onSignInSuccess.handle(e);
               }
               else {
                   actionTarget.setText("Неправильные логин или пароль. Попробуйте еще раз!");
               }
           }
        });
    }
    
    private boolean isSignInSuccess() {
        String userName = userTextField.getText();
        String password = passwordTextBox.getText();
        User user = userService.findByLoginAndPassword(userName, password);
        if(user != null) {
            return true;
        }
        return false;
    }
    
    public void setOnSignInSuccess(EventHandler<ActionEvent> eh) {
        onSignInSuccess = eh;
    }
}
