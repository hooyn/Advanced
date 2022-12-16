package core.advanced.app.v5;

import core.advanced.trace.callback.TraceCallback;
import core.advanced.trace.callback.TraceTemplate;
import core.advanced.trace.logtrace.LogTrace;
import core.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate template;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace);
    }

    @GetMapping("/v5/request")
    public String request(String itemId){
        
        return template.execute("OrderController.request()", new TraceCallback<>() {
            @Override
            public String call() {
                orderService.orderItem(itemId);
                return "OK";
            }
        });
    }
}