import server.Server;

public class Main {
    private static final Server server = new Server();
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Server ♕");
        server.run(3676);
    }
}