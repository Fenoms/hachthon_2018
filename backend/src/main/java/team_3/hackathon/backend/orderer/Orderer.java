package team_3.hackathon.backend.orderer;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Orderer {
    SocketIOServer server;
    ProposalList<Integer, Proposal> proposalList;

    private int maxWaitingTime;
    private Timer timer;

    public Orderer(String hostname,
                   int port,
                   Optional<Integer> maxBlockSize,
                   Optional<Integer> numBuf,
                   Optional<Integer> maxWaitingTime){
        //TODO add to interface
        proposalList = new ProposalList<>(maxBlockSize.orElse(50), numBuf.orElse(3));
        this.maxWaitingTime = maxWaitingTime.orElse(500);

        timer = new Timer();

        Configuration config = new Configuration();
        config.setHostname(hostname);
        config.setPort(port);

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);

        config.setSocketConfig(socketConfig);

        server = new SocketIOServer(config);

        server.addNamespace("database");

        server.addConnectListener(client->{
            System.out.println(client.getSessionId() + "connected");
        });
        server.addEventListener("test", String.class, (client, data, ackSender)->{
           test(client, data);
        });

        server.addEventListener("proposal", String.class, (client, data, ackSender) -> {
            proposalHandler(client, data);
        });

        server.addEventListener("db_hello", String.class, (client, data, ackSender) -> {
            System.out.println("Database join: " + client.getSessionId());
            client.joinRoom("database");
        });
    }

    private void test(SocketIOClient client, String data){
        System.out.println("test");
        client.sendEvent("test", "");
    }

    private void proposalHandler(SocketIOClient client, String data){
        try {
            System.out.println("Orderer: " + data);
            JSONObject proposal = new JSONObject(data);
            HashMap<Integer, Proposal> block =
                    proposalList.append(proposal.getInt("ticket_id"),
                            new Proposal(proposal.getInt("user_id"),
                                    proposal.getString("cmd"),
                                    Optional.ofNullable(proposal.optString("reply"))));
//            if (block != null){
//                System.out.println("Orderor: " + block.toString());
//                //timer.cancel();
//                broadcastBlock(block);
//                //timer.purge();
//                //restartTimer();
//            }
            broadcastBlockIfThereIs(block);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void broadcastBlockIfThereIs(HashMap<Integer, Proposal> block){
        if (block != null) {
            System.out.println("Orderor broadcast: "+server.getRoomOperations("database").getClients());
            server.getRoomOperations("database").sendEvent("order", new Gson().toJson(block));
        }
    }

//    private void broadcastBlock(HashMap<Integer, Proposal> block){
//        server.getRoomOperations("database").sendEvent("order", new Gson().toJson(block));
//    }

//    private void restartTimer(){
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                broadcastBlockIfThereIs(proposalList.forceSwitchBuf());
//                restartTimer();
//            }
//        }, maxWaitingTime);
//    }

    public void start(){
        server.start();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                broadcastBlockIfThereIs(proposalList.forceSwitchBuf());
                //restartTimer();
            }
        }, maxWaitingTime, maxWaitingTime);
    }

    public void stop(){
        server.stop();
    }
}
