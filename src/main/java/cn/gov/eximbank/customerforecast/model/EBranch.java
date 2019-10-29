package cn.gov.eximbank.customerforecast.model;

public enum EBranch {
    GN("公司客户部", "公司"),
    JY("交通运输融资部", "交通"),
    ZQ("主权客户部", "主权"),
    ZD("转贷部", "转贷"),
    TZ("投资管理部", "投资"),
    BJ("北京分行", "北京"),
    SH("上海分行", "上海"),
    SZ("深圳分行", "深圳"),
    JS("江苏省分行", "江苏"),
    LN("辽宁省分行", "辽宁"),
    SC("四川省分行", "四川"),
    SD("山东省分行", "山东"),
    ZJ("浙江省分行", "浙江"),
    HN("湖南省分行", "湖南"),
    CQ("重庆分行", "重庆"),
    SN("陕西省分行", "陕西"),
    HB("湖北省分行", "湖北"),
    HL("黑龙江省分行", "黑龙江"),
    GD("广东省分行", "广东"),
    YN("云南省分行", "云南"),
    NB("宁波分行", "宁波"),
    FJ("福建省分行", "福建"),
    AH("安徽省分行", "安徽"),
    XJ("新疆维吾尔自治区分行", "新疆"),
    XM("厦门分行", "厦门"),
    TJ("天津分行", "天津"),
    JX("江西省分行", "江西"),
    HI("海南省分行", "海南"),
    JL("吉林省分行", "吉林"),
    HE("河北省分行", "河北"),
    GS("甘肃省分行", "甘肃"),
    HA("河南省分行", "河南"),
    NM("内蒙古自治区分行", "内蒙"),
    SX("山西省分行", "山西"),
    GX("广西壮族自治区分行", "广西"),
    GZ("贵州省分行", "贵州"),
    KS("喀什分行", "喀什"),
    BL("巴黎分行", "巴黎"),
    NONE("NONE", "NONE");

    private String branchName;
    private String branchBrief;

    EBranch(String branchName, String branchBrief) {
        this.branchName = branchName;
        this.branchBrief = branchBrief;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getBranchBrief() {
        return branchBrief;
    }

    public static EBranch getBranchId(String str) {
        for (EBranch branch : values()) {
            if (str.startsWith(branch.branchBrief)) {
                return branch;
            }
        }
        return NONE;
    }
}
