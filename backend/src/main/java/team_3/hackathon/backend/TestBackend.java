package team_3.hackathon.backend;

public class TestBackend {
    public static void main(String[] args) {
        Backend backend = new Backend("localhost", 12345);

        backend.start();

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        backend.stop();
    }
}
