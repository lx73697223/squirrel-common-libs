package com.pi.common.utils.aop;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.pi.common.utils.aop.annotation.LogMethodReturn;
import com.pi.common.utils.core.ReflectionUtils;
import com.pi.common.utils.mapper.json.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LogMethodReturnIntercept {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogMethodReturnIntercept.class);

    @Autowired(required = false)
    private JsonMapper jsonMapper;

    @Pointcut("@annotation(com.pi.common.utils.aop.annotation.LogMethodReturn)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Object reval = pjp.proceed();

        doAfter(pjp, stopwatch, reval);
        return reval;
    }

    public void doAfter(ProceedingJoinPoint jp, Stopwatch stopwatch, Object reval) {
        try {
            stopwatch.stop();

            Method method = ReflectionUtils.getMethod(jp);
            LogMethodReturn anno = method.getAnnotation(LogMethodReturn.class);

            Logger logger = null;
            if (StringUtils.isEmpty(anno.loggerName())) {
                logger = LOGGER;
            } else {
                logger = LoggerFactory.getLogger(anno.loggerName());
            }

            StringBuilder sb = new StringBuilder("log intercept:");
            String invokeObject = method.getDeclaringClass().getName() + "." + method.getName();
            sb.append(invokeObject);
            List<LogMethodReturn.LogContent> logContents = Lists.newArrayList(anno.logContent());
            if (logContents.contains(LogMethodReturn.LogContent.ARGS)) {
                sb.append(" args=[");
                Parameter[] parameters = method.getParameters();
                if (parameters != null) {
                    for (int i = 0; i < parameters.length; i++) {
                        if (i > 0) {
                            sb.append("&");
                        }
                        Parameter parameter = parameters[i];
                        String value = jp.getArgs()[i] == null ? "" : jsonMapper.toJson(jp.getArgs()[i]);
                        sb.append(parameter.getName() + "=" + value);
                    }
                    sb.append("]");
                }
            }

            if (logContents.contains(LogMethodReturn.LogContent.RETURN_CONTENT)) {
                sb.append(" response=[");
                if (reval != null) {
                    sb.append(jsonMapper.toJson(reval));
                }
                sb.append("]");
            }

            if (logContents.contains(LogMethodReturn.LogContent.COST_TIME)) {
                sb.append(" costTime=[").append(stopwatch.elapsed(TimeUnit.MILLISECONDS)).append("]");
            }
            String logStr = sb.toString();
            log(logger, anno, logStr);
        } catch (Exception e) {
            LOGGER.error("LoggerIntercept error ", e);
        }
    }

    private void log(Logger logger, LogMethodReturn anno, String logStr) {
        switch (anno.level()) {
            case DEBUG:
                logger.debug(logStr);
                break;
            case INFO:
                logger.info(logStr);
                break;
            case WARN:
                logger.warn(logStr);
            default:
                logger.debug(logStr);
                break;
        }
    }

}
