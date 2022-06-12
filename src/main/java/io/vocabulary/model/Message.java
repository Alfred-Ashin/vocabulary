package io.vocabulary.model;

import lombok.Data;

import java.io.File;

/**
 * @author Ashin
 * @date 2022/6/2
 * @Desc
 */
@Data
public class Message {
    private  String savePath;
    private  String realPath;
    private boolean flag;
}
