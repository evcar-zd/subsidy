package com.evcar.subsidy;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloTest {
    private static Logger s_logger = LoggerFactory.getLogger(HelloTest.class);

    @Test
    public void HelloTestFoo(){
        int a = 12;
        int b = 5;
        int c = a % b;

        s_logger.info("{} mod {} = {}", a, b, c);
        Assert.assertTrue("A junit test sample", c == 2);
    }
}
