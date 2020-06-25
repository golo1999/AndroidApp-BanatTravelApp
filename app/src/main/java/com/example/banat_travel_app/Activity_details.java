package com.example.banat_travel_app;

import android.os.Parcel;
import android.os.Parcelable;

public class Activity_details implements Parcelable // clasa creata de mine (implementeaza Parcelable pentru ca obiectul sau sa poata fi trimis intre activitati)
{
    private String Location, From, To, Price;
    private Integer Duration, Persons, TotalPrice;

    public Activity_details(String location, String from, String to, String price, Integer duration, Integer persons)
    {
        Location=location;
        From=from;
        To=to;
        Price=price;
        Duration=duration;
        Persons=persons;
        TotalPrice=Integer.parseInt(price)*persons;
    }

    protected Activity_details(Parcel in)
    {
        Location = in.readString();
        From = in.readString();
        To = in.readString();
        Price = in.readString();

        if (in.readByte() == 0)
            Duration = null;
        else Duration = in.readInt();

        if (in.readByte() == 0)
            Persons = null;
        else Persons = in.readInt();

        if (in.readByte() == 0)
            TotalPrice = null;
        else TotalPrice = in.readInt();
    }

    public static final Creator<Activity_details> CREATOR = new Creator<Activity_details>()
    {
        @Override
        public Activity_details createFromParcel(Parcel in)
        {
            return new Activity_details(in);
        }

        @Override
        public Activity_details[] newArray(int size)
        {
            return new Activity_details[size];
        }
    };

    public String getLocation()
    {
        return Location;
    }

    public void setLocation(String location)
    {
        Location = location;
    }

    public String getFrom()
    {
        return From;
    }

    public void setFrom(String from)
    {
        From = from;
    }

    public String getTo()
    {
        return To;
    }

    public void setTo(String to)
    {
        To = to;
    }

    public String getPrice()
    {
        return Price;
    }

    public void setPrice(String price)
    {
        Price = price;
    }

    public Integer getDuration()
    {
        return Duration;
    }

    public void setDuration(Integer duration)
    {
        Duration = duration;
    }

    public Integer getPersons()
    {
        return Persons;
    }

    public void setPersons(Integer persons)
    {
        Persons = persons;
    }

    public Integer getTotalPrice()
    {
        return TotalPrice;
    }

    public void setTotalPrice()
    {
        TotalPrice = Integer.parseInt(this.Price)*this.Persons;
    }

    public Activity_details()
    {

    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(Location);
        dest.writeString(From);
        dest.writeString(To);
        dest.writeString(Price);
        if (Duration == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Duration);
        }
        if (Persons == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Persons);
        }
        if (TotalPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(TotalPrice);
        }
    }
}
