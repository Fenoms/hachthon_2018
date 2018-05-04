package team_3.hackathon.backend.userdb;

public class UserDBEntry {
    public enum Type{WORKER, CUSTOMER};
    private int userID;
    private String userName;
    private Type type;

    public UserDBEntry(){}

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
