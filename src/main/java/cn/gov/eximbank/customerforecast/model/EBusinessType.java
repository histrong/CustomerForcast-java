package cn.gov.eximbank.customerforecast.model;

public enum EBusinessType {

    LN("贷款"),
    TI("表内贸金"),
    TO("表外贸金");

    private String businessType;

    EBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessType() {
        return businessType;
    }
}
