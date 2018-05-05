package team_3.hackathon.backend.orderer;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Orderer {
    SocketIOServer server;
    ProposalList<Integer, Proposal> proposalList;

    private int maxWaitingTime;
    private Timer timer;

    public Orderer(String hostname, int port){
        //TODO add to interface
        proposalList = new ProposalList<>(2, 2);
        maxWaitingTime = 1000;

        timer = new Timer();

        Configuration config = new Configuration();
        config.setHostname(hostname);
        config.setPort(port);

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);

        config.setSocketConfig(socketConfig);

        server = new SocketIOServer(config);

        server.addNamespace("database");

        server.addEventListener("test", String.class, (client, data, ackSender)->{
           test(client, data);
        });

        server.addEventListener("proposal", String.class, (client, data, ackSender) -> {
            proposalHandler(client, data);
        });

        server.addEventListener("db_hello", String.class, (client, data, ackSender) -> {
            client.joinRoom("database");
        });
    }

    private void test(SocketIOClient client, String data){
        System.out.println("test");
        client.sendEvent("test", "");
    }

    private void proposalHandler(SocketIOClient client, String data){
        try {
            JSONObject proposal = new JSONObject(data);
            HashMap<Integer, Proposal> block =
                    proposalList.append(proposal.getInt("ticket_id"),
                            new Proposal(proposal.getInt("user_id"),
                                    proposal.getString("cmd")));
            if (block != null){
                timer.cancel();
                timer.purge();
                restartTimer();
            }
            broadcastBlockIfThereIs(block);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void broadcastBlockIfThereIs(HashMap<Integer, Proposal> block){
        if (block != null) {
            server.getRoomOperations("database").sendEvent("order", new Gson().toJson(block));
        }
    }

    private void restartTimer(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                broadcastBlockIfThereIs(proposalList.forceSwitchBuf());
                restartTimer();
            }
        }, maxWaitingTime);
    }

    public void start(){
        server.start();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                broadcastBlockIfThereIs(proposalList.forceSwitchBuf());
                restartTimer();
            }
        }, maxWaitingTime);
    }

    public void stop(){
        server.stop();
    }
}
