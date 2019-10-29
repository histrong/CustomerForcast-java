package cn.gov.eximbank.customerforecast.repository;

import cn.gov.eximbank.customerforecast.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    List<Customer> findAllByGroupId(String groupId);

}
