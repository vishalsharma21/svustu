package com.ritara.svustudent.utils;

public class FeeModel {

    String Branch;
    String Course;
    String Credit;
    String Debit;
    String Description;
    String EnrollNo;
    String FName;
    String Name;
    String ReceiptNo;
    String TransDate;
    String Fac_name;
    String Emp_id;

    public String getEmp_id() {
        return Emp_id;
    }

    public void setEmp_id(String emp_id) {
        Emp_id = emp_id;
    }

    public void getFac_name(String fac_name){
        Fac_name = fac_name;
    }

    public void setFac_name(String fac_name) {
        Fac_name = fac_name;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getDebit() {
        return Debit;
    }

    public void setDebit(String debit) {
        Debit = debit;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEnrollNo() {
        return EnrollNo;
    }

    public void setEnrollNo(String enrollNo) {
        EnrollNo = enrollNo;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getReceiptNo() {
        return ReceiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        ReceiptNo = receiptNo;
    }

    public String getTransDate() {
        return TransDate;
    }

    public void setTransDate(String transDate) {
        TransDate = transDate;
    }
}
