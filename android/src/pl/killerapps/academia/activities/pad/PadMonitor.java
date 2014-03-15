package pl.killerapps.academia.activities.pad;

import android.util.Log;
import android.widget.EditText;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

  private final Queue<String> patches_fifo = new LinkedList<String>();

  private boolean paused = false;

  static long PERIOD = 500; // [ms]

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
    this.paused = false;

    prev = editor.getText().toString();
    timer.scheduleAtFixedRate(new TimerTask() {

      @Override
      public void run() {
        if (!paused) {
          if (patches_fifo.size() > 0) {
            while (!patches_fifo.isEmpty()) {
              apply_patches(patches_fifo.remove());
            }
          } else {
            paused = true;
            String curr = editor.getText().toString();
            LinkedList<DiffMatchPatch.Diff> diffs = dmp.diff_main(prev, curr);
            String patches_text = dmp.patch_toText(dmp.patch_make(diffs));
            if (!patches_text.equals("")) {
              on_local_patches(patches_text);
              prev = curr;
            }
          }
          paused = false;
        }
      }
    }, PERIOD, PERIOD);
  }

  public void pause() {
    this.paused = true;
  }

  public void resume() {
    this.paused = false;
  }

  /**
   * Implement what should be done if diffs were detected within currently
   * edited pad.
   *
   * @param patches_text
   */
  public abstract void on_local_patches(String patches_text);

  public void push_patches_text(String patches_text) {
    this.patches_fifo.add(patches_text);
  }

  private void apply_patches(String patches_text) {
    pause();
    final String curr = editor.getText().toString();
    List<DiffMatchPatch.Patch> patches;
    patches = (LinkedList<DiffMatchPatch.Patch>) dmp.patch_fromText(patches_text);
    Log.i("patches_len", "Patches length: " + patches.size());
    final String next = (String) ((dmp.patch_apply((LinkedList<DiffMatchPatch.Patch>) patches, curr))[0]);
    activity.runOnUiThread(new SafeRunnable(activity) {
      @Override
      public void safeRun()
              throws PreferencesUninitializedException,
              FaultyConnectionDetailsException, URISyntaxException,
              MalformedURLException, IOException, HelloRequiredException,
              LoginRequiredException, HttpHostConnectException,
              HelloFailedException {
        synchronized (editor) {
          editor.setText(next);
        }
        synchronized (prev) {
          prev = next;
        }
      }

    });
    start();
  }
}
