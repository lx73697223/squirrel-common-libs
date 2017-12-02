package com.pi.common.test.jackson.databind.entity;

import com.pi.common.jackson.databind.UrlJsonSerializer;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class PiTestUrlJsonSerializer extends UrlJsonSerializer {

    public static final String BaseUrl = "http://a3.topitme.com/";

    @Override
    protected String getBaseUrl() {
        return BaseUrl;
    }

    @Override
    protected String getDefaultUrl() {
        return "http://img2.imgtn.bdimg.com/it/u=1188970075,4160509163&fm=27&gp=0.jpg";
    }
}
