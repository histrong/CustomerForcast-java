package cn.gov.eximbank.customerforecast.repository;

import cn.gov.eximbank.customerforecast.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
}
