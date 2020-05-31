package com.wequan.bu.repository.model;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Zhaochao Huang
 */
public class Professor {
    private Integer id;
    private String firstName;
    private String lastName;
    private School school;
    private Double overallScore;
    private LocalDate createTime;
    private Integer createBy;
    private Department department;
    private List<Course> courses;
    private List<CourseRate> courseRates;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

       public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department departmentId) {
        this.department = departmentId;
    }

    public List<CourseRate> getCourseRates() {
        return courseRates;
    }

    public void setCourseRates(List<CourseRate> courseRates) {
        this.courseRates = courseRates;
    }
}
