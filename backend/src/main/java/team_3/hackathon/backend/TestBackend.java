package team_3.hackathon.backend;

import team_3.hackathon.backend.orderer.Orderer;

public class TestBackend {
    public static void main(String[] args) {
//        Backend backend = new Backend("localhost", 12345);
//
//        backend.start();
//
//        try {
//            Thread.sleep(Integer.MAX_VALUE);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        backend.stop();

//        SocketIOServer server;
//
//        Configuration config = new Configuration();
//        config.setHostname("localhost");
//        config.setPort(12345);
//
//        SocketConfig socketConfig = new SocketConfig();
//        socketConfig.setReuseAddress(true);
//
//        config.setSocketConfig(socketConfig);
//
//        server = new SocketIOServer(config);
//
//        server.addEventListener("test", String.class, (client, data, ackSender)->{
//            System.out.println("test");
//            client.sendEvent("test", "");
//        });
//
//        server.start();

        Orderer orderer = new Orderer("localhost", 12345);

        orderer.start();

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //server.stop();
        orderer.stop();
    }
}
