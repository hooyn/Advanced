package core.advanced.app.v5;

import core.advanced.trace.callback.TraceCallback;
import core.advanced.trace.callback.TraceTemplate;
import core.advanced.trace.logtrace.LogTrace;
import core.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceV5 {

    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate template;

    public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace trace) {
        this.orderRepository = orderRepository;
        this.template = new TraceTemplate(trace);
    }

    public void orderItem(String itemId){
        template.execute("OrderService.request()", () -> {
            orderRepository.save(itemId);
            return null;
        });
    }
}
