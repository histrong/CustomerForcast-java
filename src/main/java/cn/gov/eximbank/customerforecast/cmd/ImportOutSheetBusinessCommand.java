package cn.gov.eximbank.customerforecast.cmd;

import cn.gov.eximbank.customerforecast.model.Business;
import cn.gov.eximbank.customerforecast.model.EBranch;
import cn.gov.eximbank.customerforecast.model.EBusinessType;
import cn.gov.eximbank.customerforecast.repository.BusinessRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ImportOutSheetBusinessCommand implements ICommand {

    private static Logger logger = LoggerFactory.getLogger(ImportOutSheetBusinessCommand.class);

    private static String importPath = "import";

    private static String outSheetFileName = "OutSheetBusiness.xlsx";

    private String period;

    private BusinessRepository businessRepository;

    public ImportOutSheetBusinessCommand(String period, BusinessRepository businessRepository) {
        this.period = period;
        this.businessRepository = businessRepository;
    }

    @Override
    public String getCommandName() {
        return "ImportOutSheetBusinessCommand";
    }

    @Override
    public void execute() {
        File outSheetBusinessFile = new File(importPath + File.separator + outSheetFileName);
        if (outSheetBusinessFile.exists()) {
            readOutSheetBusinessFile(outSheetBusinessFile);
        }
        else {
            logger.error(outSheetBusinessFile.getName() + " not exists");
        }
    }

    private void readOutSheetBusinessFile(File outSheetBusinessFile) {
        try {
            Workbook wb = WorkbookFactory.create(outSheetBusinessFile);
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 4; i != sheet.getLastRowNum(); ++i) {
                Row row = sheet.getRow(i);
                readOutInSheetBusinessRow(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readOutInSheetBusinessRow(Row row) {
        String businessId = CellContentUtil.getStringContent(row.getCell(1));
        String branchText = CellContentUtil.getStringContent(row.getCell(0));
        String branchId = EBranch.getBranchId(branchText).name();
        if (branchId.equals(EBranch.NONE.name())) {
            logger.info(businessId + " : " + branchText + " cannot read");
        }
        double balance = CellContentUtil.getNumericContent(row.getCell(4));
        String customerId = CellContentUtil.getStringContent(row.getCell(5));
        Optional<Business> businessOptional = businessRepository.findById(businessId);
        if (businessOptional.isPresent()) {
            Business business = businessOptional.get();
            business.addBusinessDetail(period, customerId, branchId, balance);
//            businessRepository.save(business);
        }
        else {
            Business business = new Business(businessId, EBusinessType.TO);
            business.addBusinessDetail(period, customerId, branchId, balance);
//            businessRepository.save(business);
        }
        logger.info(businessId + " : " + branchId + " : " + balance);
    }
}
