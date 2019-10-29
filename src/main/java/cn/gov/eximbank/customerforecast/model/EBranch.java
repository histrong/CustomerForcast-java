package cn.gov.eximbank.customerforecast.model;

public enum EBranch {
    GN("��˾�ͻ���", "��˾"),
    JY("��ͨ�������ʲ�", "��ͨ"),
    ZQ("��Ȩ�ͻ���", "��Ȩ"),
    ZD("ת����", "ת��"),
    TZ("Ͷ�ʹ���", "Ͷ��"),
    BJ("��������", "����"),
    SH("�Ϻ�����", "�Ϻ�"),
    SZ("���ڷ���", "����"),
    JS("����ʡ����", "����"),
    LN("����ʡ����", "����"),
    SC("�Ĵ�ʡ����", "�Ĵ�"),
    SD("ɽ��ʡ����", "ɽ��"),
    ZJ("�㽭ʡ����", "�㽭"),
    HN("����ʡ����", "����"),
    CQ("�������", "����"),
    SN("����ʡ����", "����"),
    HB("����ʡ����", "����"),
    HL("������ʡ����", "������"),
    GD("�㶫ʡ����", "�㶫"),
    YN("����ʡ����", "����"),
    NB("��������", "����"),
    FJ("����ʡ����", "����"),
    AH("����ʡ����", "����"),
    XJ("�½�ά�������������", "�½�"),
    XM("���ŷ���", "����"),
    TJ("������", "���"),
    JX("����ʡ����", "����"),
    HI("����ʡ����", "����"),
    JL("����ʡ����", "����"),
    HE("�ӱ�ʡ����", "�ӱ�"),
    GS("����ʡ����", "����"),
    HA("����ʡ����", "����"),
    NM("���ɹ�����������", "����"),
    SX("ɽ��ʡ����", "ɽ��"),
    GX("����׳������������", "����"),
    GZ("����ʡ����", "����"),
    KS("��ʲ����", "��ʲ"),
    BL("�������", "����"),
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
