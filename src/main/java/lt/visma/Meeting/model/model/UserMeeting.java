package lt.visma.Meeting.model.model;

public class UserMeeting {
    User user;
    Meeting meeting;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public UserMeeting(User user, Meeting meeting) {
        this.user = user;
        this.meeting = meeting;
    }

    public UserMeeting() {
    }
}