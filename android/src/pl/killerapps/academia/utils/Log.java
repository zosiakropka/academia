package pl.killerapps.academia.utils;

/**
 *
 * @author zosia
 */
public class Log {

  private final String tag;

  public Log(String tag) {
    this.tag = tag;
  }

  public void v(String msg) {
    android.util.Log.v(tag, msg);
  }

  public void d(String msg) {
    android.util.Log.d(tag, msg);
  }

  public void i(String msg) {
    android.util.Log.i(tag, msg);
  }

  public void w(String msg) {
    android.util.Log.w(tag, msg);
  }

  public void w(String msg, Throwable tr) {
    android.util.Log.w(tag, msg, tr);
  }

  public void w(Throwable tr) {
    android.util.Log.w(tag, tr);
  }

  public void e(String msg) {
    android.util.Log.e(tag, msg);
  }

  public void e(String msg, Throwable tr) {
    android.util.Log.e(tag, msg, tr);
  }

  public void e(Throwable tr) {
    android.util.Log.e(tag, tr.getLocalizedMessage(), tr);
  }

  public void wtf(String msg) {
    android.util.Log.wtf(tag, msg);
  }

  public void wtf(String msg, Throwable tr) {
    android.util.Log.wtf(tag, msg, tr);
  }

}
