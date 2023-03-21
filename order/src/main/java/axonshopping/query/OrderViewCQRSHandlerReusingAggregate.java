package axonshopping.query;

import axonshopping.aggregate.*;
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
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("orderView")
public class OrderViewCQRSHandlerReusingAggregate {

    @Autowired
    private OrderReadModelRepository repository;

    @Autowired
    private QueryUpdateEmitter queryUpdateEmitter;

    @QueryHandler
    public List<OrderReadModel> handle(OrderViewQuery query) {
        return repository.findAll();
    }

    @QueryHandler
    public Optional<OrderReadModel> handle(OrderViewSingleQuery query) {
        return repository.findById(query.getId());
    }

    @EventHandler
    public void whenOrderPlaced_then_CREATE(OrderPlacedEvent event)
        throws Exception {
        OrderReadModel entity = new OrderReadModel();
        OrderAggregate aggregate = new OrderAggregate();
        aggregate.on(event);

        BeanUtils.copyProperties(aggregate, entity);

        repository.save(entity);

        queryUpdateEmitter.emit(OrderViewQuery.class, query -> true, entity);
    }

    @EventHandler
    public void whenOrderCanceled_then_CREATE(OrderCanceledEvent event)
        throws Exception {
        OrderReadModel entity = new OrderReadModel();
        OrderAggregate aggregate = new OrderAggregate();
        aggregate.on(event);

        BeanUtils.copyProperties(aggregate, entity);

        repository.save(entity);

        queryUpdateEmitter.emit(OrderViewQuery.class, query -> true, entity);
    }
}
