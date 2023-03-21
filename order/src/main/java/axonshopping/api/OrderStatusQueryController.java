package axonshopping.api;

import axonshopping.query.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class OrderStatusQueryController {

    private final QueryGateway queryGateway;

    private final ReactorQueryGateway reactorQueryGateway;

    public OrderStatusQueryController(
        QueryGateway queryGateway,
        ReactorQueryGateway reactorQueryGateway
    ) {
        this.queryGateway = queryGateway;
        this.reactorQueryGateway = reactorQueryGateway;
    }

    @GetMapping("/orderStatuses")
    public CompletableFuture findAll(OrderStatusQuery query) {
        return queryGateway
            .query(query, ResponseTypes.multipleInstancesOf(OrderStatus.class))
            .thenApply(resources -> {
                List modelList = new ArrayList<EntityModel<OrderStatus>>();

                resources
                    .stream()
                    .forEach(resource -> {
                        modelList.add(hateoas(resource));
                    });

                CollectionModel<OrderStatus> model = CollectionModel.of(
                    modelList
                );

                return new ResponseEntity<>(model, HttpStatus.OK);
            });
    }

    @GetMapping("/orderStatuses/{id}")
    public CompletableFuture findById(@PathVariable("id") String id) {
        OrderStatusSingleQuery query = new OrderStatusSingleQuery();
        query.setId(id);

        return queryGateway
            .query(query, ResponseTypes.optionalInstanceOf(OrderStatus.class))
            .thenApply(resource -> {
                if (!resource.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(
                    hateoas(resource.get()),
                    HttpStatus.OK
                );
            })
            .exceptionally(ex -> {
                throw new RuntimeException(ex);
            });
    }

    EntityModel<OrderStatus> hateoas(OrderStatus resource) {
        EntityModel<OrderStatus> model = EntityModel.of(resource);

        model.add(Link.of("/orderStatuses/" + resource.getId()).withSelfRel());

        return model;
    }

    @MessageMapping("orderStatuses.all")
    public Flux<OrderStatus> subscribeAll() {
        return reactorQueryGateway.subscriptionQueryMany(
            new OrderStatusQuery(),
            OrderStatus.class
        );
    }

    @MessageMapping("orderStatuses.{id}.get")
    public Flux<OrderStatus> subscribeSingle(@DestinationVariable String id) {
        OrderStatusSingleQuery query = new OrderStatusSingleQuery();
        query.setId(id);

        return reactorQueryGateway.subscriptionQuery(query, OrderStatus.class);
    }
}
