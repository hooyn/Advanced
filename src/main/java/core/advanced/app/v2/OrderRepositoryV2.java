package core.advanced.app.v2;

import core.advanced.trace.TraceId;
import core.advanced.trace.TraceStatus;
import core.advanced.trace.adtrace.AdTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {

    private final AdTraceV2 trace;

    public void save(TraceId traceId, String itemId) {
        TraceStatus status = null;
        try{
            status = trace.beginSync(traceId, "OrderRepository.request()");

            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);

            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e; // 예외를 꼭 다시 던져주어야 합니다.
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
