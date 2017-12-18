package com.pi.common.utils.core.sequence;

import org.junit.Test;

public class SnowflakeIdGeneratorTest {

    @Test
    public void next() {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(0);
        long id = snowflakeIdGenerator.next();
        System.out.println("id = " + id);
    }

}
