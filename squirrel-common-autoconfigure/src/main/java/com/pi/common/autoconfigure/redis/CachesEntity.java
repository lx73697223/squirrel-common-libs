package com.pi.common.autoconfigure.redis;

public interface CachesEntity {

    int DEFAULT_MAXSIZE = 50000;

    int getMaxSize();

    int getTtl();

}
