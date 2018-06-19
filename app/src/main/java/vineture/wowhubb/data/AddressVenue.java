package vineture.wowhubb.data;

/**
 * Created by Salman on 23-12-2017.
 */

public class AddressVenue
{
    String venuname,address,city,state,zipcode;
    int id;

    public AddressVenue(String address, String city, String state, String zipcode)
    {
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public void setVenuname(String venuname) {
        this.venuname = venuname;
    }

    public String getVenuname() {
        return venuname;
    }

    public AddressVenue()
    { }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
