package cn.gov.eximbank.customerforecast.report;

import cn.gov.eximbank.customerforecast.Application;
import cn.gov.eximbank.customerforecast.model.GroupSnapshotInBranch;
import cn.gov.eximbank.customerforecast.model.GroupSnapshot;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordReporter {

    private XWPFDocument templateDoc;

    public boolean loadTemplateFile() {
        URL templateResource = Application.class.getResource("/ReportTemplate.docx");
        try {
            templateDoc = new XWPFDocument(POIXMLDocument.openPackage(templateResource.getFile()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean generateReportDocument(GroupSnapshot groupSnapshot)
            throws IOException {
        XWPFDocument destDoc = new XWPFDocument();
        for (IBodyElement bodyElement : templateDoc.getBodyElements()) {
            if (bodyElement.getElementType().equals(BodyElementType.PARAGRAPH)) {
                XWPFParagraph paragraph = (XWPFParagraph) bodyElement;
                generateParagraph(destDoc, paragraph, groupSnapshot.toVariableMap());
            }
            else if (bodyElement.getElementType().equals(BodyElementType.TABLE)) {
                XWPFTable table = (XWPFTable) bodyElement;
                generateTable(destDoc, table, groupSnapshot);
            }
        }

        FileOutputStream out = new FileOutputStream(getOutFileFullName(groupSnapshot.getGroupName(), "report/"));
        destDoc.write(out);
        return true;
    }

    private void generateTable(XWPFDocument destDoc, XWPFTable table, GroupSnapshot groupSnapshot) {
        CTTbl ctTbl = CTTbl.Factory.newInstance();
        ctTbl.set(table.getCTTbl());
        XWPFTable replacedTable = new XWPFTable(ctTbl, destDoc);

        XWPFTableRow templateRow = table.getRow(1);
        for (int i = 0; i != groupSnapshot.getGroupBranchCount(); ++i) {
            GroupSnapshotInBranch groupSnapshotInBranch = groupSnapshot.getGroupSnapshotInBranches().get(i);
            if (i == 0) {
                replaceSingleRow(replacedTable.getRow(1), groupSnapshotInBranch.toVariableMap());
            }
            else {
                CTRow ctRow = CTRow.Factory.newInstance();
                ctRow.set(templateRow.getCtRow());
                XWPFTableRow row = new XWPFTableRow(ctRow, replacedTable);
                replaceSingleRow(row, groupSnapshotInBranch.toVariableMap());
                replacedTable.addRow(row);
            }
        }

        setTableStyle(replacedTable);

        destDoc.createTable();
        int position = destDoc.getTables().size() - 1;
        destDoc.setTable(position, replacedTable);
    }

    private void setTableStyle(XWPFTable table) {
        String borderColor = "000000";
        int borderSize = 2;
        int borderSpace = 0;
        table.setBottomBorder(XWPFTable.XWPFBorderType.SINGLE, borderSize, borderSpace, borderColor);
        table.setTopBorder(XWPFTable.XWPFBorderType.SINGLE, borderSize, borderSpace, borderColor);
        table.setLeftBorder(XWPFTable.XWPFBorderType.SINGLE, borderSize, borderSpace, borderColor);
        table.setRightBorder(XWPFTable.XWPFBorderType.SINGLE, borderSize, borderSpace, borderColor);
        table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, borderSize, borderSpace, borderColor);
        table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, borderSize, borderSpace, borderColor);
        table.setCellMargins(0, 110, 0, 110);
    }

    private void replaceTableContent(XWPFTable table, GroupSnapshot groupSnapshot) {
        for (int i = 0; i != groupSnapshot.getGroupBranchCount(); ++i) {
            GroupSnapshotInBranch groupSnapshotInBranch = groupSnapshot.getGroupSnapshotInBranches().get(i);
            replaceSingleRow(table.getRows().get(i + 1), groupSnapshotInBranch.toVariableMap());
        }
    }

    private void replaceSingleRow(XWPFTableRow row, Map<String, String> variableMap) {
        for (int i = 0; i != row.getTableCells().size(); ++i) {
            XWPFTableCell cell = row.getTableCells().get(i);
            replaceSingleCell(cell, variableMap);
        }
    }

    private void replaceSingleCell(XWPFTableCell cell, Map<String, String> variableMap) {
        for (int i = 0; i != cell.getParagraphs().size(); ++i) {
            XWPFParagraph paragraph = cell.getParagraphs().get(i);
            try {
                replaceParagraph(paragraph, variableMap);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    private void generateParagraph(XWPFDocument destDoc, XWPFParagraph paragraph, Map<String, String> variableMap) {
        CTP ctParagraph = CTP.Factory.newInstance();
        ctParagraph.set(paragraph.getCTP());
        XWPFParagraph replacedParagraph = new XWPFParagraph(ctParagraph, destDoc);
        replaceParagraph(replacedParagraph, variableMap);
        destDoc.createParagraph();
        int position = destDoc.getParagraphs().size() - 1;
        destDoc.setParagraph(replacedParagraph, position);
    }

    private void replaceParagraph(XWPFParagraph paragraph, Map<String, String> variableMap) {
        Pattern variablePattern = Pattern.compile("\\{[a-zA-Z]+\\}");

        while (true) {
            String content = paragraph.getText();
            Matcher matcher = variablePattern.matcher(content);
            // 如果找不到匹配的变量，则退出循环
            if (!matcher.find()) {
                break;
            }
            // 如果找到了匹配的变量，开始进行替换
            else {
                int startRunIndex = paragraph.searchText(ETemplateVariable.VARIABLE_PREFIX, new PositionInParagraph()).getBeginRun();
                int endRunIndex = paragraph.searchText(ETemplateVariable.VARIABLE_SUFFIX, new PositionInParagraph()).getEndRun();
                replaceSingleVariable(paragraph, startRunIndex, endRunIndex, variableMap);
            }
        }
    }

    private void replaceSingleVariable(XWPFParagraph paragraph, int startRunIndex, int endRunIndex, Map<String, String> variableMap) {
        String variableSign = getVariableSign(paragraph, startRunIndex, endRunIndex);
        String variableValue = getVariableValue(variableMap, variableSign);
        CTRPr variableCTR = getVariableCTR(paragraph, startRunIndex, endRunIndex);
        if (startRunIndex == endRunIndex) {
            replaceSingleVariableInSingleRun(paragraph, startRunIndex, variableValue, variableCTR);
        }
        else {
            replaceSingleVariableInMultiRuns(paragraph, startRunIndex, endRunIndex, variableValue, variableCTR);
        }
    }

    private String getVariableSign(XWPFParagraph paragraph, int startRunIndex, int endRunIndex) {
        StringBuilder variableSign = new StringBuilder();
        // 如果在同一个run内获得variable
        if (startRunIndex == endRunIndex) {
            String runContent = paragraph.getRuns().get(startRunIndex).text();
            int variableStartIndex = runContent.indexOf(ETemplateVariable.VARIABLE_PREFIX);
            int variableEndIndex = runContent.indexOf(ETemplateVariable.VARIABLE_SUFFIX);
            variableSign = new StringBuilder(runContent.substring(variableStartIndex + 1, variableEndIndex));
        }
        // 如果在多个run内
        else {
            //1. 获得startRun的{后的内容
            String startRunContent = paragraph.getRuns().get(startRunIndex).text();
            int variableStartIndex = startRunContent.indexOf("{");
            variableSign.append(startRunContent.substring(variableStartIndex + 1));
            //2. 添加startRun和endRun之间的内容
            for (int i = startRunIndex + 1; i != endRunIndex; ++i) {
                String currentRunContent = paragraph.getRuns().get(i).text();
                variableSign.append(currentRunContent);
            }
            //3. 添加endRun的}前的内容
            String endRunContent = paragraph.getRuns().get(endRunIndex).text();
            int variableEndIndex = endRunContent.indexOf("}");
            variableSign.append(endRunContent.substring(0, variableEndIndex));
        }
        return variableSign.toString();
    }

    private String getVariableValue(Map<String, String> variableMap, String variableSign) {
        return variableMap.getOrDefault(variableSign, "NA");
    }

    private CTRPr getVariableCTR(XWPFParagraph paragraph, int startRunIndex, int endRunIndex) {
        if (startRunIndex == endRunIndex) {
            return paragraph.getRuns().get(startRunIndex).getCTR().getRPr();
        }
        else {
            return paragraph.getRuns().get(startRunIndex + 1).getCTR().getRPr();
        }
    }

    private void replaceSingleVariableInSingleRun(XWPFParagraph paragraph, int runIndex, String variableValue, CTRPr variableCTR) {
        String runContent = paragraph.getRuns().get(runIndex).text();
        int variableStartIndex = runContent.indexOf(ETemplateVariable.VARIABLE_PREFIX);
        int variableEndIndex = runContent.indexOf(ETemplateVariable.VARIABLE_SUFFIX);
        String replacedRunContent = runContent.substring(0, variableStartIndex)
                + variableValue + runContent.substring(variableEndIndex + 1);
        XWPFRun replacedRun = paragraph.insertNewRun(runIndex);
        replacedRun.setText(replacedRunContent);
        replacedRun.getCTR().setRPr(variableCTR);
        paragraph.removeRun(runIndex + 1);
    }

    private void replaceSingleVariableInMultiRuns(XWPFParagraph paragraph, int startRunIndex, int endRunIndex, String variableValue, CTRPr variableCTR) {
        XWPFRun originStartRun = paragraph.getRuns().get(startRunIndex);
        XWPFRun originEndRun = paragraph.getRuns().get(endRunIndex);

        int insertRunIndex = startRunIndex;
        // 如果原startRun内有{之外的内容，插入新的startRun
        if (!originStartRun.text().equals(ETemplateVariable.VARIABLE_PREFIX)) {
            String originStartRunContent = originStartRun.text();
            int variableStartIndex = originStartRunContent.indexOf(ETemplateVariable.VARIABLE_PREFIX);
            String newStartRunContent = originStartRunContent.substring(0, variableStartIndex);
            XWPFRun newStartRun = paragraph.insertNewRun(insertRunIndex);
            newStartRun.setText(newStartRunContent);
            newStartRun.getCTR().setRPr(originStartRun.getCTR().getRPr());
            ++insertRunIndex;
        }

        // 插入新的variableRun
        XWPFRun variableRun = paragraph.insertNewRun(insertRunIndex);
        variableRun.setText(variableValue);
        variableRun.getCTR().setRPr(variableCTR);
        ++insertRunIndex;

        // 如果原endRun内有}之外的内容，插入新的startRun
        if (!originEndRun.text().equals(ETemplateVariable.VARIABLE_SUFFIX)) {
            String originEndRunContent = originEndRun.text();
            int variableEndIndex = originEndRunContent.indexOf(ETemplateVariable.VARIABLE_SUFFIX);
            String newEndRunContent = originEndRunContent.substring(variableEndIndex + 1);
            XWPFRun newEndRun = paragraph.insertNewRun(insertRunIndex);
            newEndRun.setText(newEndRunContent);
            newEndRun.getCTR().setRPr(originEndRun.getCTR().getRPr());
            ++insertRunIndex;
        }

        for (int i = 0; i != endRunIndex - startRunIndex + 1; ++i) {
            paragraph.removeRun(insertRunIndex);
        }
    }

    private void generateParagraph(XWPFDocument destDoc, XWPFParagraph paragraph, GroupSnapshot groupSnapshot) {
        destDoc.createParagraph();
        replaceVariables(paragraph, groupSnapshot);
        int pos = destDoc.getParagraphs().size() - 1;
        destDoc.setParagraph(paragraph, pos);
    }



    private List<ETemplateVariable> getColumnVariables(XWPFTableRow templateRow) {
        List<ETemplateVariable> columnVariables = new ArrayList<>();
        for (XWPFTableCell cell : templateRow.getTableCells()) {
            ETemplateVariable variable = ETemplateVariable.getVariableFromSign(cell.getText());
            columnVariables.add(variable);
        }
        return columnVariables;
    }

    private String getOutFileFullName(String groupName, String outPath) throws IOException {
        String outFileFullName = outPath + groupName + ".docx";
        return outFileFullName;
    }

    private void replaceVariables(XWPFParagraph paragraph, GroupSnapshot groupSnapshot) {
        String paragraphContent = paragraph.getText();
        Pattern variablePattern = Pattern.compile("\\{[a-zA-Z]+\\}");
        Matcher matcher = variablePattern.matcher(paragraphContent);

        while (matcher.find()) {
            System.out.println(matcher.group());
        }
        while (true) {
            int variableBeginRunIndex = getFirstIndexOf(paragraph, ETemplateVariable.VARIABLE_PREFIX, 0);
            if (variableBeginRunIndex == -1) {
                break;
            }
            else {
                int variableEndRunIndex = getFirstIndexOf(paragraph, ETemplateVariable.VARIABLE_SUFFIX, variableBeginRunIndex);
                if (variableEndRunIndex == -1) {
                    break;
                }
                String content = "";
                for (int i = variableBeginRunIndex; i <= variableEndRunIndex; ++i) {
                    content += paragraph.getRuns().get(i);
                }
                String replacedContent = replaceSingleVariableContent(content, groupSnapshot);
                paragraph.getRuns().get(variableBeginRunIndex).setText(replacedContent, 0);
                for (int i = variableBeginRunIndex + 1; i <= variableEndRunIndex; ++i) {
                    paragraph.removeRun(variableBeginRunIndex + 1);
                }
            }
        }

    }

    private int getFirstIndexOf(XWPFParagraph paragraph, String sign, int startIndex) {
        for (int i = startIndex; i != paragraph.getRuns().size(); ++i) {
            XWPFRun run = paragraph.getRuns().get(i);
            if (run.getText(0).contains(sign)) {
                return i;
            }
        }
        return -1;
    }

    private String replaceSingleVariableContent(String originContent, GroupSnapshot groupSnapshot) {
        String replacedContent = originContent;
        int begin = originContent.indexOf(ETemplateVariable.VARIABLE_PREFIX);
        int end = originContent.indexOf(ETemplateVariable.VARIABLE_SUFFIX, begin);
        if (begin < 0 || end < begin) {
            return replacedContent;
        }
        else {
            String variableSign = originContent.substring(begin, end + 1);
            if (variableSign.equals(ETemplateVariable.GROUP_NAME.getVariableSign())) {
                replacedContent = originContent.replace(variableSign, groupSnapshot.getGroupName());
            }
            else if (variableSign.equals(ETemplateVariable.GROUP_MEMBER_COUNT.getVariableSign())) {
                replacedContent = originContent.replace(variableSign, String.valueOf(groupSnapshot.getGroupMemberCount()));
            }
            else if (variableSign.equals(ETemplateVariable.GROUP_BALANCE.getVariableSign())) {
                replacedContent = originContent.replace(variableSign,
                        formatBalance(groupSnapshot.getGroupBalance()));
            }
            else if (variableSign.equals(ETemplateVariable.GROUP_LOAN_BALANCE.getVariableSign())) {
                replacedContent = originContent.replace(variableSign,
                        formatBalance(groupSnapshot.getGroupLoanBalance()));
            }
            else if (variableSign.equals(ETemplateVariable.GROUP_TRADE_IN_SHEET_BALANCE.getVariableSign())) {
                replacedContent = originContent.replace(variableSign,
                        formatBalance(groupSnapshot.getGroupTradeInSheetBalance()));
            }
            else if (variableSign.equals(ETemplateVariable.GROUP_TRADE_OUT_SHEET_BALANCE.getVariableSign())) {
                replacedContent = originContent.replace(variableSign,
                        formatBalance(groupSnapshot.getGroupTradeOutSheetBalance()));
            }
            else if (variableSign.equals(ETemplateVariable.GROUP_BRANCH_COUNT.getVariableSign())) {
                replacedContent = originContent.replace(variableSign, String.valueOf(groupSnapshot.getGroupBranchCount()));
            }
            return replacedContent;
        }
    }

    private static String formatBalance(double balance) {
        return String.format("%.2f", balance / 100000000);
    }
}
