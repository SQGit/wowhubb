package vineture.wowhubb.wowtag.Model;

/**
 * Created by Guna on 08-02-2018.
 */

public class WowtagModel {

    public String wowtagId = "";
    public String wowtagName = "";
    public String wowtagVenue = "";
    public String wowtagAddress = "";
    public String wowtagEventStartDate = "";
    public String wowtagEventEndDate = "";
    public String wowtagEventStartTime = "";
    public String wowtagEventEndTime = "";
    public String wowtagDescription = "";
    public String wowtagStartDate = "";
    public String wowtagEndDate = "";
    public String wowtagStartTime = "";
    public String wowtagEndTime = "";
    public String wowtagShares = "";
    public String wowtagAttending = "";
    public String wowtagViews = "";
    public String wowtagVideo="";


    public WowtagModel(String wowtagId,String wowtagName,String wowtagVenue,String wowtagAddress,String wowtagEventStartDate,String wowtagEventEndDate,String wowtagEventStartTime,String wowtagEventEndTime
    ,String wowtagDescription,String wowtagStartDate,String wowtagEndDate,String wowtagStartTime,String wowtagEndTime,String wowtagShares,String wowtagAttending,String wowtagViews,String wowtagVideo) {
        this.wowtagId = wowtagId;
        this.wowtagName = wowtagName;
        this.wowtagVenue = wowtagVenue;
        this.wowtagAddress = wowtagAddress;
        this.wowtagEventStartDate = wowtagEventStartDate;
        this.wowtagEventEndDate = wowtagEventEndDate;
        this.wowtagEventStartTime = wowtagEventStartTime;
        this.wowtagEventEndTime = wowtagEventEndTime;
        this.wowtagDescription = wowtagDescription;
        this.wowtagStartDate = wowtagStartDate;
        this.wowtagEndDate = wowtagEndDate;
        this.wowtagStartTime = wowtagStartTime;
        this.wowtagEndTime = wowtagEndTime;
        this.wowtagShares = wowtagShares;
        this.wowtagAttending = wowtagAttending;
        this.wowtagViews = wowtagViews;
        this.wowtagVideo = wowtagVideo;

    }

    public WowtagModel() {

    }
}
