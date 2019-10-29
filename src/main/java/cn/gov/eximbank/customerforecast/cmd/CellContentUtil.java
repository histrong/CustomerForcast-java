package cn.gov.eximbank.customerforecast.cmd;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CellContentUtil {

    private static Logger logger = LoggerFactory.getLogger(CellContentUtil.class);

    public static String getStringContent(Cell cell) {
        if (cell.getCellType().equals(CellType.STRING)) {
            return cell.getStringCellValue();
        }
        else if (cell.getCellType().equals(CellType.NUMERIC)) {
            return Double.toString(cell.getNumericCellValue());
        }
        else if (cell.getCellType().equals(CellType.BLANK)) {
            return "";
        }
        else if (cell.getCellType().equals(CellType._NONE)) {
            return "";
        }
        else if (cell.getCellType().equals(CellType.FORMULA)) {
            FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
            evaluator.evaluate(cell);
            CellValue cellValue = evaluator.evaluate(cell);
            String value = cellValue.getStringValue();
            return value;
        }
        else {
            logger.error("Read String cell error, row : " + cell.getRowIndex()
                    + ", column : " + cell.getColumnIndex());
            return "";
        }
    }

    public static double getNumericContent(Cell cell) {
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            return cell.getNumericCellValue();
        }
        else if (cell.getCellType().equals(CellType.STRING)) {
            return Double.valueOf(cell.getStringCellValue());
        }
        else {
            logger.error("Read Double cell error, row : " + cell.getRowIndex()
                    + ", column : " + cell.getColumnIndex());
            return 0;
        }
    }
}
