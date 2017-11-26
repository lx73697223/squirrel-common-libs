package com.pi.common.test;

import com.pi.common.utils.exceptions.PiRuntimeException;
import com.pi.common.utils.spring.Profiles;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(Profiles.UNIT_TEST)
@SpringBootTest
public abstract class DaoTests extends AbstractTransactionalTestNGSpringContextTests {

    public void executeAndCheckOptismitcLocking(Supplier<Integer> daoFunction) {
        try {
            daoFunction.get();
        } catch (MyBatisSystemException e) {
            if (e.getMostSpecificCause() instanceof PiRuntimeException) {
                PiRuntimeException ex = (PiRuntimeException) e.getMostSpecificCause();
                assertThat(ex.getStatus()).isEqualTo(HttpStatus.CONFLICT);
            } else {
                throw e;
            }
        }
    }
}
