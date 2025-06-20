package com.qst.itoffer.bean;

/**
 * Represents an enterprise entity in the system.
 * Contains company profile, status, and related information.
 */
public class Company {
    // Company ID
    private int companyId;
    // Company name
    private String companyName;
    // Company area
    private String companyArea;
    // Company size
    private String companySize;
    // Company type
    private String companyType;
    // Company brief/introduction
    private String companyBrief;
    // Company recruitment status (1: recruiting, 2: paused, 3: closed)
    private int companyState;
    // Sort order
    private int companySort;
    // View count
    private int companyViewnum;
    // Company picture
    private String companyPic;

    /**
     * Default constructor.
     */
    public Company() {}

    /**
     * Constructor for creating a company with all fields except ID.
     */
    public Company(String companyName, String companyArea, String companySize, String companyType, String companyBrief, int companyState, int companySort, int companyViewnum, String companyPic) {
        this.companyName = companyName;
        this.companyArea = companyArea;
        this.companySize = companySize;
        this.companyType = companyType;
        this.companyBrief = companyBrief;
        this.companyState = companyState;
        this.companySort = companySort;
        this.companyViewnum = companyViewnum;
        this.companyPic = companyPic;
    }

    /**
     * Full constructor including companyId.
     */
    public Company(int companyId, String companyName, String companyArea, String companySize, String companyType, String companyBrief, int companyState, int companySort, int companyViewnum, String companyPic) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyArea = companyArea;
        this.companySize = companySize;
        this.companyType = companyType;
        this.companyBrief = companyBrief;
        this.companyState = companyState;
        this.companySort = companySort;
        this.companyViewnum = companyViewnum;
        this.companyPic = companyPic;
    }

    /**
     * Gets the company ID.
     */
    public int getCompanyId() { return companyId; }

    /**
     * Sets the company ID.
     */
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    /**
     * Gets the company name.
     */
    public String getCompanyName() { return companyName; }

    /**
     * Sets the company name.
     */
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    /**
     * Gets the company area.
     */
    public String getCompanyArea() { return companyArea; }

    /**
     * Sets the company area.
     */
    public void setCompanyArea(String companyArea) { this.companyArea = companyArea; }

    /**
     * Gets the company size.
     */
    public String getCompanySize() { return companySize; }

    /**
     * Sets the company size.
     */
    public void setCompanySize(String companySize) { this.companySize = companySize; }

    /**
     * Gets the company type.
     */
    public String getCompanyType() { return companyType; }

    /**
     * Sets the company type.
     */
    public void setCompanyType(String companyType) { this.companyType = companyType; }

    /**
     * Gets the company brief/introduction.
     */
    public String getCompanyBrief() { return companyBrief; }

    /**
     * Sets the company brief/introduction.
     */
    public void setCompanyBrief(String companyBrief) { this.companyBrief = companyBrief; }

    /**
     * Gets the company recruitment status.
     */
    public int getCompanyState() { return companyState; }

    /**
     * Sets the company recruitment status.
     */
    public void setCompanyState(int companyState) { this.companyState = companyState; }

    /**
     * Gets the sort order.
     */
    public int getCompanySort() { return companySort; }

    /**
     * Sets the sort order.
     */
    public void setCompanySort(int companySort) { this.companySort = companySort; }

    /**
     * Gets the view count.
     */
    public int getCompanyViewnum() { return companyViewnum; }

    /**
     * Sets the view count.
     */
    public void setCompanyViewnum(int companyViewnum) { this.companyViewnum = companyViewnum; }

    /**
     * Gets the company picture.
     */
    public String getCompanyPic() { return companyPic; }

    /**
     * Sets the company picture.
     */
    public void setCompanyPic(String companyPic) { this.companyPic = companyPic; }
}