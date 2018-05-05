package team_3.hackathon.backend;

import io.socket.client.IO;
import io.socket.client.Socket;
import team_3.hackathon.backend.database.Database;

import java.net.URISyntaxException;

public class StartTestFrontEnd {
    public static void main(String[] args) {
        try {
            Socket dbClient = IO.socket(Const.DBURL);
            //Socket dbClient = IO.socket("http://192.168.137.19:12346");
            Socket odererClient = IO.socket(Const.OrdererURL);

            dbClient.on("accept", objects->{
                System.out.println("accept: " + objects[0]);
                odererClient.emit("proposal", (String) objects[0]);
            }).on("refuse", objects->{
                System.out.println("refused: " + objects[0]);
            }).on("order", o->{
                System.out.println("order:");
                System.out.println(o[0]);
            });

            //Database db = new Database(Const.OrdererURL, "192.168.137.19", 12346);
            //db.start();

            odererClient.connect();
            dbClient.connect();

            //client.connect();

            //client.emit("test", "");

            dbClient.emit("proposal","{ticket_id: 1, user_id: 3, cmd: process}");
            dbClient.emit("proposal","{ticket_id: 2, user_id: 3, cmd: process}");
            Thread.sleep(2000);
            dbClient.emit("proposal","{ticket_id: 1, user_id: 1, cmd: process}");
            dbClient.emit("proposal","{ticket_id: 1, user_id: 1, reply: \"asdfasdfasdfasdfadsf\", cmd: close}");
            dbClient.emit("proposal","{ticket_id: 1, user_id: 3, reply: \"asdfasdfasdfasdf\", cmd: close}");
            //client.emit("proposal","{ticket_id: 1, user_id: 3}");
            //client.emit("proposal","{ticket_id: 2, user_id: 2}");
            Thread.sleep(Integer.MAX_VALUE);
            dbClient.disconnect();
            odererClient.disconnect();
            //db.stop();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
