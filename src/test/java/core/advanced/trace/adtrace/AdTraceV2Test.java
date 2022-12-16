package core.advanced.trace.adtrace;

import core.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

public class AdTraceV2Test {

    @Test
    void begin_end() {
        AdTraceV2 trace = new AdTraceV2();
        TraceStatus status1 = trace.begin("Advanced");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "Advanced2");
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void begin_exception() {
        AdTraceV2 trace = new AdTraceV2();
        TraceStatus status1 = trace.begin("Advanced");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "Advanced2");
        trace.exception(status2, new IllegalStateException());
        trace.exception(status1, new IllegalStateException());
    }
}