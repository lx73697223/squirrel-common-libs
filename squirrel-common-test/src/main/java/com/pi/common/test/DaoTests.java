package com.pi.common.test;

import com.pi.common.utils.exceptions.PiRuntimeException;
import com.pi.common.utils.spring.Profiles;
import org.junit.runner.RunWith;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@RunWith(SpringRunner.class)
@ActiveProfiles(Profiles.UNIT_TEST)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class DaoTests {

    public void executeAndCheckOptimisticLocking(Supplier<Integer> daoFunction) {
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
