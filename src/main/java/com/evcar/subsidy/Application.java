package com.evcar.subsidy;

import com.evcar.subsidy.util.Constant;
import com.evcar.subsidy.util.ESTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{Application.class}, args);
    }

    private Logger _logger;

    @Override
    public void run(String... args) throws Exception {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        System.out.println("Active log4j config file: " + config.getName());
        this._logger = LoggerFactory.getLogger(Application.class);
        this._logger.info("appver: " + (new GitVer()).getVersion());

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            _logger.info("shutdown ...");
            ESTools.clearClient();
        }));
    }
}