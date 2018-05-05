package team_3.hackathon.backend.database;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import team_3.hackathon.backend.orderer.Proposal;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
    private HashMap<Integer, CacheInfo> cache;
    Socket client;
    SocketIOServer server;
    ReentrantReadWriteLock cacheLock;

    public Database(){
        cache = new HashMap<>();
        cacheLock = new ReentrantReadWriteLock();

        try {
            client = IO.socket("http://127.0.0.1:12345");

            client.on("order", objects->{
                orderHandler((String) objects[0]);
            });


            //Server

            Configuration config = new Configuration();
            config.setHostname("localhost");
            config.setPort(12346);

            SocketConfig socketConfig = new SocketConfig();
            socketConfig.setReuseAddress(true);

            config.setSocketConfig(socketConfig);

            server = new SocketIOServer(config);

            server.addEventListener("proposal", String.class, ((client, data, ackSender) -> {
                proposalHandler(client, data);
            }));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        client.connect();
        client.emit("db_hello", "");
        server.start();
    }

    public void stop(){
        server.stop();
        client.disconnect();
    }

    private void orderHandler(String order_str){
        HashMap<Integer, Proposal> toBeUpdated = new HashMap<>();
        ArrayList<Integer> toBeDeletedFromCache = new ArrayList<>();
        Type type = new TypeToken<HashMap<Integer, Proposal>>(){}.getType();
        HashMap<Integer, Proposal> order = new Gson().fromJson(order_str, type);

        cacheLock.writeLock().lock();
        for(Map.Entry<Integer, Proposal> entry: order.entrySet()){
            if (entry.getValue().getCmd().equals("process")){
                if (! cache.containsKey(entry.getKey())){
                    cache.put(entry.getKey(), new CacheInfo(entry.getValue().getUserID(), entry.getValue().getCmd()));
                    toBeUpdated.put(entry.getKey(), entry.getValue());
                }
            } else {
                if (cache.containsKey(entry.getKey())
                        && cache.get(entry.getKey()).getUserID() == entry.getValue().getUserID()){
                    toBeDeletedFromCache.add(entry.getKey());
                    toBeUpdated.put(entry.getKey(), entry.getValue());
                }
            }
        }
        for (Integer i: toBeDeletedFromCache){
            cache.remove(i);
        }
        cacheLock.writeLock().unlock();

        if (toBeUpdated.size() > 0) {
            server.getBroadcastOperations().sendEvent("order", new Gson().toJson(toBeUpdated));

            //TODO change database state
            //System.out.println(toBeUpdated);
        }
    }

    private void proposalHandler(SocketIOClient client, String data){
        JSONObject proposal = null;
        try {
            proposal = new JSONObject(data);
            int ticketID = proposal.getInt("ticket_id");
            int userID = proposal.getInt("user_id");
            String cmd = proposal.getString("cmd");
            boolean accept = false;
            if (cmd.equals("process")){
                cacheLock.readLock().lock();
                if (! cache.containsKey(ticketID)){
                    accept = true;
                }
                cacheLock.readLock().unlock();
            } else {
                cacheLock.readLock().lock();
                if (cache.containsKey(ticketID) && cache.get(ticketID).getUserID() == userID){
                    accept = true;
                }
                cacheLock.readLock().unlock();
            }

            if(accept){
                client.sendEvent("accept", data);
            } else {
                client.sendEvent("refuse", data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
