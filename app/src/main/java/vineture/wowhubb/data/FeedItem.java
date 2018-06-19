package vineture.wowhubb.data;

public class FeedItem {
    String eventid, pcoordinator, peventname, pftime, pttime, peventno, designation, pday, friendid, friendname, friendwowtagid, friendpic, friiendsstatus, receivedstatus;
    String comment, userfname,userimage,mutualfriends;
    private int id;
    private String fulladdress, name, status, image, profilePic, timeStamp, url, wowtagvideo, highlight1, highlight2, eventname, description, eventdate, coverimage;
    private String highlightvideo2, highlightvideo1,wowcount,commentscount;
    private String eventtopic, runtimefrom, runtimeto, giftregistryurl, donationurl, othersurl,timezone,category,eventstartdate,eventenddate;
String displaytime;
    public FeedItem()
    { }

    public String getMutualfriends() {
        return mutualfriends;
    }

    public void setMutualfriends(String mutualfriends) {
        this.mutualfriends = mutualfriends;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEventstartdate() {
        return eventstartdate;
    }

    public void setEventstartdate(String eventstartdate) {
        this.eventstartdate = eventstartdate;
    }

    public String getEventenddate() {
        return eventenddate;
    }

    public void setEventenddate(String eventenddate) {
        this.eventenddate = eventenddate;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCommentscount() {
        return commentscount;
    }

    public void setCommentscount(String commentscount) {
        this.commentscount = commentscount;
    }

    public String getWowcount() {
        return wowcount;
    }

    public void setWowcount(String wowcount) {
        this.wowcount = wowcount;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getComment() {
        return comment;
    }

    public String getDisplaytime() {
        return displaytime;
    }

    public void setDisplaytime(String displaytime) {
        this.displaytime = displaytime;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserfname() {
        return userfname;
    }

    public void setUserfname(String userfname) {
        this.userfname = userfname;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFriiendsstatus() {
        return friiendsstatus;
    }

    public void setFriiendsstatus(String friiendsstatus) {
        this.friiendsstatus = friiendsstatus;
    }

    public String getReceivedstatus() {
        return receivedstatus;
    }

    public void setReceivedstatus(String receivedstatus) {
        this.receivedstatus = receivedstatus;
    }

    public String getFriendpic() {
        return friendpic;
    }

    public void setFriendpic(String friendpic) {
        this.friendpic = friendpic;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    public String getFriendwowtagid() {
        return friendwowtagid;
    }

    public void setFriendwowtagid(String friendwowtagid) {
        this.friendwowtagid = friendwowtagid;
    }

    public String getPcoordinator() {
        return pcoordinator;
    }

    public void setPcoordinator(String pcoordinator) {
        this.pcoordinator = pcoordinator;
    }

    public String getPeventname() {
        return peventname;
    }

    public void setPeventname(String peventname) {
        this.peventname = peventname;
    }

    public String getPftime() {
        return pftime;
    }

    public void setPftime(String pftime) {
        this.pftime = pftime;
    }

    public String getPttime() {
        return pttime;
    }

    public void setPttime(String pttime) {
        this.pttime = pttime;
    }

    public String getPeventno() {
        return peventno;
    }

    public void setPeventno(String peventno) {
        this.peventno = peventno;
    }

    public String getPday() {
        return pday;
    }

    public void setPday(String pday) {
        this.pday = pday;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getFulladdress() {
        return fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    public String getEventdate() {
        return eventdate;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImge() {
        return image;
    }

    public void setImge(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWowtagvideo() {
        return wowtagvideo;
    }

    public void setWowtagvideo(String wowtagvideo) {
        this.wowtagvideo = wowtagvideo;
    }

    public String getHighlight1() {
        return highlight1;
    }

    public void setHighlight1(String highlight1) {
        this.highlight1 = highlight1;
    }


    public String getHighlight2() {
        return highlight2;
    }

    public void setHighlight2(String highlight2) {
        this.highlight2 = highlight2;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getCoverimage() {
        return coverimage;
    }

    public void setCoverimage(String coverimage) {
        this.coverimage = coverimage;
    }

    public String getHighlightvideo1() {
        return highlightvideo1;
    }

    public void setHighlightvideo1(String highlightvideo1) {
        this.highlightvideo1 = highlightvideo1;
    }

    public String getHighlightvideo2() {
        return highlightvideo2;
    }

    public void setHighlightvideo2(String highlightvideo2) {
        this.highlightvideo2 = highlightvideo2;
    }

    public String getRuntimefrom() {
        return runtimefrom;
    }

    public void setRuntimefrom(String runtimefrom) {
        this.runtimefrom = runtimefrom;
    }

    public String getRuntimeto() {
        return runtimeto;
    }

    public void setRuntimeto(String runtimeto) {
        this.runtimeto = runtimeto;
    }

    public String getEventtopic() {
        return eventtopic;
    }

    public void setEventtopic(String eventtopic) {
        this.eventtopic = eventtopic;
    }


    public String getDonationurl() {
        return donationurl;
    }

    public void setDonationurl(String donationurl) {
        this.donationurl = donationurl;
    }

    public String getGiftregistryurl() {
        return giftregistryurl;
    }

    public void setGiftregistryurl(String giftregistryurl) {
        this.giftregistryurl = giftregistryurl;
    }

    public String getOthersurl() {
        return othersurl;
    }

    public void setOthersurl(String othersurl) {
        this.othersurl = othersurl;
    }
}
