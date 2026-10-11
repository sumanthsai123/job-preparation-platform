package com.jobprep.job_preparation_platform.dto;

public class JobResponseDTO {

    private Integer id;
    private String title;
    private String company;
    private String location;

    public JobResponseDTO() {
    }

    public JobResponseDTO(
            Integer id,
            String title,
            String company,
            String location) {

        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}