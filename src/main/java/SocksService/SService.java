package SocksService;

import sun.misc.RequestProcessor;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SService implements Runnable {
    private int serverPort = 2018;
    private int buffer_size = 1024;
    private byte buffer[] = new byte[buffer_size];
    private ServerSocket ss;

    @Override
    public void run() {
        serverInit();
    }

    private void serverInit() {
        try {
            ss = new ServerSocket(serverPort);
            Executor executor = Executors.newFixedThreadPool(10);
            System.out.println("Waiting...");


            while (true) {
                Socket skt = ss.accept();

                Runnable worker = new RequestHandler(skt);
                executor.execute(worker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
