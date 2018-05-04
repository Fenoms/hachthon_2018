package team_3.hackathon.backend;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import team_3.hackathon.backend.testclients.TestClient;

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

        Socket socket = null;
        try {
            socket = IO.socket("http://127.0.0.1:12345");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
        socket.emit("auth", "");
        socket.disconnect();
    }
}
