package team_3.hackathon.backend;

import team_3.hackathon.backend.database.Database;

public class StartDB {
    public static void main(String[] args) {
        Database db = new Database(Const.OrdererURL, Const.OrdererHostname, Const.DBPort);

        db.start();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        db.stop();
    }
}
