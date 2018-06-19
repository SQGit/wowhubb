
package vineture.wowhubb.Groups;


public class Userid {

    private String _id;
    private String firstname;
    private String lastname;
    private String wowtagid;
    private String personalimage;
    private String designation;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
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

    public String getPersonalimage() {
        return personalimage;
    }

    public void setPersonalimage(String personalimage) {
        this.personalimage = personalimage;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}
