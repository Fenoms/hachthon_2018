package team_3.hackathon.backend.ticket;

public class Ticket {
    enum Status{OPEN, PROCESSING, CLOSED};
    int ticketID;
    String submiterName;
    int submiterID;
    String replierName;
    int replierID;
    Status status;
    String claim;
    String reply;

    public int getTicketID() {
        return ticketID;
    }

    public String getSubmiterName() {
        return submiterName;
    }

    public int getSubmiterID() {
        return submiterID;
    }

    public String getReplierName() {
        return replierName;
    }

    public int getReplierID() {
        return replierID;
    }

    public Status getStatus() {
        return status;
    }

    public String getClaim() {
        return claim;
    }

    public String getReply() {
        return reply;
    }


}
