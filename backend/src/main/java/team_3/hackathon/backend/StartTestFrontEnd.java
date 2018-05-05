package team_3.hackathon.backend;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class StartTestFrontEnd {
    public static void main(String[] args) {
        try {
            Socket dbClient = IO.socket(Const.DBURL);
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

            odererClient.connect();
            dbClient.connect();

            //client.connect();

            //client.emit("test", "");
            dbClient.emit("proposal","{ticket_id: 1, user_id: 3, cmd: process}");
            dbClient.emit("proposal","{ticket_id: 2, user_id: 3, cmd: process}");
            Thread.sleep(2000);
            dbClient.emit("proposal","{ticket_id: 1, user_id: 1, cmd: process}");
            dbClient.emit("proposal","{ticket_id: 1, user_id: 1, cmd: close}");
            dbClient.emit("proposal","{ticket_id: 1, user_id: 3, cmd: close}");
            //client.emit("proposal","{ticket_id: 1, user_id: 3}");
            //client.emit("proposal","{ticket_id: 2, user_id: 2}");
            Thread.sleep(Integer.MAX_VALUE);
            dbClient.disconnect();
            odererClient.disconnect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
