package com.sayone.hush;

import java.util.Date;

public class CalendarEvent implements Comparable<CalendarEvent>{
    
    private String title;
    private Date begin, end;
    private String id;
    
    public CalendarEvent() {
            
    }
    
    public CalendarEvent(String title, Date begin, Date end, String allDay) {
            setTitle(title);
            setBegin(begin);
            setEnd(end);
            setId(allDay);
    }

    public String getTitle() {
            return title;
    }

    public void setTitle(String title) {
            this.title = title;
    }

    public Date getBegin() {
            return begin;
    }

    public void setBegin(Date begin) {
            this.begin = begin;
    }

    public Date getEnd() {
            return end;
    }

    public void setEnd(Date end) {
            this.end = end;
    }

    public String getId() {
            return id;
    }

    public void setId(String _id) {
            this.id = _id;
    }
    
    @Override
    public String toString(){
            return getTitle() + " " + getBegin() + " " + getEnd() + " " + getId();
    }

    @Override
    public int compareTo(CalendarEvent other) {
            // -1 = less, 0 = equal, 1 = greater
            return getBegin().compareTo(other.begin);
    }

}