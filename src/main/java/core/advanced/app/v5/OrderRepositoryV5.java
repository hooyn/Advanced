package core.advanced.app.v5;

import core.advanced.trace.callback.TraceTemplate;
import core.advanced.trace.logtrace.LogTrace;
import core.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.logging.LogRecord;

import static java.lang.Thread.sleep;

@Repository
public class OrderRepositoryV5 {

    private final TraceTemplate template;

    public OrderRepositoryV5(LogTrace trace) {
        this.template = new TraceTemplate(trace);
    }

    public void save(String itemId) {

        template.execute("OrderRepository.request()", () -> {
            try {
                if (itemId.equals("ex"))
                    throw new IllegalStateException("예외 발생!");

                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }
}
