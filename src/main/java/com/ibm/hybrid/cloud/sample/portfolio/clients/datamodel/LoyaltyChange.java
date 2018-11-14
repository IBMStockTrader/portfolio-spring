package com.ibm.hybrid.cloud.sample.portfolio.clients.datamodel;

public class LoyaltyChange {
    private String fOwner;
    private String fOld;
    private String fNew;
    private String fId;


    public LoyaltyChange() { //default constructor
    }

    public LoyaltyChange(String initialOwner, String initialOldLoyalty, String initialNewLoyalty) {
        setOwner(initialOwner);
        setOld(initialOldLoyalty);
        setNew(initialNewLoyalty);
    }

    public String getOwner() {
        return fOwner;
    }

    public void setOwner(String initialOwner) {
        fOwner = initialOwner;
    }

    public String getOld() {
        return fOld;
    }

    public void setOld(String initialOldLoyalty) {
        fOld = initialOldLoyalty;
    }

    public String getNew() {
        return fNew;
    }

    public void setNew(String initialNewLoyalty) {
        fNew = initialNewLoyalty;
    }

    public String getId() {
        return fId;
    }

    public void setId(String initialId) {
        fId = initialId;
    }
}