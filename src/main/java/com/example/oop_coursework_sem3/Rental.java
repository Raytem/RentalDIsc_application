package com.example.oop_coursework_sem3;

public class Rental {
    private Integer rentalId;
    private Integer discId;
    private String userPhone;
    private String issueDate;
    private String returnDate;
    private Boolean isReturned;

    public Rental(Integer rentalId, Integer discId, String userPhone, String issueDate, String returnDate, Boolean isReturned) {
        this.rentalId = rentalId;
        this.discId = discId;
        this.userPhone = userPhone;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.isReturned = isReturned;
    }

    public Rental(String userPhone) {
        this.userPhone = userPhone;
    }

    public Rental(Integer discId) {
        this.discId = discId;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public Integer getDiscId() {
        return discId;
    }

    public void setDiscId(Integer discId) {
        this.discId = discId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public Boolean getIsReturned() {
        return isReturned;
    }

    public void setIsReturned(Boolean isReturned) {
        this.isReturned = isReturned;
    }

    @Override
    public String toString() {
        return discId +
                " _ " + userPhone +
                " _ " + issueDate +
                " _ " + returnDate +
                " _ ";
    }

}
