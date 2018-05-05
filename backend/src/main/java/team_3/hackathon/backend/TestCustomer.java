package team_3.hackathon.backend;

import io.socket.client.IO;
import io.socket.client.Socket;
import team_3.hackathon.backend.database.Database;

import java.net.URISyntaxException;

public class TestCustomer {
    public static void main(String[] args) {
//        TestClient client = new TestClient("http://127.0.0.1:12345");
//        client.start();
//        try {
//            client.login(3);
//            client.newTicket("Hello World 1!");
//            client.newTicket("Hello World 2!");
//            client.newTicket("Hello World 3!");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        client.stop();

//        Socket socket = null;
//        try {
//            socket = IO.socket("http://127.0.0.1:12345");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        socket.connect();
//        socket.emit("login", "aaa");
//        socket.disconnect();

        try {
            //Socket client = IO.socket("http://127.0.0.1:12346");

//            client.on("test", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    System.out.println("test");
//                }
//            });

            //client.on("test", objects->{System.out.println("test");});
//            client.on("order", objects->{
//                System.out.println((String) objects[0]);
//            });

            Socket dbClient = IO.socket("http://127.0.0.1:12346");
            Socket odererClient = IO.socket("http://127.0.0.1:12345");

            dbClient.on("accept", objects->{
                odererClient.emit("proposal", (String) objects[0]);
            }).on("refuse", objects->{
                System.out.println("refused: " + objects[0]);
            }).on("order", o->{
                System.out.println("order:");
                System.out.println(o[0]);
            });

            odererClient.connect();
            dbClient.connect();

            Database db = new Database();

            //client.connect();
            db.start();
            //client.emit("test", "");
            dbClient.emit("proposal","{ticket_id: 1, user_id: 3, cmd: process}");
            Thread.sleep(2000);
            dbClient.emit("proposal","{ticket_id: 1, user_id: 1, cmd: process}");
            dbClient.emit("proposal","{ticket_id: 1, user_id: 1, cmd: close}");
            dbClient.emit("proposal","{ticket_id: 1, user_id: 3, cmd: close}");
            //client.emit("proposal","{ticket_id: 1, user_id: 3}");
            //client.emit("proposal","{ticket_id: 2, user_id: 2}");
            Thread.sleep(Integer.MAX_VALUE);
            dbClient.disconnect();
            odererClient.disconnect();
            db.stop();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
