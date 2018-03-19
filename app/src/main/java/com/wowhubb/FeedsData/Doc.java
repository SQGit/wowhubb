package com.wowhubb.FeedsData;

/**
 * Created by Salman on 08-02-2018.
 */


import java.util.List;

public class Doc {

    private String _id;
    private Userid userid;
    private String eventtype;
    private int eventtypeint;
    private int eventdayscount;
    private String eventcategory;
    private String eventname;
    private String eventtimezone;
    private String eventstartdate;
    private String eventenddate;
    private String eventdescription;
    private String coverpage;
    private String createdAt;
    private String urllink;
    private String thoughtsimage;
    private String thoughtsvideo;
    private String thoughtstext;
    private List<Comment> comments = null;
    private List<Object> rsvp = null;
    private List<Wowsome> wowsome = null;
    private Audeinceengagementurl audeinceengagementurl;
    private List<Programschedule> programschedule = null;
    private List<Object> eventfaqs = null;
    private List<Eventvenue> eventvenue = null;
    private List<Object> physicalsalesstorelocation = null;
    private Integer v;

    private String wowtagvideo;
    private String runtimeto;
    private String runtimefrom;
    private Object eventcity;
    private String eventtitle;
    private String faqanswer4;
    private String faqquestion4;
    private String faqanswer3;
    private String faqquestion3;
    private String faqanswer2;
    private String faqquestion2;
    private String faqanswer1;
    private String faqquestion1;
    private Object inviteonlyevent;
    private Object onlineevent;
    private Object eventvenueguestshare;
    private Object eventvenueaddressvisibility;
    private String eventhighlightsvideo2;
    private String eventhighlightsvideo1;
    private String eventhighlights2;
    private String eventhighlights1;
    private String eventspeakeractivities2;
    private String eventspeakeractivities1;
    private String eventspeakerlink2;
    private String eventspeakerlink1;
    private String eventspeakername2;
    private String eventspeakername1;
    private String eventguesttype2;
    private String eventguesttype1;
    private String eventnolinks;
    private String donationsurl;
    private String giftregistryurl;
    private String otherurl;
    private String registerrsvp;
    private Object privateevent;
    private String eventcontactmessage;
    private String eventcontactemail;
    private String eventcontactphone;
    private String eventcontactname;
    private Integer wowsomecount;
    private Integer commentcount;
    //private String keywordsearch;
    private String organisationname;
    private String engagementformaction;
    private String engagementformtype;
    private Audienceengagementform audienceengagementform;

    public String getThoughtstext() {
        return thoughtstext;
    }

    public void setThoughtstext(String thoughtstext) {
        this.thoughtstext = thoughtstext;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getThoughtsimage() {
        return thoughtsimage;
    }

    public void setThoughtsimage(String thoughtsimage) {
        this.thoughtsimage = thoughtsimage;
    }

    public String getThoughtsvideo() {
        return thoughtsvideo;
    }

    public void setThoughtsvideo(String thoughtsvideo) {
        this.thoughtsvideo = thoughtsvideo;
    }

    public Userid getUserid() {
        return userid;
    }

    public void setUserid(Userid userid) {
        this.userid = userid;
    }

    public String getEventtype() {
        return eventtype;
    }

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype;
    }

    public int getEventtypeint() {
        return eventtypeint;
    }

    public void setEventtypeint(int eventtypeint) {
        this.eventtypeint = eventtypeint;
    }

    public int getEventdayscount() {
        return eventdayscount;
    }

    public void setEventdayscount(int eventdayscount) {
        this.eventdayscount = eventdayscount;
    }

    public String getEventcategory() {
        return eventcategory;
    }

