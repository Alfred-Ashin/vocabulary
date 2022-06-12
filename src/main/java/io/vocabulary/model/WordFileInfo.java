package io.vocabulary.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Ashin
 * @date 2022-05
 * @Desc
 */
public class WordFileInfo implements Serializable {

    private String wordName;

    private String wordPath;

    public WordFileInfo() {
    }

    public WordFileInfo(String wordName, String wordPath) {
        this.wordName = wordName;
        this.wordPath = wordPath;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordPath() {
        return wordPath;
    }

    public void setWordPath(String wordPath) {
        this.wordPath = wordPath;
    }

    @Override
    public String toString() {
        return "WordFileInfo{" +
                "wordName='" + wordName + '\'' +
                ", wordPath='" + wordPath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordFileInfo that = (WordFileInfo) o;
        return Objects.equals(wordName, that.wordName) && Objects.equals(wordPath, that.wordPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordName, wordPath);
    }
}
