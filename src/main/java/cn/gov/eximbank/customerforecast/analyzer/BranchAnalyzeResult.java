package cn.gov.eximbank.customerforecast.analyzer;

import cn.gov.eximbank.customerforecast.report.ETemplateVariable;

import java.util.HashMap;
import java.util.Map;

public class BranchAnalyzeResult {

    private String branchName;

    private double branchLoanBalance;

    private double branchTradeInSheetBalance;

    private double branchTradeOutSheetBalance;

    public BranchAnalyzeResult(String branchName,
                               double branchLoanBalance,
                               double branchTradeInSheetBalance,
                               double branchTradeOutSheetBalance) {
        this.branchName = branchName;
        this.branchLoanBalance = branchLoanBalance;
        this.branchTradeInSheetBalance = branchTradeInSheetBalance;
        this.branchTradeOutSheetBalance = branchTradeOutSheetBalance;
    }

    public String getBranchName() {
        return branchName;
    }

    public double getBranchLoanBalance() {
        return branchLoanBalance;
    }

    public double getBranchTradeInSheetBalance() {
        return branchTradeInSheetBalance;
    }

    public double getBranchTradeOutSheetBalance() {
        return branchTradeOutSheetBalance;
    }

    public double getBranchInSheetBalance() {
        return branchLoanBalance + branchTradeInSheetBalance;
    }

    public double getBranchBalance() {
        return branchLoanBalance + branchTradeInSheetBalance + branchTradeOutSheetBalance;
    }

    public Map<String, String> toVariableMap() {
        Map<String, String> variableMap = new HashMap<>();
        variableMap.put(ETemplateVariable.BRANCH_NAME.getVariableSign(), getBranchName());
        variableMap.put(ETemplateVariable.BRANCH_LOAN_BALANCE.getVariableSign(),
                formatDouble(getBranchLoanBalance(), 100000000));
        variableMap.put(ETemplateVariable.BRANCH_TRADE_IN_SHEET_BALANCE.getVariableSign(),
                formatDouble(getBranchTradeInSheetBalance(), 100000000));
        variableMap.put(ETemplateVariable.BRANCH_TRADE_OUT_SHEET_BALANCE.getVariableSign(),
                formatDouble(getBranchTradeOutSheetBalance(), 100000000));
        variableMap.put(ETemplateVariable.BRANCH_IN_SHEET_BALANCE.getVariableSign(),
                formatDouble(getBranchInSheetBalance(), 100000000));
        variableMap.put(ETemplateVariable.BRANCH_BALANCE.getVariableSign(),
                formatDouble(getBranchBalance(), 100000000));
        return variableMap;
    }

    private String formatDouble(double value, int base) {
        return String.format("%.2f", value / base);
    }
}
