package com.pi.common.utils.mapper.json;

import java.io.Reader;
import java.util.List;

public interface JsonMapper {

    public String toJson(Object object);

    public byte[] toJsonAsBytes(Object object);

    public <T> T fromJson(String jsonString, Class<T> clazz);

    public <T> T fromJson(Reader source, Class<T> clazz);

    public <T> List<T> fromCollectionJson(String jsonString, Class<T> clazz);

    public <T> List<T> fromCollectionJson(Reader source, Class<T> clazz);

}
