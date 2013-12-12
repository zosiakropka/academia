package pl.killerapps.academia.pad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.UnsupportedOperationException;

import android.text.TextUtils;
import android.util.Log;

public abstract class PadClient implements Runnable {

    static final int CHUNK_SIZE = 1024;

    private Socket socket;
    private String ip;
    private int port;
    private String buffer = "";
    static final String DELIMITER = "\u001E";

    private BufferedReader br;
    private BufferedWriter bw;

    Thread thread;

    boolean ready = true;

    public PadClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private void init() throws UnknownHostException, IOException {
        socket = new Socket(ip, port);

        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        br = new BufferedReader(isr);

        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
        bw = new BufferedWriter(osw);
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            init();

            Log.i("PadSocket", "Waiting for data.");
            while (true) {
                String line = br.readLine();
                if (line != null && !line.isEmpty()) {
                    buffer += line;
                }
                int d_index = buffer.indexOf(DELIMITER);
                if (d_index >= 0) {
                    String parts[] = TextUtils.split(buffer, DELIMITER);
                    buffer = parts[parts.length - 1];  // this ought to be implemented in C-pointer-like manner
                    for (int i = 0; i < parts.length - 1; i++) {
                        PadMessage msg = new PadMessage();
                        msg.decode(parts[i]);
                        Log.i("PadSocket", "Data onboard: " + parts[i]);
                        if (msg.contains("purpose")) {
                            onMessage(msg);
                        }
                    }
                }
            }
        } catch (IOException e) {
            onFailure(e);
        } catch (IndexOutOfBoundsException e) {
            onFailure(e);
        }
    }

    void stop() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void send(PadMessage msg) throws UnsupportedOperationException, IOException {
        bw.write(msg.encode() + DELIMITER);
        bw.flush();
    }

    protected abstract void onMessage(PadMessage message);

    protected abstract void onFailure(Exception e);
}
