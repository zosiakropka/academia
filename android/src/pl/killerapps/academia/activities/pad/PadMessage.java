package pl.killerapps.academia.activities.pad;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class PadMessage {

  static PadMessageCodes CODES;

  {
    CODES = PadMessageCodes.instance();
  }
  private static final String INNER_DELIMITER = "\u001F";
  private static final String UNIT_DELIMITER = "\u001F\u001F";
  public static final char VERSION = '\u0001';
  private HashMap<String, String> strings;
  private Set<String> flags;
  private HashMap<String, byte[]> data;

  public PadMessage() {
    strings = new HashMap<String, String>();
    data = new HashMap<String, byte[]>();
    flags = new HashSet<String>();
  }

  public PadMessage(char[] raw) {
    decode(new String(raw));
  }

  public void set_string(String key, String value) {
    strings.put(key, value);
  }

  public void set_data(String key, byte[] bytes) {
    data.put(key, bytes);
  }

  public void set_flag(String key) {
    flags.add(key);
  }

  public String get_string(String key) {
    return strings.get(key);
  }

  public byte[] get_data(String key) {
    return data.get(key);
  }

  public boolean has_flag(String key) {
    return flags.contains(key);
  }

  public boolean has_flags(Collection<String> keys) {
    return flags.containsAll(keys);
  }

  public boolean contains(String key) {
    return strings.containsKey(key) || data.containsKey(key) || flags.contains(key);
  }

  public String encode() {
    Set<String> msgs = new HashSet<String>();

    for (Iterator<Entry<String, String>> i = strings.entrySet().iterator(); i.hasNext();) {
      HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>) i.next();

      String code = CODES.stringCode((entry.getKey()));
      String value = Base64.encodeToString(entry.getValue().getBytes(), Base64.DEFAULT);
      msgs.add(code + INNER_DELIMITER + value);
      i.remove();
    }
    for (Iterator<Entry<String, byte[]>> i = data.entrySet().iterator(); i.hasNext();) {
      HashMap.Entry<String, byte[]> entry = (HashMap.Entry<String, byte[]>) i.next();

      String code = CODES.stringCode((entry.getKey()));
      String value = Base64.encodeToString(entry.getValue(), Base64.DEFAULT);
      msgs.add(code + INNER_DELIMITER + value);
      i.remove();
    }
    for (Iterator<String> i = flags.iterator(); i.hasNext();) {
      String flag = CODES.stringCode(i.next());
      msgs.add(flag);
      i.remove();
    }
    return TextUtils.join(UNIT_DELIMITER, msgs).toString();
  }

  public void decode(String message) {
    String[] units = TextUtils.split(message, UNIT_DELIMITER);
    for (String unit : units) {
      String[] pair = TextUtils.split(unit, INNER_DELIMITER);
      String key = pair[0];
      String keyLabel = null;
      keyLabel = CODES.label((int) key.charAt(0));
      if (pair.length == 1) {
        set_flag(keyLabel);
      } else {
        byte[] bytes = Base64.decode(pair[1], Base64.DEFAULT);
        String value;
        try {
          value = new String(bytes, "UTF-8");
          set_string(keyLabel, value);
        } catch (UnsupportedEncodingException e) {
          Log.i("PadSocket", "Processing bytes.");
          set_data(keyLabel, bytes);
        }

      }
    }
  }
}
