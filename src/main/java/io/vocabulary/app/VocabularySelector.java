package io.vocabulary.app;

import io.vocabulary.utils.ChangeStageUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 *
 * @author Ashin
 * @date 2022-05
 * @Desc
 */
public class VocabularySelector extends Application {

//    private double offsetX,offsetY;
//    private Scene scene;

     @Override
     public void start(Stage primaryStage) throws Exception {
         ChangeStageUtils.changeStage(primaryStage,"/fxml/vocabularyselector.fxml");

//         FXMLLoader loader = new FXMLLoader();
//         loader.setLocation(getClass().getResource("/fxml/vocabularyselector.fxml"));
//         Parent parent = loader.load();
//         scene = new Scene(parent, null);
//         primaryStage.setScene(scene);
//         primaryStage.initStyle(StageStyle.TRANSPARENT);
//         //关闭全屏退出提示
//         primaryStage.setFullScreenExitHint("");
//         primaryStage.setTitle("词汇");
//         primaryStage.getIcons().add(new Image(this.getClass().getResource("/img/logo.png").toExternalForm()));
//         primaryStage.show();
//         //获取鼠标按下时场景图的位置
//         scene.setOnMousePressed(event -> {
//             offsetX = event.getSceneX();
//             offsetY = event.getSceneY();
//         });
//
//         //设置场景图拖拽时的位置并减去偏移量
//         scene.setOnMouseDragged(event ->{
//             primaryStage.setX(event.getScreenX() - offsetX);
//             primaryStage.setY(event.getScreenY() - offsetY);
//         });

     }

}
