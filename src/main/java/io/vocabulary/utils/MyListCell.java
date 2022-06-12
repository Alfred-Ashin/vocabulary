package io.vocabulary.utils;

import io.vocabulary.model.WordFileInfo;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

/**
 *
 * @author Ashin
 * @date 2022-05
 * @Desc
 */
public class MyListCell extends ListCell<WordFileInfo> {

    private final Label label;
    private final VBox pane;

    public MyListCell() {
        pane = new VBox();
        label = new Label();
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.getChildren().add(label);
        setGraphic(pane);
    }

    @Override
    protected void updateItem(WordFileInfo file, boolean b) {
        super.updateItem(file, b);

        if(file == null || b){
            setGraphic(null);
            setText("");
        }else {
            String name = file.getWordName();
            label.setText(name);
            setGraphic(pane);
        }
    }
}
