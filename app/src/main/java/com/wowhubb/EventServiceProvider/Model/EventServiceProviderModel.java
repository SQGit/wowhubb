package com.wowhubb.EventServiceProvider.Model;

/**
 * Created by Dell on 11/9/2017.
 */

public class EventServiceProviderModel {

    public String providerId = "";
    public String providerName = "";
    public String providerLogo = "";
    public String providerCategory ="";

    String newFlag = "old";

    public String getproviderId() {
        return providerId;
    }

    public void setproviderId(String jobId) {
        this.providerId = providerId;
    }

    public String getproviderName() {
        return providerName;
    }

    public void setproviderName(String jobTitle) {
        this.providerName = providerName;
    }

    public String getproviderLogo() {
        return providerLogo;
    }

    public void setproviderLogo(String jobLocation) {
        this.providerLogo = providerLogo;
    }


    public String getproviderCategory() {
        return providerCategory;
    }

    public void setproviderCategory(String providerCategory) {
        this.providerCategory = providerCategory;
    }


}
