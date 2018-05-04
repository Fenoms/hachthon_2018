package team_3.hackathon.backend.userdb;

import java.util.HashMap;

public class UserDB {
    private HashMap<Integer, UserDBEntry> DB;

    public UserDB(){
        DB = new HashMap<>();
        UserDBEntry worker1 = new UserDBEntry();
        worker1.setType(UserDBEntry.Type.WORKER);
        worker1.setUserID(1);
        worker1.setUserName("worker1");

        DB.put(worker1.getUserID(), worker1);

        UserDBEntry worker2 = new UserDBEntry();
        worker1.setType(UserDBEntry.Type.WORKER);
        worker1.setUserID(2);
        worker1.setUserName("worker2");

        DB.put(worker2.getUserID(), worker2);


        UserDBEntry customer1 = new UserDBEntry();
        worker1.setType(UserDBEntry.Type.CUSTOMER);
        worker1.setUserID(3);
        worker1.setUserName("customer1");

        DB.put(customer1.getUserID(), customer1);


        UserDBEntry customer2 = new UserDBEntry();
        worker1.setType(UserDBEntry.Type.CUSTOMER);
        worker1.setUserID(4);
        worker1.setUserName("customer2");

        DB.put(customer2.getUserID(), customer2);
    }

    public UserDBEntry getUserInfo(int userID) {
        return DB.get(userID);
    }
}
