package team_3.hackathon.backend.orderer;

public class Proposal {
    //int version;
    int userID;
    String cmd;

    Proposal(int userID, String cmd){
        //this.version = version;
        this.userID = userID;
        this.cmd = cmd;
    }

    public int getUserID() {
        return userID;
    }

    public String getCmd() {
        return cmd;
    }
}
