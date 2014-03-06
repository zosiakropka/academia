package pl.killerapps.academia.activities.pad;

import android.util.Log;
import android.widget.EditText;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import name.fraser.neil.plaintext.DiffMatchPatch;
import org.apache.http.conn.HttpHostConnectException;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloFailedException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.LoginRequiredException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;
import pl.killerapps.academia.utils.safe.SafeRunnable;

/**
 *
 * @author zosia
 */
public abstract class PadMonitor {

  private boolean running = false;

  static long PERIOD = 5000; // [ms]

  private final EditText editor;
  private final SafeActivity activity;
  private final Timer timer = new Timer();
  private final DiffMatchPatch dmp = new DiffMatchPatch();

  private String prev;

  public PadMonitor(SafeActivity activity, EditText editor) {
    this.editor = editor;
    this.activity = activity;
  }

  public void start() {
    this.running = true;

    prev = editor.getText().toString();
    timer.scheduleAtFixedRate(new TimerTask() {

      @Override
      public void run() {
        if (running) {
          String curr = editor.getText().toString();
          LinkedList<DiffMatchPatch.Diff> diffs = dmp.diff_main(prev, curr);
          String patches_text = dmp.patch_toText(dmp.patch_make(diffs));
          if (!patches_text.equals("")) {
            on_patches(patches_text);
            prev = curr;
          }
        }
      }
    }, PERIOD, PERIOD);
  }

  public void pause() {
    this.running = false;
  }

  public void resume() {
    this.running = true;
  }

  public abstract void on_patches(String patches_text);

  public void apply_patches(String patches_text) {
    pause();
    final String curr = editor.getText().toString();
    List<DiffMatchPatch.Patch> patches = (LinkedList<DiffMatchPatch.Patch>) dmp.patch_fromText(patches_text);
    Log.i("patches_len", "Patches length: " + patches.size());
    final String next = (String) ((dmp.patch_apply((LinkedList<DiffMatchPatch.Patch>) patches, curr))[0]);
    activity.runOnUiThread(new SafeRunnable(activity) {
      @Override
      public void safeRun() throws PreferencesUninitializedException, FaultyConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, LoginRequiredException, HttpHostConnectException, HelloFailedException {
        editor.setText(next);
      }

    });
    start();
  }
}
