package core.advanced.trace.adtrace;

import core.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

public class AdTraceV1Test {

    @Test
    void begin_end() {
        AdTraceV1 trace = new AdTraceV1();
        TraceStatus status = trace.begin("Advanced");
        trace.end(status);
    }

    @Test
    void begin_exception() {
        AdTraceV1 trace = new AdTraceV1();
        TraceStatus status = trace.begin("Advanced");
        trace.exception(status, new IllegalStateException());
    }
}