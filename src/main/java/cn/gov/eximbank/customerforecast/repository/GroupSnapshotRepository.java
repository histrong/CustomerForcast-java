package cn.gov.eximbank.customerforecast.repository;

import cn.gov.eximbank.customerforecast.model.GroupSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupSnapshotRepository extends MongoRepository<GroupSnapshot, String> {

    GroupSnapshot findByGroupName(String name);

}
