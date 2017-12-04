package com.pi.common.jackson.databind;

import com.google.common.collect.Sets;
import com.pi.common.jackson.autoconfigure.PiJacksonAutoConfiguration;
import com.pi.common.jackson.databind.enums.PiTestEnumEntity;
import com.pi.common.jackson.databind.enums.PiTestNamedEnumEntity;
import com.pi.common.utils.mapper.json.JsonMapper;
import com.pi.common.utils.spring.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(Profiles.UNIT_TEST)
@SpringBootTest(classes = { EnumEntityJsonComponentTest.class, EnumEntityJsonComponent.class })
@ContextConfiguration(classes = PiJacksonAutoConfiguration.class)
@JsonTest
public class EnumEntityJsonComponentTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void serialize() {
        com.pi.common.jackson.databind.enums.PiTestEnumEntity[] piTestEnumEntities = PiTestEnumEntity.values();
        String result = jsonMapper.toJson(piTestEnumEntities);
        assertThat(result).isEqualTo("[1,2,3]");
    }
    @Test
    public void serializeNamedEnumEntity() {
        PiTestNamedEnumEntity[] piTestEnumEntities = PiTestNamedEnumEntity.values();
        String result = jsonMapper.toJson(piTestEnumEntities);
        assertThat(result).isEqualTo("[{\"id\":1,\"name\":\"壹\",\"shortName\":\"一\"},{\"id\":2,\"name\":\"贰\",\"shortName\":\"二\"},{\"id\":3,\"name\":\"叁\",\"shortName\":\"三\"}]");
    }

    @Test
    public void deserialize() {
        PiTestEnumEntity result = jsonMapper.fromJson("1", PiTestEnumEntity.class);
        assertThat(result).isEqualTo(PiTestEnumEntity.ENUM_ONE);
    }

    @Test
    public void deserializeCollection() {
        List<PiTestEnumEntity> result = jsonMapper.fromCollectionJson("[1,2,3]", PiTestEnumEntity.class);
        assertThat(result).hasSameElementsAs(Sets.newHashSet(PiTestEnumEntity.values()));
    }

}
