package com.pi.common.test;

import com.pi.common.utils.spring.Profiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

@ActiveProfiles(Profiles.UNIT_TEST)
@SpringBootTest
public abstract class ServiceTests extends AbstractTransactionalTestNGSpringContextTests {

}
