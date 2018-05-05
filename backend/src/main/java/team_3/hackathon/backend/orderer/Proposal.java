package team_3.hackathon.backend.orderer;
import java.util.Optional;

import java.util.Optional;

public class Proposal {
    Optional<String> reply;
    int userID;
    String cmd;


    Proposal(int userID, String cmd, Optional<String> reply){
        //this.version = version;
        this.userID = userID;
        this.cmd = cmd;
        this.reply = reply;
    }

    public int getUserID() {
        return userID;
    }

    public String getCmd() {
        return cmd;
    }

    public Optional<String> getReply() {
        return reply;
    }
}
