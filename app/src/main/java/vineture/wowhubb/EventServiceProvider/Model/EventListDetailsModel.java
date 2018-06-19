package vineture.wowhubb.EventServiceProvider.Model;

/**
 * Created by Dell on 11/9/2017.
 */

public class EventListDetailsModel {

    public String shopName = "";
    public String shopRating = "";
    public String shopAddress = "";
    public String shopCountry="";
    public String shopLink ="";
    public String shopLogo ="";
    public String shopContact="";
    public String shopLocation="";
    public String shopCategory="";
    public String shopOpenTime="";
    public double shopLatitude;
    public double shopLongitude;
    public String shopDiscount;




    String newFlag = "old";

    public String getshopName() {
        return shopName;
    }

    public void setshopName(String shopName) {
        this.shopName = shopName;
    }

    public String getshopCountry() {
        return shopCountry;
    }

    public void setshopCountry(String shopCountry) {
        this.shopCountry = shopCountry;
    }

    public String getshopRating() {
        return shopRating;
    }

    public void setshopRating(String shopRating) {
        this.shopRating = shopRating;
    }

    public String getshopAddress() {
        return shopAddress;
    }

    public void setshopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }


    public String getshopContact() {
        return shopContact;
    }

    public void setshopContact(String shopContact) {this.shopContact = shopContact; }




    public String getshopLogo() {
        return shopLogo;
    }

    public void setshopLogo(String shopLogo) {this.shopLogo = shopLogo;}



    public String getshopLocation() {
        return shopLocation;
    }

    public void setshopLocation(String shopLocation) {this.shopLocation = shopLocation;}



    public String getshopLink() {
        return shopLink;
    }

    public void setshopLink(String shopLink) {this.shopLink = shopLink;}


    public String getshopCategory() {
        return shopCategory;
    }

    public void setshopCategory(String shopCategory) {this.shopCategory = shopCategory;}



    public String getshopOpenTime() {
        return shopOpenTime;
    }

    public void setshopOpenTime(String shopOpenTime) {this.shopOpenTime = shopOpenTime;}


    public double getshopLatitude() {
        return shopLatitude;
    }

    public void setshopLatitude(double shopLatitude) {this.shopLatitude = shopLatitude;}


    public double getshopLongitude() {
        return shopLongitude;
    }

    public void setshopLongitude(double shopLongitude) {this.shopLongitude = shopLongitude;}


}
