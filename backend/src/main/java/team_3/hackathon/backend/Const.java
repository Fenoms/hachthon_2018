package team_3.hackathon.backend;

public final class Const {
    public final static String OrdererHostname = "192.168.137.19";
    public final static String DBHostName = "192.168.137.30";
    public final static int DBPort = 12346;
    public final static int OrdererPort = 12345;

    public final static String DBURL = "http://" + DBHostName + ":" + DBPort;
    public final static String OrdererURL = "http://" + OrdererHostname + ":" + OrdererPort;
}
