package cn.gov.eximbank.customerforecast;

import cn.gov.eximbank.customerforecast.model.GroupSnapshotInBranch;
import cn.gov.eximbank.customerforecast.model.GroupSnapshot;
import cn.gov.eximbank.customerforecast.model.Business;
import cn.gov.eximbank.customerforecast.model.EBranch;
import cn.gov.eximbank.customerforecast.repository.BusinessRepository;
import cn.gov.eximbank.customerforecast.repository.CustomerRepository;
import cn.gov.eximbank.customerforecast.repository.GroupSnapshotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApplicationRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private GroupSnapshotRepository groupSnapshotRepository;

    @Override
    public void run(String... args) throws Exception {
        Business business = new Business("123");
        business.addBusinessDetail("201908", "123", EBranch.AH.name(), 100000000);
        business.addBusinessDetail("201909", "123", EBranch.AH.name(), 100000000);
        //businessRepository.save(business);
        Business business1 = businessRepository.findById("123").get();
        System.out.println(business1.getCustomerId("201908"));
        analyzeGroup();
    }

    private GroupSnapshot analyzeGroup() throws IOException {
        String groupName = "测试集团";
        String branch1 = new String(EBranch.GS.getBranchName());
        String branch2 = new String("北京分行");
        GroupSnapshotInBranch gongsibu = new GroupSnapshotInBranch("123", branch1, 1000000000,
                200000000, 3E10);
        GroupSnapshotInBranch beijing = new GroupSnapshotInBranch("234", branch2, 1100000000,
                220000000, 3.5E10);
        GroupSnapshot groupSnapshot = new GroupSnapshot("123", groupName, 5,
                2.22E10, 3.33E10, 4.44E10);
        groupSnapshot.addGroupSnapshotInBranch(gongsibu);
        groupSnapshot.addGroupSnapshotInBranch(beijing);
        groupSnapshotRepository.deleteAll();
        groupSnapshotRepository.save(groupSnapshot);
        return groupSnapshot;
    }


}
