package com.pi.common.jackson.databind;

import com.pi.common.jackson.autoconfigure.PiJacksonAutoConfiguration;
import com.pi.common.jackson.databind.entity.PiTestJsonComponentEntity;
import com.pi.common.utils.mapper.json.JsonMapper;
import com.pi.common.jackson.databind.entity.PiTestUrlJsonSerializer;
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
@SpringBootTest(classes = { UrlJsonSerializerTest.class, PiTestUrlJsonSerializer.class,
                                  LongJsonComponent.class })
@ContextConfiguration(classes = PiJacksonAutoConfiguration.class)
@JsonTest
public class UrlJsonSerializerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void serialize() {
        PiTestJsonComponentEntity entity = new PiTestJsonComponentEntity();
        entity.setId(123L);
        entity.setPhotoUrl("0/1c/12/1128107705fd5121c0l.jpg");
        String result = jsonMapper.toJson(entity);
        assertThat(result).isEqualTo(
                String.format("{\"id\":\"%s\",\"photoUrl\":\"%s\"}", entity.getId(),
                              PiTestUrlJsonSerializer.BaseUrl.concat(entity.getPhotoUrl())));
    }

}
