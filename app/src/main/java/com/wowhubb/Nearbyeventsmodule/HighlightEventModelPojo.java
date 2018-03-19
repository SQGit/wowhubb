package com.wowhubb.Nearbyeventsmodule;

import android.graphics.drawable.Drawable;

/**
 * Created by Guna on 08-02-2018.
 */

public class HighlightEventModelPojo {

    public String guestWowTagId = "";
    public String guestName = "";
    public String guestInfo = "";
    public String guestSite ="";
    public String guestProfile ="";


    public HighlightEventModelPojo(String guestWowTagId, String guestName, String guestInfo, String guestSite, String  guestProfile) {
    this.guestWowTagId=guestWowTagId;
    this.guestName=guestName;
    this.guestInfo=guestInfo;
    this.guestSite=guestSite;
    this.guestProfile=guestProfile;
    }


    public String getguestWowTagId() {
        return guestWowTagId;
    }

    public void setguestWowTagId(String guestWowTagId) {
        this.guestWowTagId = guestWowTagId;
    }

    public String getguestName() {
        return guestName;
    }

    public void setguestName(String guestName) {
        this.guestName = guestName;
    }

    public String getguestInfo() {
        return guestInfo;
    }

    public void setguestInfo(String guestInfo) {
        this.guestInfo = guestInfo;
    }


    public String getguestSite() {
        return guestSite;
    }

    public void setguestSite(String guestSite) {
        this.guestSite = guestSite;
    }

    public String  getguestProfile() {
        return guestProfile;
    }

    public void setguestProfile(String guestProfile) {
        this.guestProfile = guestProfile;
    }
   }
