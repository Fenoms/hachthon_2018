package team_3.hackathon.backend;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import team_3.hackathon.backend.userdb.UserDB;
import team_3.hackathon.backend.userdb.UserDBEntry;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class Backend {
    private SocketIOServer server;

    private ConcurrentHashMap<UUID, Integer> authedWorker;
    private ConcurrentHashMap<UUID, Integer> authedCustomer;

    private ConcurrentHashMap<Integer, String> tickets;

    private UserDB db;

    AtomicInteger nextTicketID;

    public Backend(String hostname, int port){
        init(hostname, port);
    }

    public void init(String hostname, int port){
        db = new UserDB();

        authedWorker = new ConcurrentHashMap<>();
        authedCustomer = new ConcurrentHashMap<>();
        tickets = new ConcurrentHashMap<>();

        nextTicketID = new AtomicInteger(0);

        Configuration config = new Configuration();
        config.setHostname(hostname);
        config.setPort(port);

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);

        config.setSocketConfig(socketConfig);

        server = new SocketIOServer(config);

        //server.addNamespace("employee");
        //server.addNamespace("customer");

        server.addConnectListener(socketIOClient->{System.out.println("Connected");});
        server.addEventListener("login", String.class, new DataListener<String>() {
                    @Override
                    public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                        loginHandler(client, data);
                    }
                });
        server.addEventListener("pull_tickets", String.class, (socketIOClient, str, ackRequest)
                        -> pullTicketsHandler(socketIOClient, str));
        server.addEventListener("new_ticket", String.class, (socketIOClient, str, ackRequest)
            -> newTicketHandler(socketIOClient, str));
        server.addEventListener("test", String.class, (socketIOClient, str, ackRequest)
            -> {System.out.println("test");});
        server.addDisconnectListener(socketIOClient->{System.out.println("disconnected");});
    }

    public void start(){
        server.start();
    }

    public void stop(){
        server.stop();
    }

    private void loginHandler(SocketIOClient socketIOClient, String str) throws JSONException {
        JSONObject json = new JSONObject(str);
        UserDBEntry user = db.getUserInfo(json.getInt("userID"));
        if (user.getType() == UserDBEntry.Type.WORKER) {
            authedWorker.put(socketIOClient.getSessionId(), user.getUserID());
            //socketIOClient.joinRoom("employee");
        } else {
            authedCustomer.put(socketIOClient.getSessionId(), user.getUserID());
            //socketIOClient.joinRoom("customer");
        }

        JSONObject reply = new JSONObject();
        reply.put("user", user.getUserName());
        socketIOClient.sendEvent("login_f", reply.toString());
    }

    private void newTicketHandler(SocketIOClient socketIOClient, String str) throws JSONException {
        //if (authedCustomer.containsKey(socketIOClient.getSessionId())) {
            JSONObject json = new JSONObject(str);
            System.out.println("new ticket from: " + socketIOClient.getSessionId());
            tickets.put(nextTicketID.getAndIncrement(), json.getString("text"));
        //}
    }

    private void pullTicketsHandler(SocketIOClient socketIOClient, String str) throws JSONException {
        //if (authedWorker.containsKey(socketIOClient.getSessionId())){
            JSONArray json = new JSONArray();

            synchronized (this.tickets){
                for(Map.Entry<Integer, String> entry: tickets.entrySet()){
                    JSONObject jsonEntry = new JSONObject();
                    jsonEntry.put("id", entry.getKey());
                    jsonEntry.put("text", entry.getValue());
                    json.put(jsonEntry);
                }
            }

            socketIOClient.sendEvent("pull_r", json.toString());
        //}
    }
}
