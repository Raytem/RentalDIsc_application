package com.example.oop_coursework_sem3;

import java.util.Objects;

public class RentalUser extends Rental {
    private String userName;
    private String surname;
    private String patronymic;
    private String address;

    public RentalUser(String surname, String userName, String patronymic, String address, String userPhone) {
        super(userPhone);
        this.userName = userName;
        this.surname = surname;
        this.patronymic = patronymic;
        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RentalUser user = (RentalUser) obj;

        return surname.equals(user.surname) && userName.equals(user.userName) && patronymic.equals(user.patronymic)
                && address.equals(user.address) && getUserPhone().equals(user.getUserPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, surname, patronymic, address, getUserPhone());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return surname +
                " _ " + userName +
                " _ " + patronymic +
                " _ " + address +
                " _ " + this.getUserPhone() +
                " _ ";
    }

}
