
package com.wowhubb.Groups;

import java.util.List;

public class Group {

    private String _id;
    private String firstname;
    private Adminid adminid;
    private String wowtagid;
    private String lastname;
    private String groupname;
    private String createdAt;
    private String groupcount;
    private String privacy;
    private Integer v;
    private List<User> users = null;

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getWowtagid() {
        return wowtagid;
    }

    public void setWowtagid(String wowtagid) {
        this.wowtagid = wowtagid;
    }

    public String getGroupcount() {
        return groupcount;
    }

    public void setGroupcount(String groupcount) {
        this.groupcount = groupcount;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public Adminid getAdminid() {
        return adminid;
    }

    public void setAdminid(Adminid adminid) {
        this.adminid = adminid;
    }
    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
