package cn.gov.eximbank.customerforecast.cmd;

import cn.gov.eximbank.customerforecast.model.*;
import cn.gov.eximbank.customerforecast.repository.BusinessRepository;
import cn.gov.eximbank.customerforecast.repository.CustomerRepository;
import cn.gov.eximbank.customerforecast.repository.GroupRepository;
import cn.gov.eximbank.customerforecast.repository.GroupSnapshotRepository;

import java.util.*;

public class GenerateGroupSnapshotCommand implements ICommand {

    private static String commandName = "GenerateGroupSnapshotCommand";

    private GroupRepository groupRepository;

    private CustomerRepository customerRepository;

    private BusinessRepository businessRepository;

    private GroupSnapshotRepository groupSnapshotRepository;

    private String period;

    public GenerateGroupSnapshotCommand(String period,
                                        GroupRepository groupRepository,
                                        CustomerRepository customerRepository,
                                        BusinessRepository businessRepository,
                                        GroupSnapshotRepository groupSnapshotRepository) {
        this.period = period;
        this.groupRepository = groupRepository;
        this.customerRepository = customerRepository;
        this.businessRepository = businessRepository;
        this.groupSnapshotRepository = groupSnapshotRepository;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public void execute() {
        //1. 清空所有的集团情况快照
        groupSnapshotRepository.deleteAll();

        //2. 找到所有的集团列表
        List<Group> groups = groupRepository.findAll();

        //3. 遍历每个集团，生成快照
        for (Group group : groups) {
            generateGroupSnapshot(group);
        }
    }

    private void generateGroupSnapshot(Group group) {
        //1. 找到集团的所有子成员
        List<Customer> customers = customerRepository.findAllByGroupId(group.getId());
        if (customers.isEmpty()) {
            return;
        }

        //2. 业务仓库里出现集团的子成员，才需要进行快照
        GroupSnapshot groupSnapshot = new GroupSnapshot(group.getId(), group.getName(), customers.size());
        Map<String, GroupSnapshotInBranch> groupSnapshotInBranchMap = new HashMap<>();

        //3. 把该集团所有成员的id保存到集合中
        Set<String> customerIds = new HashSet<>();
        for (Customer customer : customers) {
            customerIds.add(customer.getId());
        }

        //4. 遍历所有的业务，把属于该集团的业务累加
        List<Business> businesses = businessRepository.findAll();
        for (Business business : businesses) {
            String customerId = business.getCustomerId(period);
            //如果是该集团的成员
            if (customerIds.contains(customerId)) {
                recordBusiness(business, groupSnapshotInBranchMap);
            }
        }

        //5. 对结果进行排序和最后处理
        List<GroupSnapshotInBranch> groupSnapshotInBranches = new ArrayList<>();
        groupSnapshotInBranches.addAll(groupSnapshotInBranchMap.values());
        Collections.sort(groupSnapshotInBranches, new Comparator<GroupSnapshotInBranch>() {
            @Override
            public int compare(GroupSnapshotInBranch gs1, GroupSnapshotInBranch gs2) {
                if (gs1.getBranchBalance() < gs2.getBranchBalance()) {
                    return 1;
                }
                else if (gs1.getBranchBalance() == gs2.getBranchBalance()) {
                    return 0;
                }
                else {
                    return -1;
                }
            }
        });

        groupSnapshot.addGroupSnapshotInBranches(groupSnapshotInBranches);
        groupSnapshot.calculateGroupBalances();

        groupSnapshotRepository.save(groupSnapshot);
    }

    private void recordBusiness(Business business, Map<String, GroupSnapshotInBranch> groupSnapshotInBranchMap) {
        String branchId = business.getBranchId(period);
        double balance = business.getBalance(period);
        if (!groupSnapshotInBranchMap.containsKey(branchId)) {
            EBranch branch = EBranch.valueOf(branchId);
            GroupSnapshotInBranch groupSnapshotInBranch = new GroupSnapshotInBranch(branchId, branch.getBranchName());
            groupSnapshotInBranchMap.put(branchId, groupSnapshotInBranch);
        }
        switch (business.getType()) {
            case LN: groupSnapshotInBranchMap.get(branchId).addBranchLoanBalance(balance);break;
            case TI: groupSnapshotInBranchMap.get(branchId).addBranchTradeInSheetBalance(balance);break;
            case TO: groupSnapshotInBranchMap.get(branchId).addBranchTradeOutSheetBalance(balance);break;
        }
    }
}
