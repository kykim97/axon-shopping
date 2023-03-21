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
@ProcessingGroup("deliveryStatus")
public class DeliveryStatusCQRSHandler {

    @Autowired
    private DeliveryStatusRepository deliveryStatusRepository;

    @QueryHandler
    public List<DeliveryStatus> handle(DeliveryStatusQuery query) {
        return deliveryStatusRepository.findAll();
    }

    @EventHandler
    public void whenDeliveryStarted_then_CREATE_1(
        DeliveryStartedEvent deliveryStarted
    ) throws Exception {
        // view 객체 생성
        DeliveryStatus deliveryStatus = new DeliveryStatus();
        // view 객체에 이벤트의 Value 를 set 함
        deliveryStatus.setId(deliveryStarted.getId());
        deliveryStatus.setOrderId(deliveryStarted.getOrderId());
        deliveryStatus.setCustomerId(deliveryStarted.getCustomerId());
        deliveryStatus.setAddress(deliveryStarted.getAddress());
        // view 레파지 토리에 save
        deliveryStatusRepository.save(deliveryStatus);
    }
}
