package vineture.wowhubb.FeedsData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Salman on 08-02-2018.
 */

public class Programschedule implements Parcelable  {

    private String ityendtime;
    private String itystarttime;
    private String agenda;
    private String location;
    private String facilitator;
    private String endtime;
    private String starttime;
    private String eventnumber;
    private String day;
    private String id;

    protected Programschedule(Parcel in) {
        ityendtime = in.readString();
        itystarttime = in.readString();
        agenda = in.readString();
        location = in.readString();
        facilitator = in.readString();
        endtime = in.readString();
        starttime = in.readString();
        eventnumber = in.readString();
        day = in.readString();
        id = in.readString();
    }


    public static final Creator<Programschedule> CREATOR = new Creator<Programschedule>() {
        @Override
        public Programschedule createFromParcel(Parcel in) {
            return new Programschedule(in);
        }

        @Override
        public Programschedule[] newArray(int size) {
            return new Programschedule[size];
        }
    };

    public String getItyendtime() {
        return ityendtime;
    }

    public void setItyendtime(String ityendtime) {
        this.ityendtime = ityendtime;
    }

    public String getItystarttime() {
        return itystarttime;
    }

    public void setItystarttime(String itystarttime) {
        this.itystarttime = itystarttime;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFacilitator() {
        return facilitator;
    }

    public void setFacilitator(String facilitator) {
        this.facilitator = facilitator;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEventnumber() {
        return eventnumber;
    }

    public void setEventnumber(String eventnumber) {
        this.eventnumber = eventnumber;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(ityendtime);
        parcel.writeString(itystarttime);
        parcel.writeString(agenda);
        parcel.writeString(location);
        parcel.writeString(facilitator);
        parcel.writeString(endtime);
        parcel.writeString(starttime);
        parcel.writeString(eventnumber);
        parcel.writeString(day);
        parcel.writeString(id);
    }
}
