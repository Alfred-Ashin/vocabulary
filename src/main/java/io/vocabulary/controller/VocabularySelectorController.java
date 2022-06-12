package io.vocabulary.controller;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXDialogLayout;
import com.leewyatt.rxcontrols.controls.RXTranslationButton;
import io.vocabulary.model.LevelEnum;
import io.vocabulary.model.Message;
import io.vocabulary.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertiesUtil;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author Ashin
 * @date 2022-05
 * @Desc
 */
@Slf4j
public class VocabularySelectorController {


    @FXML
    private AnchorPane drawerPane;
    @FXML
    private GridPane selectorPane;

    private double offsetX,offsetY;
    private Scene scene;
    private static String rootPath = System.getProperties().getProperty("user.home");
    private static boolean isDownloading = true;

    private EventHandler<ActionEvent> jumpToMainPage = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            RXTranslationButton button = (RXTranslationButton) actionEvent.getSource();
            if(LevelEnum.LEVEL4.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.LEVEL4.getCode(),LevelEnum.LEVEL4.getUrl());
//                String url = "http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKYbtCAFAH8AmcgMEdHhxU294.zip";
//                loadVocabulary("level404",url);
            }

            if(LevelEnum.LEVEL6.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.LEVEL6.getCode(),LevelEnum.LEVEL6.getUrl());
            }
            if(LevelEnum.IELTS.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.IELTS.getCode(),LevelEnum.IELTS.getUrl());
            }
            if(LevelEnum.TOEFL.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.TOEFL.getCode(),LevelEnum.TOEFL.getUrl());
            }
            if(LevelEnum.RESEARCH.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.RESEARCH.getCode(),LevelEnum.RESEARCH.getUrl());
            }
            if(LevelEnum.SPECIAL4.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.SPECIAL4.getCode(),LevelEnum.SPECIAL4.getUrl());
            }
            if(LevelEnum.SPECIAL8.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.SPECIAL8.getCode(),LevelEnum.SPECIAL8.getUrl());
            }
            if(LevelEnum.ESCALATE.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.ESCALATE.getCode(),LevelEnum.ESCALATE.getUrl());
            }
            if(LevelEnum.MIDDLE.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.MIDDLE.getCode(),LevelEnum.MIDDLE.getUrl());
            }
            if(LevelEnum.HIGH.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.HIGH.getCode(),LevelEnum.HIGH.getUrl());
            }
            if(LevelEnum.LIBERTY.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.LIBERTY.getCode(),LevelEnum.LIBERTY.getUrl());
            }
            if(LevelEnum.LEVELFOUR.getCode().equals(button.getId())){
                loadVocabulary(LevelEnum.LEVELFOUR.getCode(),LevelEnum.LEVELFOUR.getUrl());
            }
        }
    };

    private void loadVocabulary(String word,String url) {
        String savePath = rootPath+ File.separator+"data";
        File file = new File(savePath + File.separator + word);

        VocabularyController.setRootPath(file.getAbsolutePath());
        if(!file.exists() || file.listFiles().length < 50){
            ProgressStage.of(
                    getStage(),
                    new CustomMultitasking(url,savePath,file.getAbsolutePath()),
                    "词源加载中..."
            ).show();
        }else {
            ChangeStageUtils.changeStage(getStage(),"/fxml/vocabulary.fxml");
//            changeStage();
        }
    }


    @FXML
    void onCloseAction(MouseEvent event) {
        Region button = (Region) event.getSource();
        DialogBuilder.createDialog(button);

    }

    @FXML
    void onFullAction(MouseEvent event) {
        Stage stage = getStage();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    void onMiniAction(MouseEvent event) {
        Stage stage = getStage();
        //设置图标话
        stage.setIconified(true);
    }

    @FXML
    void initialize() {


        Properties properties = new Properties();
        //装载配置文件中的qq信息
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sysconfig.properties");
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String directions = properties.getProperty("directions");
        String isFirstLoad = properties.getProperty("isFirstLoad");
        if("true".equals(isFirstLoad)){
            properties.setProperty("isFirstLoad", "false");
            //保存文件
            try {
                URL fileUrl = this.getClass().getClassLoader().getResource("sysconfig.properties");
                FileOutputStream fos = new FileOutputStream(new File(fileUrl.toURI()));
                properties.store(fos, "the primary key of article table");
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(new Label(directions));
            JFXAlert<Void> alert = new JFXAlert<>();
            alert.setOverlayClose(true);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.setTitle("说明");
            alert.initModality(Modality.NONE);
            alert.showAndWait();
        }



        log.info(rootPath);
        for (Node child : selectorPane.getChildren()) {
            RXTranslationButton button = (RXTranslationButton) child;

            button.setOnAction(jumpToMainPage);
        }

        if(SplashScreen.getSplashScreen() != null){
            SplashScreen.getSplashScreen().close();
        }
    }

    private Stage getStage() {
        Stage stage = (Stage) drawerPane.getScene().getWindow();
        return stage;
    }

//    private FXMLLoader createStage(String url, Stage stage) throws IOException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource(url));
//        Parent parent = loader.load();
//        scene = new Scene(parent, null);
//        stage.setScene(scene);
//        stage.initStyle(StageStyle.TRANSPARENT);
//        //关闭全屏退出提示
//        stage.setFullScreenExitHint("");
//        stage.setTitle("词汇");
//        stage.getIcons().add(new Image(this.getClass().getResource("/img/logo.png").toExternalForm()));
//        return loader;
//    }
//
//    private void stageDrag(Stage primaryStage) {
//        scene.setOnMousePressed(event -> {
//            offsetX = event.getSceneX();
//            offsetY = event.getSceneY();
//        });
//
//        //设置场景图拖拽时的位置并减去偏移量
//        scene.setOnMouseDragged(event ->{
//            primaryStage.setX(event.getScreenX() - offsetX);
//            primaryStage.setY(event.getScreenY() - offsetY);
//        });
//    }
//
//
//    private void changeStage() {
//        try {
//
//            Stage stage = new Stage();
//            createStage("/fxml/vocabulary.fxml", stage);
//            getStage().hide();
//            stage.show();
//            //获取鼠标按下时场景图的位置
//            stageDrag(stage);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    @Subscribe
    public void setIsDownloading(Message message){
        isDownloading = message.isFlag();

        if(!isDownloading){
            Platform.runLater(() ->ChangeStageUtils.changeStage(getStage(),"/fxml/vocabulary.fxml"));
        }
        log.info("接收到消息");
    }

    public VocabularySelectorController() {
        EventBusUtil.getEventBus().register(this);
    }


}

