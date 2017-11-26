package com.pi.common.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import com.pi.common.utils.spring.Profiles;

@ActiveProfiles(Profiles.UNIT_TEST)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public abstract class ServiceTests extends AbstractTransactionalTestNGSpringContextTests {

}
