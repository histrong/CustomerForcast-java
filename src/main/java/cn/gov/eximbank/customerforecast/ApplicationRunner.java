package cn.gov.eximbank.customerforecast;

import cn.gov.eximbank.customerforecast.analyzer.BranchAnalyzeResult;
import cn.gov.eximbank.customerforecast.analyzer.GroupAnalyzeResult;
import cn.gov.eximbank.customerforecast.model.EBranch;
import cn.gov.eximbank.customerforecast.report.WordReporter;
import cn.gov.eximbank.customerforecast.repository.CustomerRepository;
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

    @Override
    public void run(String... args) throws Exception {
        WordReporter wordReporter = new WordReporter();
        wordReporter.loadTemplateFile();
        GroupAnalyzeResult groupAnalyzeResult = analyzeGroup();
        wordReporter.generateReportDocument(groupAnalyzeResult);
        String str = "公司业务部公司一处";
        String str2 = "喀什分行公司处";
        System.out.println(EBranch.getBranchId(str2).name());
    }

    private GroupAnalyzeResult analyzeGroup() throws IOException {
        String groupName = "测试集团";
        String branch1 = new String(EBranch.GS.getBranchName());
        String branch2 = new String("北京分行");
        BranchAnalyzeResult gongsibu = new BranchAnalyzeResult(branch1, 1000000000,
                200000000, 3E10);
        BranchAnalyzeResult beijing = new BranchAnalyzeResult(branch2, 1100000000,
                220000000, 3.5E10);
        GroupAnalyzeResult groupAnalyzeResult = new GroupAnalyzeResult(groupName, 5,
                2.22E10, 3.33E10, 4.44E10);
        groupAnalyzeResult.addBranchAnalyzeResult(gongsibu);
        groupAnalyzeResult.addBranchAnalyzeResult(beijing);
        return groupAnalyzeResult;
    }


}
