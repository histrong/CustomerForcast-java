package cn.gov.eximbank.customerforecast.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Business {

    class BusinessDetail {

        String period;

        String customerId;

        String branchId;

        double balance;

        BusinessDetail(String period, String customerId, String branchId, double balance) {
            this.period = period;
            this.customerId = customerId;
            this.branchId = branchId;
            this.balance = balance;
        }
    }

    @Id
    private String id;

    private List<BusinessDetail> businessDetails;

    public Business(String id) {
        this.id = id;
        this.businessDetails = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getCustomerId(String period) {
        BusinessDetail detail = getBusinessDetail(period);
        if (detail == null) {
            return null;
        }
        else {
            return detail.customerId;
        }
    }

    public String getCustomerId() {
        BusinessDetail latestDetail = getLatestBusinessDetail();
        if (latestDetail == null) {
            return null;
        }
        else {
            return latestDetail.customerId;
        }
    }

    public String getBranchId(String period) {
        BusinessDetail detail = getBusinessDetail(period);
        if (detail == null) {
            return null;
        }
        else {
            return detail.branchId;
        }
    }

    public String getBranchId() {
        BusinessDetail latestDetail = getLatestBusinessDetail();
        if (latestDetail == null) {
            return null;
        }
        else {
            return latestDetail.branchId;
        }
    }

    public double getBalance(String period) {
        BusinessDetail detail = getBusinessDetail(period);
        if (detail == null) {
            return 0;
        }
        else {
            return detail.balance;
        }
    }

    public double getLatestBalance() {
        BusinessDetail latestDetail = getLatestBusinessDetail();
        if (latestDetail == null) {
            return 0;
        }
        else {
            return latestDetail.balance;
        }
    }

    public void addBusinessDetail(String period, String customerId, String branchId, double balance) {
        BusinessDetail detail = getBusinessDetail(period);
        if (detail == null) {
            businessDetails.add(new BusinessDetail(period, customerId, branchId, balance));
        }
        else {
            detail.customerId = customerId;
            detail.branchId = branchId;
            detail.balance = balance;
        }
    }

    public void delete(String period) {
        BusinessDetail detail = getBusinessDetail(period);
        if (detail != null) {
            businessDetails.remove(detail);
        }
    }

    private BusinessDetail getBusinessDetail(String period) {
        for (BusinessDetail detail : businessDetails) {
            if (detail.period.equals(period)) {
                return detail;
            }
        }
        return null;
    }

    private BusinessDetail getLatestBusinessDetail() {
        if (businessDetails.isEmpty()) {
            return null;
        }
        else {
            int index = businessDetails.size() - 1;
            return businessDetails.get(index);
        }
    }
}
