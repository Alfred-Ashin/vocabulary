package io.vocabulary.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Ashin
 * @date 2022-05
 * @Desc
 */
public class WordInfo implements Serializable {

    private String word;


    private String word_audio;

    private String image_file;

    private String accent;

    private String mean_cn;

    private String mean_en;


    private String deformation_img;

    private String sentence;

    private String sentence_phrase;


    private String sentence_trans;

    private String sentence_audio;


    private String word_etyma;


    public WordInfo() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord_audio() {
        return word_audio;
    }

    public void setWord_audio(String word_audio) {
        this.word_audio = word_audio;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public String getAccent() {
        return accent;
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public String getMean_cn() {
        return mean_cn;
    }

    public void setMean_cn(String mean_cn) {
        this.mean_cn = mean_cn;
    }

    public String getMean_en() {
        return mean_en;
    }

    public void setMean_en(String mean_en) {
        this.mean_en = mean_en;
    }

    public String getDeformation_img() {
        return deformation_img;
    }

    public void setDeformation_img(String deformation_img) {
        this.deformation_img = deformation_img;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentence_phrase() {
        return sentence_phrase;
    }

    public void setSentence_phrase(String sentence_phrase) {
        this.sentence_phrase = sentence_phrase;
    }

    public String getSentence_trans() {
        return sentence_trans;
    }

    public void setSentence_trans(String sentence_trans) {
        this.sentence_trans = sentence_trans;
    }

    public String getSentence_audio() {
        return sentence_audio;
    }

    public void setSentence_audio(String sentence_audio) {
        this.sentence_audio = sentence_audio;
    }

    public String getWord_etyma() {
        return word_etyma;
    }

    public void setWord_etyma(String word_etyma) {
        this.word_etyma = word_etyma;
    }

    @Override
    public String toString() {
        return "WordInfo{" +
                "word='" + word + '\'' +
                ", word_audio='" + word_audio + '\'' +
                ", image_file='" + image_file + '\'' +
                ", accent='" + accent + '\'' +
                ", mean_cn='" + mean_cn + '\'' +
                ", mean_en='" + mean_en + '\'' +
                ", deformation_img='" + deformation_img + '\'' +
                ", sentence='" + sentence + '\'' +
                ", sentence_phrase='" + sentence_phrase + '\'' +
                ", sentence_trans='" + sentence_trans + '\'' +
                ", sentence_audio='" + sentence_audio + '\'' +
                ", word_etyma='" + word_etyma + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordInfo wordInfo = (WordInfo) o;
        return Objects.equals(word, wordInfo.word) && Objects.equals(word_audio, wordInfo.word_audio) && Objects.equals(image_file, wordInfo.image_file) && Objects.equals(accent, wordInfo.accent) && Objects.equals(mean_cn, wordInfo.mean_cn) && Objects.equals(mean_en, wordInfo.mean_en) && Objects.equals(deformation_img, wordInfo.deformation_img) && Objects.equals(sentence, wordInfo.sentence) && Objects.equals(sentence_phrase, wordInfo.sentence_phrase) && Objects.equals(sentence_trans, wordInfo.sentence_trans) && Objects.equals(sentence_audio, wordInfo.sentence_audio) && Objects.equals(word_etyma, wordInfo.word_etyma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, word_audio, image_file, accent, mean_cn, mean_en, deformation_img, sentence, sentence_phrase, sentence_trans, sentence_audio, word_etyma);
    }
}
