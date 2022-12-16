package core.advanced.trace.logtrace;

import core.advanced.trace.TraceId;
import core.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X--";

    //private TraceId traceIdHolder; // traceId 동기화, 동시성 이슈 발생
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }

        releaseTraceId();
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()){
            traceIdHolder.remove(); // destroy
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }

    private Object addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "|   ");
        }

        return sb.toString();
    }
}

//출력 값
//2022-12-16T13:49:24.163+09:00  INFO 19976 --- [nio-8080-exec-2] c.a.trace.logtrace.ThreadLocalLogTrace   : [89d40b1b] OrderController.request()
//2022-12-16T13:49:24.163+09:00  INFO 19976 --- [nio-8080-exec-2] c.a.trace.logtrace.ThreadLocalLogTrace   : [89d40b1b] |-->OrderService.request()
//2022-12-16T13:49:24.163+09:00  INFO 19976 --- [nio-8080-exec-2] c.a.trace.logtrace.ThreadLocalLogTrace   : [89d40b1b] |   |-->OrderRepository.request()
//2022-12-16T13:49:24.305+09:00  INFO 19976 --- [nio-8080-exec-7] c.a.trace.logtrace.ThreadLocalLogTrace   : [cb897714] OrderController.request()
//2022-12-16T13:49:24.305+09:00  INFO 19976 --- [nio-8080-exec-7] c.a.trace.logtrace.ThreadLocalLogTrace   : [cb897714] |-->OrderService.request()
//2022-12-16T13:49:24.305+09:00  INFO 19976 --- [nio-8080-exec-7] c.a.trace.logtrace.ThreadLocalLogTrace   : [cb897714] |   |-->OrderRepository.request()
//2022-12-16T13:49:25.171+09:00  INFO 19976 --- [nio-8080-exec-2] c.a.trace.logtrace.ThreadLocalLogTrace   : [89d40b1b] |   |<--OrderRepository.request() time=1008ms
//2022-12-16T13:49:25.171+09:00  INFO 19976 --- [nio-8080-exec-2] c.a.trace.logtrace.ThreadLocalLogTrace   : [89d40b1b] |<--OrderService.request() time=1008ms
//2022-12-16T13:49:25.171+09:00  INFO 19976 --- [nio-8080-exec-2] c.a.trace.logtrace.ThreadLocalLogTrace   : [89d40b1b] OrderController.request() time=1008ms
//2022-12-16T13:49:25.312+09:00  INFO 19976 --- [nio-8080-exec-7] c.a.trace.logtrace.ThreadLocalLogTrace   : [cb897714] |   |<--OrderRepository.request() time=1007ms
//2022-12-16T13:49:25.312+09:00  INFO 19976 --- [nio-8080-exec-7] c.a.trace.logtrace.ThreadLocalLogTrace   : [cb897714] |<--OrderService.request() time=1007ms
//2022-12-16T13:49:25.312+09:00  INFO 19976 --- [nio-8080-exec-7] c.a.trace.logtrace.ThreadLocalLogTrace   : [cb897714] OrderController.request() time=1007ms
