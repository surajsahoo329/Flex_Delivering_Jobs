package com.example.flex;

public class DL {

    private String userId;
    private String userMail;
    private String userName;
    private String LicenseNumber;
    private String userDOB;
    private String userAddress;
    private String LicenseIssueDate;
    private String LicenseExpiryDate;
    private int userDLFlag ;

    //Constructor
    //Function Overloading(Look it up)

    //Another constructor to initialize our variables
    DL(String userId,String userMail, String userName, String LicenseNumber, String userDOB, String userAddress, String LicenseIssueDate,String LicenseExpiryDate, int userDLFlag) {
        this.userId = userId;
        this.userMail=userMail;
        this.userName = userName;
        this.LicenseNumber = LicenseNumber;
        this.userDOB = userDOB;
        this.userAddress = userAddress;
        this.LicenseIssueDate=LicenseIssueDate;
        this.LicenseExpiryDate=LicenseExpiryDate;
        this.userDLFlag = userDLFlag;

    }


    public String getUserMail() {
        return userMail;
    }

    public String getUserName() {
        return userName;
    }

    public String getLicenseNumber() {
        return LicenseNumber;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public String getUserAddress() { return userAddress; }

    public String getLicenseIssueDate(){
        return LicenseIssueDate;
    }

    public String getLicenseExpiryDate() {
        return LicenseExpiryDate;
    }

    public int getUserDLFlag() { return userDLFlag; }

}
