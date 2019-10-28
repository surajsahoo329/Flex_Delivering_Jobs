package com.example.flex;

public class Company {

    private int imageId;
    private String company;
    private String timings;
    private String address;

    public Company(int imageId, String company, String timings, String address) {
        this.imageId = imageId;
        this.company = company;
        this.timings = timings;
        this.address = address;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getTimings()
    {
        return timings;
    }

    public void setTimings(String timings)
    {
        this.timings = timings;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    @Override
    public String toString() {
        return company+ "\n"+timings+"\n" + address;
    }
}
