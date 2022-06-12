package io.vocabulary.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfoenix.controls.*;
import io.vocabulary.model.WordFileInfo;
import io.vocabulary.model.WordIndex;
import io.vocabulary.model.WordInfo;
import io.vocabulary.utils.ChangeStageUtils;
import io.vocabulary.utils.DialogBuilder;
import io.vocabulary.utils.WordUtil;
import javafx.animation.*;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.EmptyFileFilter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author Ashin
 * @date 2022/6/9
 * @Desc
 */
public class VocabularySteadyController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane drawerPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private GridPane contentPane;

    private Label centerLabel   = new Label();;

    @FXML
    private ImageView iv;

    @FXML
    private ImageView iv1;
    @FXML
    private ImageView iv2;
    @FXML
    private ImageView iv3;
    @FXML
    private ImageView iv4;
    @FXML
    private ImageView iv5;

    @FXML
    private Label phraseLabel;

    @FXML
    private ToggleButton playBtn;
    @FXML
    private HBox contentBox;
    @FXML
    private HBox imgBtnBox;
    //单词播放器
    private MediaPlayer wordPlayer;
    private  List<WordFileInfo> wordFileInfos = new ArrayList<>();
    private List<WordIndex> list = new ArrayList<>();
    private JFXListView<Label> funList = new JFXListView<>();
    //主文件路径
    public static String filePath;
    public static Stage parentStage;



    public static void setFilePath(String filePath) {
        VocabularySteadyController.filePath = filePath;
    }
    public static void setParentStage(Stage stage) {
        VocabularySteadyController.parentStage = stage;
    }
    //单词意思布局
    private VBox vbox = new VBox();
    private HBox hBox = new HBox();

    private boolean isFirstLoad = true;
    private boolean isFirstClick = true;

    private Pane changePane;
    private String imgPath;
    private String sentence;
    private String word;
    private String wordMediaPath;
    private Integer correctCount = 0;
    private Label cnMeanLabel = new Label();
    private Label wordLabel = new Label();
    private SequentialTransition pt;
    private JFXPopup popup;
    /*********seeMean**********/
    private Label wordMeanLabel;
    private Label accentLabel;
    private VBox wordListBox;
    private GridPane seeMeanStage;
    private String randomWordMean;
    private List<WordFileInfo> seeMeanList = new ArrayList<>();

    private EventHandler<MouseEvent> funMouseEvent = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Label label = funList.getSelectionModel().getSelectedItem();
            System.out.println("数据是:" + label.getText());

            stackPane.getChildren().remove(changePane);

            if(wordFileInfos.isEmpty()){
                noDataTip();
                popup.hide();
                return;
            }

            if("拼写填空".equals(label.getText())){
                stackPane.getChildren().add(contentPane);
                initRandomWord();
            }
            if("观义选词".equals(label.getText())){

                loadSeeSelectData();
                stackPane.getChildren().remove(contentPane);
                stackPane.getChildren().add(seeMeanStage);
                changePane = seeMeanStage;
            }
            if("全词默写".equals(label.getText())){
                noDataTip();
                popup.hide();
                return;
            }
            if("字母归位".equals(label.getText())){
                noDataTip();
                popup.hide();
                return;
            }
            popup.hide();
        }
    };


    private EventHandler<MouseEvent> checkCnMeanEvent = new EventHandler<>() {
        @SneakyThrows
        @Override
        public void handle(MouseEvent mouseEvent) {

            HBox source = (HBox) mouseEvent.getSource();
            ImageView iv = (ImageView) source.getChildren().get(0);
            Label mean = (Label) source.getChildren().get(1);
            if(mean.getText().equals(randomWordMean)){
                mean.setStyle("-fx-background-color: #1ECE9A;-fx-border-color: #1ECE9A;-fx-border-width: 1.5");
                iv.setImage(new Image("/img/check-one.png",20,20,false,true));
                loadSeeSelectData();

            }else {
                mean.setStyle("-fx-background-color: #ff6666;-fx-border-color: #ff6666;-fx-border-width: 1.5");
                iv.setImage(new Image("/img/error-one.png",20,20,false,true));

            }
        }
    };


    @FXML
    void checkAction() {
        pt.setNode(iv3);
        pt.play();
        correctCount = 0;
        if(list.isEmpty()){
            return;
        }
        for (int i = 0; i < hBox.getChildren().size(); i++) {
            JFXTextField field = (JFXTextField) hBox.getChildren().get(i);
            if(field.getText().isBlank()){
                field.setUnFocusColor(Color.RED);
                field.setFocusColor(Color.RED);
                field.setStyle("-fx-text-fill: red");
            }
            if(!field.getText().isBlank() && !list.isEmpty()){
                if(!field.getText().equals(list.get(i).getCharacter())){
                    field.setUnFocusColor(Color.RED);
                    field.setFocusColor(Color.RED);
                    field.setStyle("-fx-text-fill: red");
                }else {
                    field.setUnFocusColor(Color.valueOf("#1ece9a"));
                    field.setFocusColor(Color.valueOf("#1ece9a"));
                    field.setStyle("-fx-text-fill: #1ece9a");
                    correctCount += 1;
                }
            }
        }
        if(correctCount == hBox.getChildren().size()){
            loadData();
        }
    }

    @FXML
    void nextAction(MouseEvent event) {
        pt.setNode(iv4);
        pt.play();
        if(list.isEmpty()){
            return;
        }

        if(changePane.getId().equals("seeMeanPane")){
        loadSeeSelectData();
        }

        if(changePane.getId().equals("contentPane")){

            loadData();
        }
    }

    @FXML
    void tipAction(MouseEvent event) {
        pt.setNode(iv2);
        pt.play();
        if(list.isEmpty()){
            return;
        }
        if(changePane.getId().equals("seeMeanPane")){
            accentLabel.setVisible(true);
            if(wordPlayer != null) {
                wordPlayer = new MediaPlayer(new Media(new File(wordMediaPath).toURI().toString()));
                wordPlayer.setVolume(0.5);
                wordPlayer.setRate(1);
                wordPlayer.setCycleCount(1);
                wordPlayer.play();

            }
        }

        if(changePane.getId().equals("contentPane")){

            if(isFirstClick){
                if(!imgPath.isBlank() && !sentence.isBlank()){
                    iv.setImage(new Image(imgPath,173,111,false,true));
                    String replace = sentence.replace(word, "__");
                    phraseLabel.setText(replace);
                }

                cnMeanLabel.setVisible(true);
            }
        }

    }
    @FXML
    void backAction(MouseEvent event) {
        pt.setNode(iv1);
        pt.play();

        if(parentStage == null){
            return;
        }
        getStage().hide();
        parentStage.show();
    }
    @FXML
    void changeAction(MouseEvent event) {
        pt.setNode(iv5);
        pt.play();

    }

    @FXML
    void onCloseAction(MouseEvent event) {
        Region button = (Region) event.getSource();
        DialogBuilder.createDialog(button);
    }

    @FXML
    void onFullAction(MouseEvent event) {
        //        Stage stage = getStage();
        //        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    void onMiniAction(MouseEvent event) {
        Stage stage = getStage();
        //设置图标话
        stage.setIconified(true);
    }

    @FXML
    void onWordPlayAction(ActionEvent event) {
        if(wordPlayer != null){
            if(playBtn.isSelected()){
                wordPlayer = new MediaPlayer(new Media(new File(wordMediaPath).toURI().toString()));
                wordPlayer.setVolume(0.5);
                wordPlayer.setRate(1);
                wordPlayer.setCycleCount(1);
                wordPlayer.setOnEndOfMedia(() -> {
                    playBtn.setSelected(false);
                });
                wordPlayer.play();
            }else {
                wordPlayer.stop();
            }
        }
    }

    @FXML
    void initialize() {

        initWordFile();
        initRandomWord();
        initButtonAnimation();
        initBaseNodes();
        initSeeMeanStage();
    }

    @SneakyThrows
    private void initSeeMeanStage() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ChangeStageUtils.class.getResource("/fxml/seemeanselectword.fxml"));
        loader.setController(VocabularySteadyController.class);
        seeMeanStage = loader.load();
        ObservableMap<String, Object> namespace = loader.getNamespace();
        wordMeanLabel = (Label) namespace.get("wordMeanLabel");
        accentLabel = (Label) namespace.get("accentLabel");
        wordListBox = (VBox) namespace.get("wordListBox");

        wordMeanLabel.setAlignment(Pos.CENTER);
        wordMeanLabel.setFont(new Font("Heiti",20));
        wordMeanLabel.setTextFill(Paint.valueOf("#333333"));
        wordMeanLabel.setWrapText(true);

        accentLabel.setAlignment(Pos.CENTER);
        accentLabel.setFont(new Font("FZShuTi",16));
        accentLabel.setWrapText(true);
        accentLabel.setTextFill(Paint.valueOf("#333333"));

        for (Node child : wordListBox.getChildren()) {
            HBox cnMean = (HBox)child;

            ImageView iv = (ImageView) cnMean.getChildren().get(0);
            Label mean = (Label) cnMean.getChildren().get(1);
            cnMean.setOnMouseClicked(checkCnMeanEvent);
            cnMean.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mean.setStyle("-fx-background-color: #e8e8e8;-fx-border-color: #eff0f1;-fx-border-width: 2");
                    iv.setImage(null);
                }
            });
        }


    }

    private void initBaseNodes() {
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(8);

        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);

        centerLabel.setAlignment(Pos.CENTER);
        centerLabel.setFont(new Font("Heiti",15));
        centerLabel.setTextFill(Paint.valueOf("#333333"));
        contentBox.getChildren().addAll(centerLabel);


        wordLabel.setAlignment(Pos.CENTER);
        wordLabel.setFont(new Font("Heiti",20));
        wordLabel.setTextFill(Paint.valueOf("#333333"));
        wordLabel.setWrapText(true);
        cnMeanLabel.setAlignment(Pos.CENTER);
        cnMeanLabel.setFont(new Font("FZShuTi",16));
        cnMeanLabel.setWrapText(true);
        cnMeanLabel.setTextFill(Paint.valueOf("#333333"));


        funList.getStyleClass().add("word-list");
        funList.setPrefWidth(120);
        String showText = "";
        for (int i = 1; i < 5; i++) {
            Label label = new Label();
            if(i == 1){
                showText = "拼写填空";
                label.setGraphic(new ImageView(new Image("/img/write-one.png",15,15,true,true)));
            }
            if(i == 2){
                showText = "观义选词";
                label.setGraphic(new ImageView(new Image("/img/aiming-one.png",15,15,true,true)));
            }
            if(i == 3){
                showText = "全词默写";
                label.setGraphic(new ImageView(new Image("/img/repair-one.png",15,15,true,true)));
            }
            if(i == 4){
                showText = "字母归位";
                label.setGraphic(new ImageView(new Image("/img/back-one.png",15,15,true,true)));
            }

            label.setText(showText);
            label.setGraphicTextGap(10);
            funList.getItems().add(label);
        }
        funList.getSelectionModel().selectFirst();
        popup = new JFXPopup(funList);
        iv5.setOnMouseClicked(e -> popup.show(iv5, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT));
        funList.addEventHandler(MouseEvent.MOUSE_CLICKED, funMouseEvent);

    }

    private void initButtonAnimation() {
        ScaleTransition tt = new ScaleTransition();
        tt.setDuration(Duration.seconds(0.15));
        tt.setFromX(1);
        tt.setFromY(1);
        tt.setToX(1.15);
        tt.setToY(1.15);
        tt.setInterpolator(Interpolator.LINEAR);

        ScaleTransition tt1 = new ScaleTransition();
        tt1.setDuration(Duration.seconds(0.15));
        tt1.setFromX(1.15);
        tt1.setFromY(1.15);
        tt1.setToX(1);
        tt1.setToY(1);
        tt1.setInterpolator(Interpolator.LINEAR);

        pt = new SequentialTransition ();
        pt.getChildren().addAll(tt,tt1);
        pt.setAutoReverse(true);
        pt.setCycleCount(1);
    }

    private void initRandomWord() {
        if(wordFileInfos.isEmpty()){
            noDataTip();
            return;
        }

        changePane = contentPane;
        loadData();

    }


    private void loadSeeSelectData() {

        int size = wordFileInfos.size();
        if(size < 4){
            return;
        }

        Random random = new Random();
        seeMeanList.clear();
        for (int i = 0; i < 4; i++) {

            int nextInt = random.nextInt(size);
            WordFileInfo wordFileInfo = wordFileInfos.get(nextInt);
            seeMeanList.add(wordFileInfo);
        }
        if(!seeMeanList.isEmpty() && seeMeanList.size() == 4){
            int nextInt = random.nextInt(4);
            File file = new File(seeMeanList.get(nextInt).getWordPath());
            Collection<File> collection = FileUtils.listFiles(file, EmptyFileFilter.NOT_EMPTY, DirectoryFileFilter.INSTANCE);
            renderSeeMeanData(collection);

        }else {
            noDataTip();
            return;
        }


    }



    private void loadData() {
        int size = wordFileInfos.size();
        if(size < 2){
            return;
        }

        Random random = new Random();
        int nextInt = random.nextInt(size);
        WordFileInfo wordFileInfo = wordFileInfos.get(nextInt);
        if(wordFileInfo != null){
            File file = new File(wordFileInfo.getWordPath());
            Collection<File> collection = FileUtils.listFiles(file, EmptyFileFilter.NOT_EMPTY, DirectoryFileFilter.INSTANCE);
            renderData(collection);
        }
    }

    private void noDataTip() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        ImageView iv = new ImageView(new Image("/img/team.png", 315, 200, true, true));


        Label label = new Label("暂无数据!");
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("STKaiti",16));
        label.setTextFill(Paint.valueOf("#CCCCCC"));
        vBox.getChildren().addAll(iv,label);
        stackPane.getChildren().remove(contentPane);
        stackPane.getChildren().add(vBox);
        changePane = vBox;
        return;
    }

    private void renderData(Collection<File> collection){
        for (File fileInfo : collection) {
            if(fileInfo.getName().endsWith(".json")){
                String s = null;
                try {
                    s = FileUtils.readFileToString(fileInfo, "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                disposeData();

                WordInfo wordInfo = JSONObject.parseObject(s, WordInfo.class);
                if(wordInfo!=null){
                    word = wordInfo.getWord();
                    list = WordUtil.extractWordCharacters(wordInfo.getWord());
                    String temp = wordInfo.getWord();
                    for (WordIndex vo : list) {
                        StringBuilder sb=new StringBuilder(temp);
                        StringBuilder replace = sb.replace(vo.getIndex(), vo.getIndex(), "_");
                        temp = String.valueOf(replace);
                        JFXTextField field = new JFXTextField();
                        field.setLabelFloat(true);
                        field.setPrefWidth(30);
                        field.setFont(new Font("Heiti",18));
                        field.setUnFocusColor(Color.valueOf("#1ece9a"));
                        field.setId(String.valueOf(vo.getIndex()));
                        field.setOnKeyPressed(event -> {
                            if(KeyCode.ENTER.equals(event.getCode())) {
                                checkAction();
                            }
                        });
                        hBox.getChildren().addAll(field);
                    }

                    wordLabel.setText(temp);
                    cnMeanLabel.setText(wordInfo.getMean_cn());
                    cnMeanLabel.setVisible(false);
                    vbox.getChildren().addAll(wordLabel, cnMeanLabel, hBox);
                    if(isFirstLoad){
                        contentPane.add(vbox,0,0);
                        isFirstLoad =false;
                    }

                    if(!wordInfo.getAccent().isBlank()){

                        centerLabel.setText(wordInfo.getAccent());
                        playBtn.setVisible(true);
                    }
                    if(!wordInfo.getImage_file().isBlank()){
                        imgPath = "file:///"+fileInfo.getParent()+File.separator+wordInfo.getImage_file();
                    }
                    if(!wordInfo.getSentence().isBlank()){
                        sentence = wordInfo.getSentence()+"\n"+wordInfo.getSentence_trans();
                    }

                    if(wordInfo.getWord_audio() != null){

                        disposePlayer();

                        String path = fileInfo.getAbsolutePath();
                        String accent =path.substring(0,path.lastIndexOf(File.separator));

                        String accentPath = accent+File.separator+wordInfo.getWord_audio();
                        if(accentPath.contains(".aac")){
                            wordMediaPath = accentPath.replace(".aac",".mp3");
                        }else {
                            wordMediaPath = accentPath;
                        }
                        wordPlayer = new MediaPlayer(new Media(new File(wordMediaPath).toURI().toString()));
                        wordPlayer.setVolume(0.5);
                        wordPlayer.setRate(1);
                        wordPlayer.setCycleCount(1);
                        wordPlayer.setOnEndOfMedia(() -> {
                            playBtn.setSelected(false);
                        });
                        wordPlayer.play();
                    }

                }
            }
        }
    }

    private void renderSeeMeanData(Collection<File> collection) {
        for (File fileInfo : collection) {
            if(fileInfo.getName().endsWith(".json")){
                String s = null;
                try {
                    s = FileUtils.readFileToString(fileInfo, "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                WordInfo wordInfo = JSONObject.parseObject(s, WordInfo.class);
                if(wordInfo!=null){

                    if(!wordInfo.getMean_cn().isBlank() && !wordInfo.getWord().isBlank()){
                        wordMeanLabel.setText(wordInfo.getMean_cn());
                        randomWordMean = wordInfo.getWord();
                    }

                    if(!wordInfo.getAccent().isBlank()){
                        accentLabel.setText(wordInfo.getAccent());
                        accentLabel.setVisible(false);
                    }

                    if(wordInfo.getWord_audio() != null){

                        disposePlayer();

                        String path = fileInfo.getAbsolutePath();
                        String accent =path.substring(0,path.lastIndexOf(File.separator));

                        String accentPath = accent+File.separator+wordInfo.getWord_audio();
                        if(accentPath.contains(".aac")){
                            wordMediaPath = accentPath.replace(".aac",".mp3");
                        }else {
                            wordMediaPath = accentPath;
                        }
                        wordPlayer = new MediaPlayer(new Media(new File(wordMediaPath).toURI().toString()));
                        wordPlayer.setVolume(0.5);
                        wordPlayer.setRate(1);
                        wordPlayer.setCycleCount(1);
                        wordPlayer.setOnEndOfMedia(() -> {
                            playBtn.setSelected(false);
                        });
                        wordPlayer.play();
                    }
                }
            }
        }

        seeMeanList.stream().sorted((o1, o2) -> o2.hashCode() - o1.hashCode());

        for (int i = 0; i < wordListBox.getChildren().size(); i++) {
            HBox cnMean = (HBox)wordListBox.getChildren().get(i);
            Label mean = (Label) cnMean.getChildren().get(1);
            mean.setText(seeMeanList.get(i).getWordName());
        }

    }

    private void disposeData() {
        if(vbox.getChildren().size() > 0 || hBox.getChildren().size() > 0){
            vbox.getChildren().clear();
            hBox.getChildren().clear();
        }
        imgPath = null;
        sentence =null;
        word = null;
        iv.setImage(null);
        phraseLabel.setText("");
    }

    private void initWordFile() {
        if(filePath == null){
            return;
        }
        Collection<File> files = FileUtils.listFilesAndDirs(new File(filePath), EmptyFileFilter.NOT_EMPTY, DirectoryFileFilter.INSTANCE);
        if(files.isEmpty()){
            return;
        }
        for (File file : files) {
            if(file.isDirectory()){
                if(filePath.equals(file.getAbsolutePath())){

                }else {
                    WordFileInfo info = new WordFileInfo();
                    info.setWordName(file.getName());
                    info.setWordPath(file.getAbsolutePath());
                    wordFileInfos.add(info);
                }
            }

        }
    }

    private void disposePlayer() {
        if(wordPlayer !=null){
            wordPlayer.stop();
            playBtn.setSelected(false);
            wordPlayer.setOnEndOfMedia(null);
            wordPlayer.dispose();
            wordPlayer = null;
        }
    }

    private Stage getStage() {
        Stage stage = (Stage) drawerPane.getScene().getWindow();
        return stage;
    }
}
