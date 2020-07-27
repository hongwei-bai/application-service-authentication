package com.hongwei.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogWrapper {
    private static final Logger logger = LoggerFactory.getLogger(LogWrapper.class);

    public static void debug(String msg) {
        logger.debug(msg);
        System.out.println(msg);
    }

    public static void info(String msg) {
        logger.info(msg);
        System.out.println(msg);
    }

}
