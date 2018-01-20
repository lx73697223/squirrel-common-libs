package com.pi.common.test;

import com.pi.common.utils.spring.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

@ActiveProfiles(Profiles.FUNCTIONAL_TEST)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class FunctionalTests extends AbstractTestNGSpringContextTests {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Value("${local.server.port}")
    protected int port;

    private String baseUrl;

    public String getBaseUrl() {
        if (baseUrl == null) {
            baseUrl = "http://localhost:" + port;
        }
        return baseUrl;
    }

    @BeforeClass
    public void setup() {
        testRestTemplate.setUriTemplateHandler(new RootUriTemplateHandler(baseUrl));
    }

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
