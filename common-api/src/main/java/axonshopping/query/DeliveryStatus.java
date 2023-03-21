package axonshopping.query;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Table(name = "DeliveryStatus_table")
@Data
@Relation(collectionRelation = "deliveryStatuses")
public class DeliveryStatus {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private String id;

    private String orderId;
    private String customerId;
    private String address;
}
