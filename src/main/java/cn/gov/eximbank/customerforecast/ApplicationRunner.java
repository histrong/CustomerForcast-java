package cn.gov.eximbank.customerforecast;

import cn.gov.eximbank.customerforecast.cmd.GenerateGroupSnapshotCommand;
import cn.gov.eximbank.customerforecast.cmd.ImportCustomerCommand;
import cn.gov.eximbank.customerforecast.cmd.ImportInSheetBusinessCommand;
import cn.gov.eximbank.customerforecast.cmd.ImportOutSheetBusinessCommand;
import cn.gov.eximbank.customerforecast.model.*;
import cn.gov.eximbank.customerforecast.report.WordReporter;
import cn.gov.eximbank.customerforecast.repository.BusinessRepository;
import cn.gov.eximbank.customerforecast.repository.CustomerRepository;
import cn.gov.eximbank.customerforecast.repository.GroupRepository;
import cn.gov.eximbank.customerforecast.repository.GroupSnapshotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class ApplicationRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private GroupSnapshotRepository groupSnapshotRepository;

    @Override
    public void run(String... args) throws Exception {
//        Business business = new Business("123");
//        business.addBusinessDetail("201908", "123", EBranch.AH.name(), 100000000);
//        business.addBusinessDetail("201909", "123", EBranch.AH.name(), 100000000);
//        //businessRepository.save(business);
//        Business business1 = businessRepository.findById("123").get();
//        System.out.println(business1.getCustomerId("201908"));
//        analyzeGroup();
//        testGenerateCustomers();
//        testGenerateBusiness();
//        GenerateGroupSnapshotCommand cmd = new GenerateGroupSnapshotCommand("201909", groupRepository,
//                customerRepository, businessRepository, groupSnapshotRepository);
//        cmd.execute();
//        analyzeGroup();
        ImportCustomerCommand importCustomerCommand = new ImportCustomerCommand(groupRepository, customerRepository);
//        customerImporter.importCustomers();

        ImportInSheetBusinessCommand importInSheetBusinessCommand = new ImportInSheetBusinessCommand("201909", businessRepository);
//        importInSheetBusinessCommand.execute();
        GenerateGroupSnapshotCommand generateGroupSnapshotCommand = new GenerateGroupSnapshotCommand("201909",
                groupRepository, customerRepository, businessRepository, groupSnapshotRepository);
        generateGroupSnapshotCommand.execute();

        ImportOutSheetBusinessCommand importOutSheetBusinessCommand = new ImportOutSheetBusinessCommand("201909", businessRepository);
//        importOutSheetBusinessCommand.execute();
        analyzeGroup();
    }

    private void analyzeGroup() throws IOException {
//        String groupName = "测试集团";
//        String branch1 = new String(EBranch.GS.getBranchName());
//        String branch2 = new String("北京分行");
//        GroupSnapshotInBranch gongsibu = new GroupSnapshotInBranch("123", branch1, 1000000000,
//                200000000, 3E10);
//        GroupSnapshotInBranch beijing = new GroupSnapshotInBranch("234", branch2, 1100000000,
//                220000000, 3.5E10);
//        GroupSnapshot groupSnapshot = new GroupSnapshot("123", groupName, 5);
////                2.22E10, 3.33E10, 4.44E10);
//        groupSnapshot.addGroupSnapshotInBranch(gongsibu);
//        groupSnapshot.addGroupSnapshotInBranch(beijing);
//        groupSnapshotRepository.deleteAll();
//        groupSnapshotRepository.save(groupSnapshot);
//        return groupSnapshot;
        Group group = groupRepository.findByName("中国电力建设集团有限公司");
        GroupSnapshot groupSnapshot = groupSnapshotRepository.findByGroupName(group.getName());
        WordReporter reporter = new WordReporter();
        reporter.loadTemplateFile();
        reporter.generateReportDocument(groupSnapshot);
    }

    public void testGenerateCustomers() {
        groupRepository.deleteAll();;
        customerRepository.deleteAll();

        Group group1 = new Group("JTKH1", "测试集团1", EBranch.BJ.name());
        Group group2 = new Group("JTKH2", "测试集团2", EBranch.GN.name());
        groupRepository.save(group1);
        groupRepository.save(group2);

        Customer customer1 = new Customer("1360901", "测试客户1", "JTKH1", EBranch.BJ.name());
        Customer customer2 = new Customer("1360902", "测试客户2", "JTKH1", EBranch.BJ.name());
        Customer customer3 = new Customer("1360903", "测试客户3", "JTKH1", EBranch.SH.name());
        Customer customer4 = new Customer("1360904", "测试客户4", "JTKH1", EBranch.GD.name());
        Customer customer5 = new Customer("1360905", "测试客户5", "JTKH2", EBranch.GN.name());
        Customer customer6 = new Customer("1360906", "测试客户6", "JTKH2", EBranch.GN.name());
        Customer customer7 = new Customer("1360907", "测试客户7", "JTKH2", EBranch.TJ.name());
        Customer customer8 = new Customer("1360908", "测试客户8", "JTKH2", EBranch.HB.name());
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);
        customerRepository.save(customer5);
        customerRepository.save(customer6);
        customerRepository.save(customer7);
        customerRepository.save(customer8);
    }

    public void testGenerateBusiness() {
        businessRepository.deleteAll();

        Business business1 = new Business("2201", EBusinessType.LN);
        business1.addBusinessDetail("201908", "1360901", EBranch.BJ.name(), 100000000);
        business1.addBusinessDetail("201909", "1360901", EBranch.BJ.name(), 80000000);

        Business business2 = new Business("BH01", EBusinessType.TI);
        business2.addBusinessDetail("201908", "1360901", EBranch.BJ.name(), 90000000);
        business2.addBusinessDetail("201909", "1360901", EBranch.BJ.name(), 70000000);

        Business business3 = new Business("BH02", EBusinessType.TI);
        business3.addBusinessDetail("201908", "1360902", EBranch.BJ.name(), 150000000);
        business3.addBusinessDetail("201909", "1360902", EBranch.BJ.name(), 180000000);

        Business business4 = new Business("XY01", EBusinessType.TO);
        business4.addBusinessDetail("201908", "1360902", EBranch.HE.name(), 50000000);
        business4.addBusinessDetail("201909", "1360902", EBranch.HE.name(), 60000000);

        Business business5 = new Business("2202", EBusinessType.LN);
        business5.addBusinessDetail("201908", "1360902", EBranch.BJ.name(), 200000000);
        business5.addBusinessDetail("201909", "1360902", EBranch.BJ.name(), 190000000);

        Business business6 = new Business("2203", EBusinessType.LN);
        business6.addBusinessDetail("201908", "1360903", EBranch.SH.name(), 80000000);
        business6.addBusinessDetail("201909", "1360903", EBranch.SH.name(), 75000000);

        Business business7 = new Business("2204", EBusinessType.LN);
        business7.addBusinessDetail("201908", "1360903", EBranch.SH.name(), 20000000);
        business7.addBusinessDetail("201909", "1360903", EBranch.SH.name(), 20000000);

        Business business8 = new Business("FF01", EBusinessType.TO);
        business8.addBusinessDetail("201908", "1360903", EBranch.JS.name(), 50000000);
        business8.addBusinessDetail("201909", "1360903", EBranch.JS.name(), 50000000);

        businessRepository.save(business1);
        businessRepository.save(business2);
        businessRepository.save(business3);
        businessRepository.save(business4);
        businessRepository.save(business5);
        businessRepository.save(business6);
        businessRepository.save(business7);
        businessRepository.save(business8);
    }

}
