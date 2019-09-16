package cn.gov.eximbank.customerforcast.model;

import org.springframework.data.annotation.Id;

public class Customer {

    @Id
    private String id;

    private String groupId;

    public Customer(String id, String groupId) {
        this.id = id;
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }
}
