package io.vocabulary.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfoenix.controls.*;
import io.vocabulary.utils.ChangeStageUtils;
import io.vocabulary.utils.DialogBuilder;
import io.vocabulary.utils.MyListCell;
import io.vocabulary.model.WordFileInfo;
import io.vocabulary.model.WordInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.EmptyFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Ashin
 * @date 2022-05
 * @Desc
 */
public class VocabularyController {

    public static String rootPath;

    public static void setRootPath(String rootPath) {
        VocabularyController.rootPath = rootPath;
    }

    private static String savePath = System.getProperties().getProperty("user.dir");

    @FXML
    private AnchorPane drawerPane;

    @FXML
    private VBox listBox;

    @FXML
    private TextField searchText;

    @FXML
    private JFXButton orderPlay;

    @FXML
    private JFXButton randomPlay;

    @FXML
    private JFXButton stopPlay;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private AnchorPane imgContentPane;

    @FXML
    private Label wordNameLabel;

    @FXML
    private Label wordLabel;

    @FXML
    private Label accentLabel;

    @FXML
    private Label meanCnLabel;

    @FXML
    private Label wordEtymaLabel;

    @FXML
    private ImageView contentImg;

    @FXML
    private Slider playSlider;

    @FXML
    private JFXRadioButton cnSelected;

    @FXML
    private ToggleGroup languageGroup;

    @FXML
    private ToggleButton isShowSentence;

    @FXML
    private JFXRadioButton enSelected;

    @FXML
    private JFXRadioButton cnAndEnSelected;

    @FXML
    private Label textArea;

    @FXML
    private Label textCnEnShow;

    @FXML
    private Label totalTime;

    @FXML
    private JFXCheckBox isShowImg;

    @FXML
    private JFXCheckBox isPlaySentence;

    @FXML
    private JFXComboBox<Label> wordType;
    @FXML
    private ToggleButton playBtn;
    @FXML
    private ToggleButton sentenceBtn;
    @FXML
    private ListView<WordFileInfo> listView;

    @FXML
    public Label playList;
    @FXML
    public Label wordHistory;

    private boolean showImg = true;

    private boolean visibleImg = true;

    private boolean playSentence = true;

    private boolean isWordPlaying = true;

    private boolean isOrderPlay = true;
    private boolean isAutoNextPlay = false;
    private boolean isFirstLoad = true;


    private List<WordFileInfo>  wordFileInfos = new ArrayList<>();
    private List<WordFileInfo>  historyFileInfos = new ArrayList<>();
    private MediaPlayer wordPlayer;
    private MediaPlayer sentencePlayer;

    private String wordPath = null;
    private String sentencePath = null;

    private String playWordType = "playList";

    private RadioButton radioButton = new RadioButton("英");

    private File historyFile = new File(savePath + File.separator + "cacheData.txt");
    private File wordFile = new File(rootPath);


    private int totalStudyTime = 0;

    private int playIndex;

