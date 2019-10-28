package cn.gov.eximbank.customerforecast.repository;

import cn.gov.eximbank.customerforecast.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {

}
