package com.khurshid.kamkorapartner.model;


import com.google.gson.annotations.SerializedName;

public class Partner {
    @SerializedName("_id")
    private String id;
    private String name;
    private String age;
    private String phone;
    private String email;
    private String profession;
    private String avatar;
    private String password_digest;
    private Boolean isAccountVerified;
    private String address;
    private String city;
    private String updatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword_digest() {
        return password_digest;
    }

    public void setPassword_digest(String password_digest) {
        this.password_digest = password_digest;
    }

    public Boolean getAccountVerified() {
        return isAccountVerified;
    }

    public void setAccountVerified(Boolean accountVerified) {
        isAccountVerified = accountVerified;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
