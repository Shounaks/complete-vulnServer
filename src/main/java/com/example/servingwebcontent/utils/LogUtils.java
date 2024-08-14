package com.example.servingwebcontent.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@UtilityClass
public class LogUtils {
    public static void logging(Logger logger, String string) {
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("logs/logfile.txt", 0, 1, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.log(Level.WARNING, string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileHandler != null) fileHandler.close();
        }
    }
}
