package axonshopping.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

import axonshopping.command.*;
import axonshopping.event.*;
import axonshopping.query.*;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Data
@ToString
public class OrderAggregate {

    @AggregateIdentifier
    private String id;

    private String productId;
    private Integer qty;
    private String customerId;
    private String status;

    public OrderAggregate() {}

    @CommandHandler
    public OrderAggregate(OrderCommand command) {}

    @CommandHandler
    public OrderAggregate(CancelCommand command) {}

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    @EventSourcingHandler
    public void on(OrderPlacedEvent event) {
        //TODO: business logic here

    }

    @EventSourcingHandler
    public void on(OrderCanceledEvent event) {
        //TODO: business logic here

    }
}
