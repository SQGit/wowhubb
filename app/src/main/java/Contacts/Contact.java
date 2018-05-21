package Contacts;

import java.util.ArrayList;

public class Contact {
    public String id;
    public String name;
    public String phoneno;
    public String wowtagid;
    public ArrayList<ContactEmail> emails;
    public ArrayList<ContactPhone> numbers;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Contact(String id, String name) {
        this.id = id;
        this.name = name;
        this.emails = new ArrayList<ContactEmail>();
        this.numbers = new ArrayList<ContactPhone>();
    }

    public Contact()
    {}

    public Contact(String id, String name, String phoneno) {
        this.phoneno = phoneno;
        this.id = id;
        this.name = name;
    }
    public Contact(String id, String name, String phoneno,String wowtag) {
        this.phoneno = phoneno;
        this.id = id;
        this.name = name;
        this.wowtagid=wowtag;
    }
    @Override
    public String toString() {
        String result = name;
        if (numbers.size() > 0) {
            ContactPhone number = numbers.get(0);
            result += " (" + number.number + " - " + number.type + ")";
        }
        if (emails.size() > 0) {
            ContactEmail email = emails.get(0);
            result += " [" + email.address + " - " + email.type + "]";
        }
        return result;
    }

    public void addEmail(String address, String type) {
        emails.add(new ContactEmail(address, type));
    }

    public void addNumber(String number, String type) {
        numbers.add(new ContactPhone(number, type));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getWowtagid() {
        return wowtagid;
    }

    public void setWowtagid(String wowtagid) {
        this.wowtagid = wowtagid;
    }
}
