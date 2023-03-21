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
public class DeliveryAggregate {

    @AggregateIdentifier
    private String id;

    private String orderId;
    private String customerId;
    private String address;

    public DeliveryAggregate() {}

    @CommandHandler
    public DeliveryAggregate(StartDeliveryCommand command) {}

    @CommandHandler
    public DeliveryAggregate(CancelDeliveryCommand command) {}

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    @EventSourcingHandler
    public void on(DeliveryStartedEvent event) {
        //TODO: business logic here

    }

    @EventSourcingHandler
    public void on(DeliveryCanceledEvent event) {
        //TODO: business logic here

    }
}
