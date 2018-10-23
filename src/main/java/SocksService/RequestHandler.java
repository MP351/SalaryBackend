package SocksService;

import JsonSerialization.Serializer;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket client;

    public RequestHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (InputStreamReader in = new InputStreamReader(client.getInputStream())) {
            int buffLen = 1024;
            char buffer[] = new char[buffLen];
            int len;

            while (true) {
                len = in.read(buffer, 0, buffLen);
                if (len > 0) {
                    String strings[] = new String(buffer, 0, len).split("\n");
                    for (int i=0;i<strings.length;i++) {
                        ByteArrayInputStream bin = new ByteArrayInputStream(strings[i].getBytes());
                        Serializer.deserialize(new InputStreamReader(bin));
                    }
                }
                Thread.sleep(10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
