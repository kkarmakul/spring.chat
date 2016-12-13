package edu.kkarmakul.spring.security.chat.server.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerInterceptor {
    private static final Logger log =
            LoggerFactory.getLogger(ControllerInterceptor.class);

    @Before(value = "execution(* edu.kkarmakul.spring.security.chat.server.controller.ChatController.add*(String)) and args(message)")
    public void logBefore(JoinPoint point, String message) {
        log.debug("Started : {} with \"{}\"",
                point.getSignature().getName(), message);
    }

    @AfterReturning(value = "execution(* edu.kkarmakul.spring.security.chat.server.controller.ChatController.add*(..))", returning = "result")
    public void logAfter(JoinPoint point, String result) {
        log.debug("Finished: {} with \"{}\"",
                point.getSignature().getName(), result);
    }

}
