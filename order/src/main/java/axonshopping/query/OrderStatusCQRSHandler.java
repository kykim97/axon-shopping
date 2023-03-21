package axonshopping.query;

import axonshopping.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("orderStatus")
public class OrderStatusCQRSHandler {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @QueryHandler
    public List<OrderStatus> handle(OrderStatusQuery query) {
        return orderStatusRepository.findAll();
    }

    @EventHandler
    public void whenOrderPlaced_then_CREATE_1(OrderPlacedEvent orderPlaced)
        throws Exception {
        // view 객체 생성
        OrderStatus orderStatus = new OrderStatus();
        // view 객체에 이벤트의 Value 를 set 함
        orderStatus.setId(orderPlaced.getId());
        orderStatus.setProductId(orderPlaced.getProductId());
        orderStatus.setQty(orderPlaced.getQty());
        orderStatus.setCustomerId(orderPlaced.getCustomerId());
        orderStatus.setStatus(orderPlaced.getStatus());
        // view 레파지 토리에 save
        orderStatusRepository.save(orderStatus);
    }
}
