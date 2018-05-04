package team_3.hackathon.backend.testclients;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class TestClient {
    private Socket client;

    public TestClient(String url){
        init(url);
    }

    private void init(String url){
        try {
            client = IO.socket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


       client.on("login_f", objects->{
           JSONObject json = null;
           try {
               json = new JSONObject((String) objects[0]);
               System.out.println("Welcome " + json.getString("user"));
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }).on("pull_r", objects->{
           JSONArray json = null;
           try {
               json = new JSONArray((String) objects[0]);
               for (int i = 0; i < json.length(); ++i){
                   JSONObject entry = (JSONObject) json.get(i);
                   System.out.println("TicketID: " + entry.get("id"));
                   System.out.println("Text: " + entry.get("text"));
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

       });
    }

    public void newTicket(String txt) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", txt);
        client.emit("new_ticket", jsonObject.toString());
    }

    public void pullTickets(){
        client.emit("pull_tickets", "");
    }

    public void login(int id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", id);
        client.emit("auth", jsonObject.toString());
    }

    public void start(){
        client.connect();
    }

    public void stop(){
        client.disconnect();
    }
}
