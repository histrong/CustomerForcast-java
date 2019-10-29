package cn.gov.eximbank.customerforecast.cmd;

import cn.gov.eximbank.customerforecast.model.Business;
import cn.gov.eximbank.customerforecast.model.EBranch;
import cn.gov.eximbank.customerforecast.model.EBusinessType;
import cn.gov.eximbank.customerforecast.repository.BusinessRepository;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ImportInSheetBusinessCommand implements ICommand {

    private static Logger logger = LoggerFactory.getLogger(ImportInSheetBusinessCommand.class);

    private static String importPath = "import";

    private static String inSheetFileName = "InSheetBusiness.xlsx";

    private String period;

    private BusinessRepository businessRepository;

    public ImportInSheetBusinessCommand(String period, BusinessRepository businessRepository) {
        this.period = period;
        this.businessRepository = businessRepository;
    }

    @Override
    public String getCommandName() {
        return "ImportInSheetBusinessCommand";
    }

    @Override
    public void execute() {
        File inSheetBusinessFile = new File(importPath + File.separator + inSheetFileName);
        if (inSheetBusinessFile.exists()) {
            readInSheetBusinessFile(inSheetBusinessFile);
        }
        else {
            logger.error(inSheetBusinessFile.getName() + " not exists");
        }
    }

    private void readInSheetBusinessFile(File inSheetBusinessFile) {
        try {
            Workbook wb = WorkbookFactory.create(inSheetBusinessFile);
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 6; i != sheet.getLastRowNum() + 1; ++i) {
                Row row = sheet.getRow(i);
                readInSheetBusinessRow(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readInSheetBusinessRow(Row row) {
        String businessId = CellContentUtil.getStringContent(row.getCell(2));
        String branchText = CellContentUtil.getStringContent(row.getCell(8));
        String branchId = EBranch.getBranchId(branchText).name();
        if (branchId.equals(EBranch.NONE.name())) {
            logger.info(businessId + " : " + branchText + " cannot read");
        }
        double balance = CellContentUtil.getNumericContent(row.getCell(10));
        String customerId = CellContentUtil.getStringContent(row.getCell(49));
        Optional<Business> businessOptional = businessRepository.findById(businessId);
        if (businessOptional.isPresent()) {
            Business business = businessOptional.get();
            business.addBusinessDetail(period, customerId, branchId, balance);
            businessRepository.save(business);
        }
        else {
            String businessTypeText = CellContentUtil.getStringContent(row.getCell(21));
            EBusinessType businessType = getBusinessType(businessTypeText);
            if (businessType.equals(EBusinessType.NONE)) {
                logger.info(businessId + " : " + businessTypeText + " cannot read");
            }
            Business business = new Business(businessId, businessType);
            business.addBusinessDetail(period, customerId, branchId, balance);
            businessRepository.save(business);
        }
    }

    private EBusinessType getBusinessType(String businessTypeText) {
        if (businessTypeText.equals("垫款") || businessTypeText.equals("贸易融资")) {
            return EBusinessType.TI;
        }
        else if (businessTypeText.contains("贷")) {
            return EBusinessType.LN;
        }
        else {
            return EBusinessType.NONE;
        }
    }
}
