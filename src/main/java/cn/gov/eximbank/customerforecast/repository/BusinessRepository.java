package cn.gov.eximbank.customerforecast.repository;

import cn.gov.eximbank.customerforecast.model.Business;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusinessRepository extends MongoRepository<Business, String> {
}
