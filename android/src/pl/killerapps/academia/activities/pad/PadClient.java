package pl.killerapps.academia.activities.pad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.text.TextUtils;
import android.util.Log;
import java.net.URI;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.Preferences;

public abstract class PadClient implements Runnable {

  static final int CHUNK_SIZE = 1024;
  private Socket socket;
  private final String ip;
  private final int port;
  private String buffer = "";
  static final String DELIMITER = "\u001E";
  private BufferedReader br;
  private BufferedWriter bw;
  Thread thread;
  boolean ready = true;

  public PadClient() throws PreferencesUninitializedException, FaultyConnectionDetailsException {
    URI padUri = Preferences.get().academiaPadUri();
    this.ip = padUri.getHost();
    this.port = padUri.getPort();
  }

  private void init()
          throws UnknownHostException, IOException {

    socket = new Socket(ip, port);

    InputStreamReader isr = new InputStreamReader(socket.getInputStream());
    br = new BufferedReader(isr);

    OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
    bw = new BufferedWriter(osw);
    Log.i("PadSocket", "Client initialized.");
  }

  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void run() {
    try {
      init();

      Log.i("PadSocket", "Waiting for data.");
      (new Thread(new Runnable() {

        public void run() {
          while (true) {
            try {
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
            } catch (SocketException e) {
              break;
            } catch (IOException e) {
              onFailure(e);
            }
          }
        }
      })).start();

      onReady();
    } catch (IOException e) {
      onFailure(e);
    } catch (IndexOutOfBoundsException e) {
      onFailure(e);
    }
  }

  void stop() throws IOException {
    thread.interrupt();
    socket.close();
  }

  public void send(PadMessage msg)
          throws IOException {
    bw.write(msg.encode() + DELIMITER);
    bw.flush();
  }

  protected abstract void onReady();

  protected abstract void onMessage(PadMessage message);

  protected abstract void onFailure(Exception e);
}
