package team_3.hackathon.backend.database;

public class CacheInfo {
    int userID;
    String status;

    public CacheInfo(int userID, String status) {
        this.userID = userID;
        this.status = status;
    }

    public int getUserID() {
        return userID;
    }

    public String getStatus() {
        return status;
    }
}
