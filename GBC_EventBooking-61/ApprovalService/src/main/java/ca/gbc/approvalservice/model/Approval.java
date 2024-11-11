package ca.gbc.approvalservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventId;
    private String staffId;
    private boolean approved;

    public Approval() {}

    public Approval(String eventId, String staffId, boolean approved) {
        this.eventId = eventId;
        this.staffId = staffId;
        this.approved = approved;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
}
