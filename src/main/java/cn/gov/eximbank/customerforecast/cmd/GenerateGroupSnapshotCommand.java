package cn.gov.eximbank.customerforecast.cmd;

import cn.gov.eximbank.customerforecast.model.Customer;
import cn.gov.eximbank.customerforecast.model.Group;
import cn.gov.eximbank.customerforecast.model.GroupSnapshot;
import cn.gov.eximbank.customerforecast.repository.BusinessRepository;
import cn.gov.eximbank.customerforecast.repository.CustomerRepository;
import cn.gov.eximbank.customerforecast.repository.GroupRepository;
import cn.gov.eximbank.customerforecast.repository.GroupSnapshotRepository;

import java.util.List;

public class GenerateGroupSnapshotCommand implements ICommand {

    private static String commandName = "GenerateGroupSnapshotCommand";

    private GroupRepository groupRepository;

    private CustomerRepository customerRepository;

    private BusinessRepository businessRepository;

    private GroupSnapshotRepository groupSnapshotRepository;

    private String period;

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
        GroupSnapshot groupSnapshot = new GroupSnapshot(group.getId(), group.getName(), customers.size(), 0, 0, 0);
        //TODO 继续实现
    }
}
