package cn.gov.eximbank.customerforecast.analyzer;

import cn.gov.eximbank.customerforecast.report.ETemplateVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupAnalyzeResult {

    private String groupName;

    private int groupMemberCount;

    private double groupLoanBalance;

    private double groupTradeInSheetBalance;

    private double groupTradeOutSheetBalance;

    private List<BranchAnalyzeResult> branchAnalyzeResults;

    public GroupAnalyzeResult(String groupName, int groupMemberCount,
                              double groupLoanBalance,
                              double groupTradeInSheetBalance,
                              double groupTradeOutSheetBalance) {
        this.groupName = groupName;
        this.groupMemberCount = groupMemberCount;
        this.groupLoanBalance = groupLoanBalance;
        this.groupTradeInSheetBalance = groupTradeInSheetBalance;
        this.groupTradeOutSheetBalance = groupTradeOutSheetBalance;
        branchAnalyzeResults = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public int getGroupMemberCount() {
        return groupMemberCount;
    }

    public int getGroupBranchCount() {
        return branchAnalyzeResults.size();
    }

    public double getGroupBalance() {
        return groupLoanBalance + groupTradeInSheetBalance + groupTradeOutSheetBalance;
    }

    public double getGroupLoanBalance() {
        return groupLoanBalance;
    }

    public double getGroupTradeInSheetBalance() {
        return groupTradeInSheetBalance;
    }

    public double getGroupTradeOutSheetBalance() {
        return groupTradeOutSheetBalance;
    }

    public List<BranchAnalyzeResult> getBranchAnalyzeResults() {
        return branchAnalyzeResults;
    }

    public void addBranchAnalyzeResult(BranchAnalyzeResult branchAnalyzeResult) {
        this.branchAnalyzeResults.add(branchAnalyzeResult);
    }

    public Map<String, String> toVariableMap() {
        Map<String, String> variableMap = new HashMap<>();
        variableMap.put(ETemplateVariable.GROUP_NAME.getVariableSign(), getGroupName());
        variableMap.put(ETemplateVariable.GROUP_MEMBER_COUNT.getVariableSign(), String.valueOf(getGroupMemberCount()));
        variableMap.put(ETemplateVariable.GROUP_BRANCH_COUNT.getVariableSign(), String.valueOf(getGroupBranchCount()));
        variableMap.put(ETemplateVariable.GROUP_BALANCE.getVariableSign(), formatDouble(getGroupBalance(), 100000000));
        variableMap.put(ETemplateVariable.GROUP_LOAN_BALANCE.getVariableSign(),
                formatDouble(getGroupLoanBalance(), 100000000));
        variableMap.put(ETemplateVariable.GROUP_TRADE_IN_SHEET_BALANCE.getVariableSign(),
                formatDouble(getGroupTradeInSheetBalance(), 100000000));
        variableMap.put(ETemplateVariable.GROUP_TRADE_OUT_SHEET_BALANCE.getVariableSign(),
                formatDouble(getGroupTradeOutSheetBalance(), 100000000));
        return variableMap;
    }

    private String formatDouble(double value, int base) {
        return String.format("%.2f", value / base);
    }
}
