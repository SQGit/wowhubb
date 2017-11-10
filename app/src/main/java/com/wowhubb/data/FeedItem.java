package com.wowhubb.data;

public class FeedItem {
    private int id;
    private String fulladdress, name, status, image, profilePic, timeStamp, url, wowtagvideo, highlight1, highlight2, eventname, description, eventdate, coverimage;

    private String highlightvideo2, highlightvideo1;

    public FeedItem() {
    }

    public FeedItem(int id, String name, String image, String status,
                    String profilePic, String timeStamp, String url) {
        super();
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.profilePic = profilePic;
        this.timeStamp = timeStamp;
        this.url = url;
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
}
