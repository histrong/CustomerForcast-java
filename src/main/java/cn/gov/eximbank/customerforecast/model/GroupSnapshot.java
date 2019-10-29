package cn.gov.eximbank.customerforecast.model;

import cn.gov.eximbank.customerforecast.report.ETemplateVariable;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupSnapshot {

    @Id
    private String id;

    private String groupName;

    private int groupMemberCount;

    private double groupLoanBalance;

    private double groupTradeInSheetBalance;

    private double groupTradeOutSheetBalance;

    private List<GroupSnapshotInBranch> groupSnapshotInBranches;

    public GroupSnapshot() {
        this("", "", 0);
    }

    public GroupSnapshot(String id, String groupName, int groupMemberCount) {
        this.id = id;
        this.groupName = groupName;
        this.groupMemberCount = groupMemberCount;
        this.groupLoanBalance = 0;
        this.groupTradeInSheetBalance = 0;
        this.groupTradeOutSheetBalance = 0;
        groupSnapshotInBranches = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getGroupMemberCount() {
        return groupMemberCount;
    }

    public int getGroupBranchCount() {
        return groupSnapshotInBranches.size();
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

    public List<GroupSnapshotInBranch> getGroupSnapshotInBranches() {
        return groupSnapshotInBranches;
    }

    public void addGroupSnapshotInBranch(GroupSnapshotInBranch groupSnapshotInBranch) {
        this.groupSnapshotInBranches.add(groupSnapshotInBranch);
    }

    public void addGroupSnapshotInBranches(List<GroupSnapshotInBranch> groupSnapshotInBranches) {
        this.groupSnapshotInBranches.addAll(groupSnapshotInBranches);
    }

    public void calculateGroupBalances() {
        for (GroupSnapshotInBranch groupSnapshotInBranch : groupSnapshotInBranches) {
            groupLoanBalance += groupSnapshotInBranch.getBranchLoanBalance();
            groupTradeInSheetBalance += groupSnapshotInBranch.getBranchTradeInSheetBalance();
            groupTradeOutSheetBalance += groupSnapshotInBranch.getBranchTradeOutSheetBalance();
        }
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