    public void setEventcategory(String eventcategory) {
        this.eventcategory = eventcategory;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventtimezone() {
        return eventtimezone;
    }

    public void setEventtimezone(String eventtimezone) {
        this.eventtimezone = eventtimezone;
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

    public String getEventdescription() {
        return eventdescription;
    }

    public void setEventdescription(String eventdescription) {
        this.eventdescription = eventdescription;
    }

    public String getCoverpage() {
        return coverpage;
    }

    public void setCoverpage(String coverpage) {
        this.coverpage = coverpage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Object> getRsvp() {
        return rsvp;
    }

    public void setRsvp(List<Object> rsvp) {
        this.rsvp = rsvp;
    }

    public List<Wowsome> getWowsome() {
        return wowsome;
    }

    public void setWowsome(List<Wowsome> wowsome) {
        this.wowsome = wowsome;
    }

    public Audeinceengagementurl getAudeinceengagementurl() {
        return audeinceengagementurl;
    }

    public void setAudeinceengagementurl(Audeinceengagementurl audeinceengagementurl) {
        this.audeinceengagementurl = audeinceengagementurl;
    }

    public List<Programschedule> getProgramschedule() {
        return programschedule;
    }

    public void setProgramschedule(List<Programschedule> programschedule) {
        this.programschedule = programschedule;
    }

    public List<Object> getEventfaqs() {
        return eventfaqs;
    }

    public void setEventfaqs(List<Object> eventfaqs) {
        this.eventfaqs = eventfaqs;
    }

    public List<Eventvenue> getEventvenue() {
        return eventvenue;
    }

    public void setEventvenue(List<Eventvenue> eventvenue) {
        this.eventvenue = eventvenue;
    }

    public List<Object> getPhysicalsalesstorelocation() {
        return physicalsalesstorelocation;
    }

    public void setPhysicalsalesstorelocation(List<Object> physicalsalesstorelocation) {
        this.physicalsalesstorelocation = physicalsalesstorelocation;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getWowtagvideo() {
        return wowtagvideo;
    }

    public void setWowtagvideo(String wowtagvideo) {
        this.wowtagvideo = wowtagvideo;
    }

    public String getRuntimeto() {
        return runtimeto;
    }

    public void setRuntimeto(String runtimeto) {
        this.runtimeto = runtimeto;
    }

    public String getRuntimefrom() {
        return runtimefrom;
    }

    public void setRuntimefrom(String runtimefrom) {
        this.runtimefrom = runtimefrom;
    }

    public Object getEventcity() {
        return eventcity;
    }

    public void setEventcity(Object eventcity) {
        this.eventcity = eventcity;
    }

    public String getEventtitle() {
        return eventtitle;
    }

    public void setEventtitle(String eventtitle) {
        this.eventtitle = eventtitle;
    }

    public String getFaqanswer4() {
        return faqanswer4;
    }

    public void setFaqanswer4(String faqanswer4) {
        this.faqanswer4 = faqanswer4;
    }

    public String getFaqquestion4() {
        return faqquestion4;
    }

    public void setFaqquestion4(String faqquestion4) {
        this.faqquestion4 = faqquestion4;
    }

    public String getFaqanswer3() {
        return faqanswer3;
    }

    public void setFaqanswer3(String faqanswer3) {
        this.faqanswer3 = faqanswer3;
    }

    public String getFaqquestion3() {
        return faqquestion3;
    }

    public void setFaqquestion3(String faqquestion3) {
        this.faqquestion3 = faqquestion3;
    }

    public String getFaqanswer2() {
        return faqanswer2;
    }

    public void setFaqanswer2(String faqanswer2) {
        this.faqanswer2 = faqanswer2;
    }

    public String getFaqquestion2() {
        return faqquestion2;
    }

    public void setFaqquestion2(String faqquestion2) {
        this.faqquestion2 = faqquestion2;
    }

    public String getFaqanswer1() {
        return faqanswer1;
    }

    public void setFaqanswer1(String faqanswer1) {
        this.faqanswer1 = faqanswer1;
    }

    public String getFaqquestion1() {
        return faqquestion1;
    }

    public void setFaqquestion1(String faqquestion1) {
        this.faqquestion1 = faqquestion1;
    }

    public Object getInviteonlyevent() {
        return inviteonlyevent;
    }

    public void setInviteonlyevent(Object inviteonlyevent) {
        this.inviteonlyevent = inviteonlyevent;
    }

    public Object getOnlineevent() {
        return onlineevent;
    }

    public void setOnlineevent(Object onlineevent) {
        this.onlineevent = onlineevent;
    }

    public Object getEventvenueguestshare() {
        return eventvenueguestshare;
    }

    public void setEventvenueguestshare(Object eventvenueguestshare) {
        this.eventvenueguestshare = eventvenueguestshare;
    }

    public Object getEventvenueaddressvisibility() {
        return eventvenueaddressvisibility;
    }

    public void setEventvenueaddressvisibility(Object eventvenueaddressvisibility) {
        this.eventvenueaddressvisibility = eventvenueaddressvisibility;
    }

    public String getEventhighlightsvideo2() {
        return eventhighlightsvideo2;
    }

    public void setEventhighlightsvideo2(String eventhighlightsvideo2) {
        this.eventhighlightsvideo2 = eventhighlightsvideo2;
    }

    public String getEventhighlightsvideo1() {
        return eventhighlightsvideo1;
    }

    public void setEventhighlightsvideo1(String eventhighlightsvideo1) {
        this.eventhighlightsvideo1 = eventhighlightsvideo1;
    }

    public String getEventhighlights2() {
        return eventhighlights2;
    }

    public void setEventhighlights2(String eventhighlights2) {
        this.eventhighlights2 = eventhighlights2;
    }

    public String getEventhighlights1() {
        return eventhighlights1;
    }

    public void setEventhighlights1(String eventhighlights1) {
        this.eventhighlights1 = eventhighlights1;
    }

    public String getEventspeakeractivities2() {
        return eventspeakeractivities2;
    }

    public void setEventspeakeractivities2(String eventspeakeractivities2) {
        this.eventspeakeractivities2 = eventspeakeractivities2;
    }

    public String getEventspeakeractivities1() {
        return eventspeakeractivities1;
    }

    public void setEventspeakeractivities1(String eventspeakeractivities1) {
        this.eventspeakeractivities1 = eventspeakeractivities1;
    }

    public String getEventspeakerlink2() {
        return eventspeakerlink2;
    }

    public void setEventspeakerlink2(String eventspeakerlink2) {
        this.eventspeakerlink2 = eventspeakerlink2;
    }

    public String getEventspeakerlink1() {
        return eventspeakerlink1;
    }

    public void setEventspeakerlink1(String eventspeakerlink1) {
        this.eventspeakerlink1 = eventspeakerlink1;
    }

    public Object getEventspeakername2() {
        return eventspeakername2;
    }

    public void setEventspeakername2(String eventspeakername2) {
        this.eventspeakername2 = eventspeakername2;
    }

    public String getEventspeakername1() {
        return eventspeakername1;
    }

    public void setEventspeakername1(String eventspeakername1) {
        this.eventspeakername1 = eventspeakername1;
    }

    public String getEventguesttype2() {
        return eventguesttype2;
    }

    public void setEventguesttype2(String eventguesttype2) {
        this.eventguesttype2 = eventguesttype2;
    }

    public String getEventguesttype1() {
        return eventguesttype1;
    }

    public void setEventguesttype1(String eventguesttype1) {
        this.eventguesttype1 = eventguesttype1;
    }

    public String getEventnolinks() {
        return eventnolinks;
    }

    public void setEventnolinks(String eventnolinks) {
        this.eventnolinks = eventnolinks;
    }

    public String getDonationsurl() {
        return donationsurl;
    }

    public void setDonationsurl(String donationsurl) {
        this.donationsurl = donationsurl;
    }

    public String getGiftregistryurl() {
        return giftregistryurl;
    }

    public void setGiftregistryurl(String giftregistryurl) {
        this.giftregistryurl = giftregistryurl;
    }

    public String getOtherurl() {
        return otherurl;
    }

    public void setOtherurl(String otherurl) {
        this.otherurl = otherurl;
    }

    public String getRegisterrsvp() {
        return registerrsvp;
    }

    public void setRegisterrsvp(String registerrsvp) {
        this.registerrsvp = registerrsvp;
    }

    public Object getPrivateevent() {
        return privateevent;
    }

    public void setPrivateevent(Object privateevent) {
        this.privateevent = privateevent;
    }

    public String getEventcontactmessage() {
        return eventcontactmessage;
    }

    public void setEventcontactmessage(String eventcontactmessage) {
        this.eventcontactmessage = eventcontactmessage;
    }

    public String getEventcontactemail() {
        return eventcontactemail;
    }

    public void setEventcontactemail(String eventcontactemail) {
        this.eventcontactemail = eventcontactemail;
    }

    public String getEventcontactphone() {
        return eventcontactphone;
    }

    public void setEventcontactphone(String eventcontactphone) {
        this.eventcontactphone = eventcontactphone;
    }

    public String getEventcontactname() {
        return eventcontactname;
    }

    public void setEventcontactname(String eventcontactname) {
        this.eventcontactname = eventcontactname;
    }

    public Integer getWowsomecount() {
        return wowsomecount;
    }

    public void setWowsomecount(Integer wowsomecount) {
        this.wowsomecount = wowsomecount;
    }

    public Integer getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(Integer commentcount) {
        this.commentcount = commentcount;
    }

   /* public String getKeywordsearch() {
        return keywordsearch;
    }

    public void setKeywordsearch(String keywordsearch) {
        this.keywordsearch = keywordsearch;
    }*/

    public String getOrganisationname() {
        return organisationname;
    }

    public void setOrganisationname(String organisationname) {
        this.organisationname = organisationname;
    }

    public String getEngagementformaction() {
        return engagementformaction;
    }

    public void setEngagementformaction(String engagementformaction) {
        this.engagementformaction = engagementformaction;
    }

    public String getEngagementformtype() {
        return engagementformtype;
    }

    public void setEngagementformtype(String engagementformtype) {
        this.engagementformtype = engagementformtype;
    }

    public Audienceengagementform getAudienceengagementform() {
        return audienceengagementform;
    }

    public void setAudienceengagementform(Audienceengagementform audienceengagementform) {
        this.audienceengagementform = audienceengagementform;
    }

    public String getUrllink() {
        return urllink;
    }

    public void setUrllink(String urllink) {
        this.urllink = urllink;
    }
}