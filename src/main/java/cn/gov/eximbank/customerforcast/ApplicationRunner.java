package cn.gov.eximbank.customerforcast;

import cn.gov.eximbank.customerforcast.analyzer.BranchAnalyzeResult;
import cn.gov.eximbank.customerforcast.analyzer.GroupAnalyzeResult;
import cn.gov.eximbank.customerforcast.report.WordReporter;
import cn.gov.eximbank.customerforcast.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

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
    }

    private GroupAnalyzeResult analyzeGroup() throws IOException {
        String charSet = Charset.defaultCharset().name();
        String groupName = new String("测试集团".getBytes("gbk"), "utf8");
        String branch1 = new String("公司部".getBytes("gbk"), "utf8");
        String branch2 = new String("北京分行".getBytes("gbk"), "utf8");
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
