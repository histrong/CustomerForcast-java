package cn.gov.eximbank.customerforcast.repository;

import cn.gov.eximbank.customerforcast.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {

}
