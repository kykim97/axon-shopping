package axonshopping.query;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Table(name = "OrderStatus_table")
@Data
@Relation(collectionRelation = "orderStatuses")
public class OrderStatus {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private String id;

    private String productId;
    private Integer qty;
    private String customerId;
    private String status;
}
