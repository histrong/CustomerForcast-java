package cn.gov.eximbank.customerforecast.cmd;

import cn.gov.eximbank.customerforecast.model.Customer;
import cn.gov.eximbank.customerforecast.model.EBranch;
import cn.gov.eximbank.customerforecast.model.Group;
import cn.gov.eximbank.customerforecast.repository.CustomerRepository;
import cn.gov.eximbank.customerforecast.repository.GroupRepository;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ImportCustomerCommand implements ICommand {

    private static Logger logger = LoggerFactory.getLogger(ImportCustomerCommand.class);

    private static String importPath = "import";

    private static String customerFileName = "customer.xlsx";

    private GroupRepository groupRepository;

    private CustomerRepository customerRepository;

    private Set<String> groupIds;

    private Set<String> customerIds;

    public ImportCustomerCommand(GroupRepository groupRepository, CustomerRepository customerRepository) {
        this.groupRepository = groupRepository;
        this.customerRepository = customerRepository;
        this.groupIds = new HashSet<>();
        this.customerIds = new HashSet<>();
    }

    @Override
    public String getCommandName() {
        return "ImportCustomerCommand";
    }

    @Override
    public void execute() {
        groupRepository.deleteAll();
        customerRepository.deleteAll();
        File customerFile = new File(importPath + File.separator + customerFileName);
        if (customerFile.exists()) {
            readCustomerFile(customerFile);
        }
        else {
            logger.error(customerFile.getName() + " not exists");
        }
    }

    private void readCustomerFile(File customerFile) {
        try {
            Workbook wb = WorkbookFactory.create(customerFile);
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i != sheet.getLastRowNum() + 1; ++i) {
                Row row = sheet.getRow(i);
                readCustomerFileRow(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readCustomerFileRow(Row row) {
        String groupId = CellContentUtil.getStringContent(row.getCell(1));
        String groupName = CellContentUtil.getStringContent(row.getCell(2));
        String customerId = CellContentUtil.getStringContent(row.getCell(3));
        String customerName = CellContentUtil.getStringContent(row.getCell(4));
        if (!groupIds.contains(groupId)) {
            Group group = new Group(groupId, groupName, EBranch.NONE.name());
            groupRepository.save(group);
            groupIds.add(groupId);
        }
        if (!customerIds.contains(customerId)) {
            Customer customer = new Customer(customerId, customerName, groupId, EBranch.NONE.name());
            customerRepository.save(customer);
            customerIds.add(customerId);
        }
    }
}
