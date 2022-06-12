package io.vocabulary.app;

import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Ashin
 * @date 2022-05
 * @Desc
 */
public class MainApp {

    private static Logger logger = LogManager.getLogger(MainApp.class);

    public static void main(String[] args) {
        // 捕捉未处理的异常
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                // 抛出栈信息
                logger.error("", e);
            }
        });

        Application.launch(VocabularySelector.class,args);
    }
}
