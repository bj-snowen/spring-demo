package com.vdrips.test.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by baixf on 2017/3/27.
 */
public class LogWrite {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public void debug(){
        logger.debug("debug test");
    }

    public void info(){
        logger.info("info test");
    }

    public void error(){
        logger.error("error test");
    }
}
