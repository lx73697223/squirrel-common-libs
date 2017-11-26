package com.pi.common.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@SpringBootTest(classes = TestConfig.class)
@SpringBootConfiguration
public class TestConfigurationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TestProperties foo;

    @Test
    public void testPlusCount()
            throws Exception {
        assertEquals(foo.getName(), "from test config");
    }

}
