package core.advanced.trace.strategy.code.template;

import core.advanced.trace.strategy.code.strategy.Strategy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeLogTemplate {

    public void execute(Callback callback) {
        long startTime = System.currentTimeMillis();
        callback.call();
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}
