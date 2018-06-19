package vineture.wowhubb.app;

import java.util.ArrayList;
import java.util.List;

import vineture.wowhubb.Profile.Certification;
import vineture.wowhubb.Profile.College;
import vineture.wowhubb.Profile.Workexperience;

/**
 * Created by Salman on 11-06-2018.
 */

public class ProfileModel {

    String skills, certication, certificationYear, college, from, to, title, company, location, description;
    List<Certification> certificationList;
    List<String> professionalSkills;
    List<Workexperience> workexperienceList;
    List<College> collegeList;

    public ProfileModel(String name) {
        this.skills = name;
    }

    public ProfileModel(List<Certification> certificationList) {
        this.certificationList = certificationList;
    }

    public ProfileModel(String name, String year) {
        this.certication = name;
        this.certificationYear = year;
    }
    public ProfileModel(List<String> professionalSkills,float d)

    {
        this.professionalSkills = professionalSkills;
    }
    public ProfileModel(String college, String from, String to) {
        this.college = college;
        this.from = from;
        this.to = to;
    }

    public ProfileModel(String title, String company, String location,String from,String to,String description) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.from = from;
        this.description=description;
        this.to=to;
    }

    public ProfileModel(List<Workexperience> certificationList, String s) {
        this.workexperienceList = workexperienceList;
    }

    public ProfileModel(List<College> collegeList, Integer i) {
        this.collegeList = collegeList;
    }



    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCertication() {
        return certication;
    }

    public void setCertication(String certication) {
        this.certication = certication;
    }

    public String getCertificationYear() {
        return certificationYear;
    }

    public void setCertificationYear(String certificationYear) {
        this.certificationYear = certificationYear;
    }

    public List<College> getCollegeList() {
        return collegeList;
    }

    public void setCollegeList(List<College> collegeList) {
        this.collegeList = collegeList;
    }

    public List<Certification> getCertificationList() {
        return certificationList;
    }

    public void setCertificationList(List<Certification> certificationList) {
        this.certificationList = certificationList;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
