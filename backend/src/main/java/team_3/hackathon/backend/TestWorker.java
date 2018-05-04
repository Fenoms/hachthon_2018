package team_3.hackathon.backend;

import org.json.JSONException;
import team_3.hackathon.backend.testclients.TestClient;

public class TestWorker {
    public static void main(String[] args) {
        TestClient client = new TestClient("http://127.0.0.1:12345");
        client.start();
        try {
            client.login(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.pullTickets();
        client.stop();
    }
}
