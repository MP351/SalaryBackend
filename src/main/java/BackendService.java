import SocksService.SService;

public class BackendService {
    public static void main(String[] args) {

        Thread serverThread = new Thread(new SService());
        serverThread.start();
    }
}
