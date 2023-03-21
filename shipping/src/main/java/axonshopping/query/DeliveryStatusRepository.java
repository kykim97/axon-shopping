package axonshopping.query;

// import org.springframework.data.rest.core.annotation.RestResource;
// import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

//@RepositoryRestResource(path = "deliveryStatuses", collectionResourceRel = "deliveryStatuses")
public interface DeliveryStatusRepository
    extends JpaRepository<DeliveryStatus, String> {
    /*
    @Override
    @RestResource(exported = false)
    void delete(OrderStatus entity);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
     <S extends OrderStatus> S save(S entity);
*/

}
