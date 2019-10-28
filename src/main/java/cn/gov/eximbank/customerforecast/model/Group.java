package cn.gov.eximbank.customerforecast.model;

import org.springframework.data.annotation.Id;

public class Group {

    @Id
    private String id;

    private String name;

    private String branchId;

    public Group(String id, String name, String branchId) {
        this.id = id;
        this.name = name;
        this.branchId = branchId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBranchId() {
        return branchId;
    }
}