    private List<String> historyInfos = new ArrayList<>();
    //把事件提取为变量，不用每次都去创建
    private EventHandler<MouseEvent> wordSelectEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getClickCount() == 2) {

                isAutoNextPlay = false;
                WordFileInfo selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem == null) {
                    return;
                }

                if(!historyFile.exists()){

                    try {
                        historyFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                    try {
                        String wordInfo = JSONObject.toJSONString(selectedItem);
                        historyInfos.add(wordInfo);
                        FileUtils.writeLines(historyFile, "UTF-8", historyInfos,";",true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                Collection<File> collection = FileUtils.listFiles(new File(selectedItem.getWordPath()), EmptyFileFilter.NOT_EMPTY, DirectoryFileFilter.INSTANCE);

                if (!CollectionUtils.isEmpty(collection)){
                    renderData(collection);
                }

            }
        }
    };

    private void renderData(Collection<File> collection){
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
                    wordNameLabel.setText(wordInfo.getWord());
                    accentLabel.setText(wordInfo.getAccent());
                    meanCnLabel.setText(wordInfo.getMean_cn());
                    wordEtymaLabel.setText(wordInfo.getWord_etyma());

                    wordLabel.setText("在学单词："+wordInfo.getWord());

                    if(wordInfo.getImage_file() != null){
                        if(showImg){
                            contentImg.setImage(new Image("file:///"+fileInfo.getParent()+File.separator+wordInfo.getImage_file(),360,206,false,true));
                        }else {
                            contentImg.setImage(null);
                        }
                    }

                    if(wordInfo.getWord_audio() != null){

                        disposePlayer();

                        String path = fileInfo.getAbsolutePath();
                        String accent =path.substring(0,path.lastIndexOf(File.separator));

                        String accentPath = accent+File.separator+wordInfo.getWord_audio();
                        if(accentPath.contains(".aac")){
                            wordPath = accentPath.replace(".aac",".mp3");
                        }else {
                            wordPath = accentPath;
                        }
                        wordPlayer = new MediaPlayer(new Media(new File(wordPath).toURI().toString()));
                        wordPlayer.setVolume(0.5);
                        wordPlayer.setRate(1);
                        wordPlayer.setCycleCount(1);
                        wordPlayer.setOnEndOfMedia(() -> {
                            playBtn.setSelected(false);
                            isWordPlaying = false;

                            if(playSentence){
                                createSentencePlayer(wordInfo, accent);
                            }else {
                                if(isAutoNextPlay){
                                    try {
                                        Thread.sleep(2000);
                                        playNextWord();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        wordPlayer.play();
                    }


                    if(isShowSentence.isSelected()){
                        if(radioButton.getText().equals("中英")){
                            if(wordInfo.getSentence() != null && wordInfo.getSentence_trans() != null){
                                textCnEnShow.setText(wordInfo.getSentence()+"\n"+wordInfo.getSentence_trans());
                            }
                        }
                        if(radioButton.getText().equals("中")){
                            if(wordInfo.getSentence_trans() != null){
                                textCnEnShow.setText(wordInfo.getSentence_trans());
                            }
                        }
                        if(radioButton.getText().equals("英")){
                            if(wordInfo.getSentence() != null){
                                textCnEnShow.setText(wordInfo.getSentence());
                            }
                        }
                        if(wordInfo.getSentence_phrase() != null){
                            textArea.setText(wordInfo.getSentence_phrase());
                        }
                    }else {
                        textCnEnShow.setText(null);
                        textArea.setText(null);
                    }


                }
            }
        }
    }




    @FXML
    void initialize() {


        for (int i = 0; i < wordType.getItems().size(); i++) {
            if(i == 0){
                wordType.getItems().get(i).setGraphic(new ImageView(new Image("/img/list.png",20,20,false,true)));
            }
            if(i == 1){
                wordType.getItems().get(i).setGraphic(new ImageView(new Image("/img/clock.png",20,20,false,true)));
            }
        }
        initListView();
        initSearch();
        initSelectProperty();
        initAnimation();
    }

    private void initSearch() {
        searchText.setOnKeyPressed(event -> {
            if(KeyCode.ENTER.equals(event.getCode())) {
                search();
            }
        });
        searchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || "".equals(newValue.trim())) {
                    initListView();
                    search();
                }
            }
        });
    }

    private void initSelectProperty() {


        wordType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {
                if(newValue.getText() != null){
                    playWordType = newValue.getId();
                    initListView();
                }
            }
        });

        isShowImg.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                showImg = newValue;
            }
        });
        isPlaySentence.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                playSentence = newValue;
            }
        });

        languageGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                radioButton = (RadioButton) newValue;
            }
        });
    }

    private void initAnimation() {
        EventHandler<ActionEvent> eventHandler = e -> {
            totalStudyTime++;
            int hour = totalStudyTime / 3600;
            int minute = (totalStudyTime - hour * 3600) / 60;
            int second = (totalStudyTime - hour * 3600 - minute * 60);
            String h = hour < 10?"0"+hour:hour+"";
            String m = minute < 10?"0"+minute:minute+"";
            String s = second < 10?"0"+second:second+"";
            totalTime.setText("学习总时长："+h+":"+m+":"+s);
        };

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));

        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void search() {
        String word = searchText.getText();
        if(word == null || "".equals(word.trim())) {
            listView.setItems(FXCollections.observableArrayList(wordFileInfos));
            return;
        }
        List<WordFileInfo> curList = new ArrayList<>();
        for (WordFileInfo info : wordFileInfos) {
            if(String.valueOf(info.getWordName()).contains(word)) {
                curList.add(info);
            }
        }
        listView.setItems(FXCollections.observableArrayList(curList));
    }


    private void initListView() {

        if(playWordType.equalsIgnoreCase("wordHistory")){

            if(historyFile.exists()){
                String s;
                try {
                    s = FileUtils.readFileToString(historyFile, "UTF-8");
                    if(s ==null){

                    }
                    s = s.substring(0,s.length() - 1);

                    List<String> list = splitValue(s);
                    if(!list.isEmpty()){
                        List<String> collect = list.stream().distinct().collect(Collectors.toList());
                        for (String wordInfo : collect) {
                            WordFileInfo wordFileInfo = JSONObject.parseObject(wordInfo, WordFileInfo.class);
                            historyFileInfos.add(wordFileInfo);
                        }

                        listView.setItems(FXCollections.observableArrayList(historyFileInfos));
                    }else {
                        listView.setItems(null);
                        listView.setPlaceholder(new Label("暂无数据"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                listView.setItems(null);
                listView.setPlaceholder(new Label("暂无数据"));
            }
        }else {

            if(isFirstLoad){
                isFirstLoad =false;

                if(!wordFile.exists()){
                    listView.setPlaceholder(new Label("暂无数据"));

                }else {

                    Collection<File> files = FileUtils.listFilesAndDirs(wordFile, EmptyFileFilter.NOT_EMPTY, DirectoryFileFilter.INSTANCE);
                    for (File file : files) {
                        if(file.isDirectory()){

                            if(rootPath.equals(file.getAbsolutePath())){

                            }else {
                                WordFileInfo info = new WordFileInfo();
                                info.setWordName(file.getName());
                                info.setWordPath(file.getAbsolutePath());
                                wordFileInfos.add(info);
                            }
                        }

                    }
                    listView.setItems(FXCollections.observableArrayList(wordFileInfos));

                }
            }else {

                if(wordFileInfos.isEmpty()){
                    listView.setPlaceholder(new Label("暂无数据"));
                }else {
                    listView.setItems(FXCollections.observableArrayList(wordFileInfos));
                }

            }
        }




        listView.setCellFactory(fileListView -> new MyListCell());
        //双击单词获取详情
        listView.addEventHandler(MouseEvent.MOUSE_CLICKED, wordSelectEvent);

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
    void showHideImgEvent(MouseEvent event) {

        if(event.getClickCount() == 2){
            if(visibleImg){
                contentImg.setVisible(false);
                visibleImg = false;
            }else {
                contentImg.setVisible(true);
                visibleImg = true;
            }
        }
    }


    @FXML
    void orderPlayAction(MouseEvent event) {

        playNextWord();
        isAutoNextPlay = true;

        SortedList<WordFileInfo> sorted = listView.getItems().sorted(Comparator.comparingInt(o -> o.getWordName().charAt(0)));

        listView.setItems(sorted);
    }

    @FXML
    void randomPlayAction(MouseEvent event) {

        playRandomWord();
        isAutoNextPlay = true;

        SortedList<WordFileInfo> sorted = listView.getItems().sorted(Comparator.comparingInt(o -> o.getWordName().charAt(o.getWordName().length() -1)));

        listView.setItems(sorted);
    }

    @FXML
    void stopPlayAction(MouseEvent event) {
        disposePlayer();
    }

    @FXML
    void exerciseAction(MouseEvent event) {
        VocabularySteadyController.setFilePath(rootPath);
        VocabularySteadyController.setParentStage(getStage());
        ChangeStageUtils.changeStage(getStage(),"/fxml/vocabularySteady.fxml");
    }

    private void playRandomWord() {
        int size = listView.getItems().size();
        if(size < 2){
            return;
        }

        Random random = new Random();
        playIndex = random.nextInt(size);
        listView.getSelectionModel().select(playIndex);
        WordFileInfo wordFileInfo = listView.getItems().get(playIndex);

        //设置滚动到哪儿
        listView.scrollTo(playIndex);

        if(wordFileInfo != null){
            File file = new File(wordFileInfo.getWordPath());
            Collection<File> collection = FileUtils.listFiles(file, EmptyFileFilter.NOT_EMPTY, DirectoryFileFilter.INSTANCE);
            renderData(collection);
        }
    }

    public void playNextWord() {
        int size = listView.getItems().size();
        if(size < 2){
            return;
        }
        playIndex = listView.getSelectionModel().getSelectedIndex();
        //如果是最后一个就跳第一个
        playIndex = (playIndex == size -1) ? 0 : playIndex + 1;
        listView.getSelectionModel().select(playIndex);
        //设置滚动到哪儿
        listView.scrollTo(playIndex);

        WordFileInfo wordFileInfo = listView.getItems().get(playIndex);
        if(wordFileInfo != null){
            File file = new File(wordFileInfo.getWordPath());
            Collection<File> collection = FileUtils.listFiles(file, EmptyFileFilter.NOT_EMPTY, DirectoryFileFilter.INSTANCE);
            renderData(collection);
        }
    }

    @FXML
    void onSentencePlayAction(ActionEvent event) {
        if(sentencePlayer != null){
            if(sentenceBtn.isSelected()){
                if(sentencePlayer !=null){
                    sentencePlayer.stop();
                    sentencePlayer.setOnEndOfMedia(null);
                    sentencePlayer.dispose();
                    sentencePlayer = null;
                }
                if(!isWordPlaying){
                    initSentencePlayer();
                }
            }else {
                sentencePlayer.stop();
            }
        }
    }


    @FXML
    void onWordPlayAction(ActionEvent event) {
        if(wordPlayer != null){
            if(playBtn.isSelected()){

                if(sentencePlayer !=null){
                    sentencePlayer.stop();
                    sentenceBtn.setSelected(false);
                }
                wordPlayer = new MediaPlayer(new Media(new File(wordPath).toURI().toString()));
                wordPlayer.setVolume(0.5);
                wordPlayer.setRate(1);
                wordPlayer.setCycleCount(1);
                wordPlayer.setOnEndOfMedia(() -> {
                    playBtn.setSelected(false);
                    isWordPlaying = false;
                });
                wordPlayer.play();
            }else {
                wordPlayer.stop();
            }
        }
    }


    private void createSentencePlayer(WordInfo wordInfo, String accent) {
        if(wordInfo.getSentence_audio() != null){

            String sentenceAudioPath = accent +File.separator+ wordInfo.getSentence_audio();
            if(sentenceAudioPath.contains(".aac")){

                sentencePath = sentenceAudioPath.replace(".aac",".mp3");
            }else {
                sentencePath = sentenceAudioPath;
            }
            sentencePlayer = new MediaPlayer(new Media(new File(sentencePath).toURI().toString()));
            sentencePlayer.setVolume(0.5);
            sentencePlayer.setRate(1);
            sentencePlayer.setCycleCount(1);
            sentencePlayer.setOnEndOfMedia(() -> {
                sentenceBtn.setSelected(false);
                playSlider.setValue(0);

                if(isAutoNextPlay){
                    try {
                        Thread.sleep(2000);
                        playNextWord();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });

            initSlider();

        }

    }

    private void initSlider() {

        sentencePlayer.setOnReady(new Runnable() {
            @Override
            public void run() {

                playSlider.setMin(0);
                playSlider.setValue(0);
                playSlider.setMax(sentencePlayer.getTotalDuration().toSeconds());

                sentencePlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                        playSlider.setValue(newValue.toSeconds());

                    }
                });
            }
        });

        if(!isWordPlaying){
            sentencePlayer.play();
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
        if(sentencePlayer !=null){
            sentencePlayer.stop();
            sentenceBtn.setSelected(false);
            sentencePlayer.setOnEndOfMedia(null);
            sentencePlayer.dispose();
            sentencePlayer = null;
        }
    }
    private void initSentencePlayer() {
        sentencePlayer = new MediaPlayer(new Media(new File(sentencePath).toURI().toString()));
        sentencePlayer.setVolume(0.5);
        sentencePlayer.setRate(1);
        sentencePlayer.setCycleCount(1);
        sentencePlayer.setOnEndOfMedia(() -> {
            sentenceBtn.setSelected(false);
            playSlider.setValue(0);
        });
        initSlider();
    }

    private Stage getStage() {
        Stage stage = (Stage) drawerPane.getScene().getWindow();
        return stage;
    }

    public static List<String> splitValue(String data) {
        if (data.isEmpty()) {
            return null;
        }
        String[] split = data.split(";");
        return Arrays.asList(split);
    }
}
