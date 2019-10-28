package cn.gov.eximbank.customerforecast.model;

import org.springframework.data.annotation.Id;

public class Group {

    @Id
    private String id;

    private String branchId;

    public Group(String id, String branchId) {
        this.id = id;
        this.branchId = branchId;
    }

    public String getId() {
        return id;
    }

    public String getBranchId() {
        return branchId;
    }
}
