package com.pirate.esredisdemo.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LogUtil {
    public static final Logger COMMON_LOGGER = LoggerFactory.getLogger("commonLogger");
    public static final Logger BANK_LOGGER = LoggerFactory.getLogger("bankLogger");

    public static void main(String[] args) {
        COMMON_LOGGER.info("11");
    }
}
