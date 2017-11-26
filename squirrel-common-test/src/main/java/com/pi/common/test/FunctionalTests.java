package com.pi.common.test;

import com.pi.common.utils.spring.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ActiveProfiles(Profiles.FUNCTIONAL_TEST)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class FunctionalTests extends AbstractTestNGSpringContextTests {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected HttpEntity<Void> createRequestEntity() {
        return createRequestEntity(null);
    }

    protected <T> HttpEntity<T> createRequestEntity(T body) {
        return createRequestEntity(CommonTestConstants.DEFAULT_ID_TOKEN, body);
    }

    protected <T> HttpEntity<T> createRequestEntity(String accessToken, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        return new HttpEntity<>(body, headers);
    }

}
