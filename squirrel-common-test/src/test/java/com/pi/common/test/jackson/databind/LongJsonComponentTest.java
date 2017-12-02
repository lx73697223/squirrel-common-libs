package com.pi.common.test.jackson.databind;

import com.pi.common.jackson.autoconfigure.PiJacksonAutoConfiguration;
import com.pi.common.jackson.databind.LongJsonComponent;
import com.pi.common.utils.mapper.json.JsonMapper;
import com.pi.common.utils.spring.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(Profiles.UNIT_TEST)
@SpringBootTest(classes = { LongJsonComponentTest.class, LongJsonComponent.class })
@ContextConfiguration(classes = PiJacksonAutoConfiguration.class)
@JsonTest
public class LongJsonComponentTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void serialize() {
        Long id = Long.MAX_VALUE;
        String result = jsonMapper.toJson(id);
        assertThat(result).isEqualTo("\"9223372036854775807\"");
    }

    @Test
    public void deserialize() {
        Long result = jsonMapper.fromJson("\"9223372036854775807\"", Long.class);
        assertThat(result).isEqualTo(Long.MAX_VALUE);
    }

}
