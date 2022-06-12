package io.vocabulary.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @author Ashin
 * @date 2022/6/9
 * @Desc
 */
public class ChangeStageUtils {
    private static Scene scene;
    private static double offsetX,offsetY;

    public static void changeStage(Stage parent,String url) {
        try {
            Stage stage = new Stage();
            createStage(url, stage);
            parent.hide();
            stage.show();
            //获取鼠标按下时场景图的位置
            stageDrag(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FXMLLoader createStage(String url, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ChangeStageUtils.class.getResource(url));
        Parent parent = loader.load();
        scene = new Scene(parent, null);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        //关闭全屏退出提示
        stage.setFullScreenExitHint("");
        stage.setTitle("词汇");
        stage.getIcons().add(new Image(ChangeStageUtils.class.getResource("/img/logo.png").toExternalForm()));
        return loader;
    }

    public static void stageDrag(Stage primaryStage) {
        scene.setOnMousePressed(event -> {
            offsetX = event.getSceneX();
            offsetY = event.getSceneY();
        });

        //设置场景图拖拽时的位置并减去偏移量
        scene.setOnMouseDragged(event ->{
            primaryStage.setX(event.getScreenX() - offsetX);
            primaryStage.setY(event.getScreenY() - offsetY);
        });
    }
}
