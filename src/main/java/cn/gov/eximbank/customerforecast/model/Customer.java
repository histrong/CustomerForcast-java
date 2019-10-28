package cn.gov.eximbank.customerforecast.model;

import org.springframework.data.annotation.Id;

public class Customer {

    @Id
    private String id;

    private String name;

    private String groupId;

    private String branchId;

    public Customer(String id, String name, String groupId, String branchId) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
        this.branchId = branchId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getBranchId() {
        return branchId;
    }
}
