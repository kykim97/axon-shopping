package axonshopping.policy;

import axonshopping.aggregate.*;
import axonshopping.command.*;
import axonshopping.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.DisallowReplay;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("shipping")
public class PolicyHandler {

    @Autowired
    CommandGateway commandGateway;

    @EventHandler
    //@DisallowReplay
    public void wheneverOrderPlaced_StartDelivery(
        OrderPlacedEvent orderPlaced
    ) {
        System.out.println(orderPlaced.toString());

        StartDeliveryCommand command = new StartDeliveryCommand();
        //TODO: mapping attributes (anti-corruption)
        commandGateway.send(command);
    }

    @EventHandler
    //@DisallowReplay
    public void wheneverOrderCanceled_CancelDelivery(
        OrderCanceledEvent orderCanceled
    ) {
        System.out.println(orderCanceled.toString());

        CancelDeliveryCommand command = new CancelDeliveryCommand();
        //TODO: mapping attributes (anti-corruption)
        commandGateway.send(command);
    }
}
