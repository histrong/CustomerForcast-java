package cn.gov.eximbank.customerforcast.report;

public enum ETemplateVariable {

    GROUP_NAME("GroupName"),
    GROUP_MEMBER_COUNT("GroupMemberCount"),
    GROUP_BRANCH_COUNT("GroupBranchCount"),
    GROUP_BALANCE("GroupBalance"),
    GROUP_LOAN_BALANCE("GroupLoanBalance"),
    GROUP_TRADE_IN_SHEET_BALANCE("GroupTradeInSheetBalance"),
    GROUP_TRADE_OUT_SHEET_BALANCE("GroupTradeOutSheetBalance"),

    BRANCH_NAME("BranchName"),
    BRANCH_LOAN_BALANCE("BranchLoanBalance"),
    BRANCH_TRADE_IN_SHEET_BALANCE("BranchTradeInSheetBalance"),
    BRANCH_TRADE_OUT_SHEET_BALANCE("BranchTradeOutSheetBalance"),
    BRANCH_IN_SHEET_BALANCE("BranchInSheetBalance"),
    BRANCH_BALANCE("BranchBalance");

    private String variableSign;

    public static String VARIABLE_PREFIX = "{";
    public static String VARIABLE_SUFFIX = "}";

    ETemplateVariable(String variableSign) {
        this.variableSign = variableSign;
    }

    public String getVariableSign() {
        return variableSign;
    }

    public static ETemplateVariable getVariableFromSign(String variableSign) {
        for (ETemplateVariable variable : ETemplateVariable.values()) {
            if (variable.getVariableSign().equals(variableSign)) {
                return variable;
            }
        }
        return null;
    }
}
