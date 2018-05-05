package team_3.hackathon.backend.orderer;
import java.util.Optional;

import java.util.Optional;

public class Proposal {
    Optional<String> reply;
    int userID;
    String cmd;
    Optional<String> reply;

    Proposal(int userID, String cmd, Optional<String> reply){
        //this.version = version;
        this.userID = userID;
        this.cmd = cmd;
<<<<<<< Updated upstream
        this.reply = Optional.ofNullable(null);
    }

    Proposal(int userID, String cmd, Optional<String> reply){
        //this.version = version;
        this.userID = userID;
        this.cmd = cmd;
=======
>>>>>>> Stashed changes
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
