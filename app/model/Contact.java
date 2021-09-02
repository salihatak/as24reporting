package model;

import java.time.LocalDateTime;
import java.util.Date;

public class Contact {

    private Long id;
    private Date contactDate;

    public Contact(Long id, Date contactDate) {
        this.id = id;
        this.contactDate = contactDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }
}
