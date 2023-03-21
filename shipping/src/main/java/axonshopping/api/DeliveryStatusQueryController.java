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
public class DeliveryStatusQueryController {

    private final QueryGateway queryGateway;

    private final ReactorQueryGateway reactorQueryGateway;

    public DeliveryStatusQueryController(
        QueryGateway queryGateway,
        ReactorQueryGateway reactorQueryGateway
    ) {
        this.queryGateway = queryGateway;
        this.reactorQueryGateway = reactorQueryGateway;
    }

    @GetMapping("/deliveryStatuses")
    public CompletableFuture findAll(DeliveryStatusQuery query) {
        return queryGateway
            .query(
                query,
                ResponseTypes.multipleInstancesOf(DeliveryStatus.class)
            )
            .thenApply(resources -> {
                List modelList = new ArrayList<EntityModel<DeliveryStatus>>();

                resources
                    .stream()
                    .forEach(resource -> {
                        modelList.add(hateoas(resource));
                    });

                CollectionModel<DeliveryStatus> model = CollectionModel.of(
                    modelList
                );

                return new ResponseEntity<>(model, HttpStatus.OK);
            });
    }

    @GetMapping("/deliveryStatuses/{id}")
    public CompletableFuture findById(@PathVariable("id") String id) {
        DeliveryStatusSingleQuery query = new DeliveryStatusSingleQuery();
        query.setId(id);

        return queryGateway
            .query(
                query,
                ResponseTypes.optionalInstanceOf(DeliveryStatus.class)
            )
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

    EntityModel<DeliveryStatus> hateoas(DeliveryStatus resource) {
        EntityModel<DeliveryStatus> model = EntityModel.of(resource);

        model.add(
            Link.of("/deliveryStatuses/" + resource.getId()).withSelfRel()
        );

        return model;
    }

    @MessageMapping("deliveryStatuses.all")
    public Flux<DeliveryStatus> subscribeAll() {
        return reactorQueryGateway.subscriptionQueryMany(
            new DeliveryStatusQuery(),
            DeliveryStatus.class
        );
    }

    @MessageMapping("deliveryStatuses.{id}.get")
    public Flux<DeliveryStatus> subscribeSingle(
        @DestinationVariable String id
    ) {
        DeliveryStatusSingleQuery query = new DeliveryStatusSingleQuery();
        query.setId(id);

        return reactorQueryGateway.subscriptionQuery(
            query,
            DeliveryStatus.class
        );
    }
}
