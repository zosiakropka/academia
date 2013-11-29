package pl.killerapps.academia.pad;

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

	private HashMap<String, String> data;
	private Set<String> flags;

	public PadMessage() {
	        data = new HashMap<String, String>();
	        flags = new HashSet<String>();
	}
	public PadMessage(char[] raw) {
		decode (new String(raw));
	}

	public void set(String key, String value) {
	        data.put(key, value);
	}
	public void set(String key) {
	        flags.add(key);
	}
	public String get(String key) {
	        return data.get(key);
	}
	public boolean test(String key) {
	        return flags.contains(key);
	}
	public boolean test(Collection<String> keys) {
	        return flags.containsAll(keys);
	}

	public String encode() {
        Character ver = Character.toChars(VERSION)[0];
        Set<String> msgs = new HashSet<String>();
	
	    for (Iterator<Entry<String, String>> i = data.entrySet().iterator(); i.hasNext();) {
	        HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>)i.next();
	
	        String code = CODES.stringCode((entry.getKey()));
	        String value = entry.getValue();
	        //String value = Base64.encodeToString(entry.getValue().getBytes(), Base64.DEFAULT);
	        msgs.add(code + INNER_DELIMITER + value);
	        i.remove();
	    }
	    for(Iterator<String> i = flags.iterator(); i.hasNext(); ) {
	        String flag = CODES.stringCode(i.next());
	        msgs.add(flag);
	        i.remove();
	    }
	    return ver.toString() + TextUtils.join(UNIT_DELIMITER, msgs).toString();
	}
	
	
	
	public void decode(String message) { // It works!
		//message = Base64.encodeToString(message.getBytes(), Base64.DEFAULT); /* Decode all Base64 */
		Log.i("PadSocket", message);
        int ver = message.charAt(0);
        if (ver == VERSION) {
                String[] units = TextUtils.split(message.substring(1), UNIT_DELIMITER);
                message.replace(UNIT_DELIMITER, "---");
                for (String unit: units) {
                        String[] pair = TextUtils.split(unit, INNER_DELIMITER);
                        String key = pair[0];
                        String keyLabel = null;
                        if (key.length() == 1) {
                                keyLabel = CODES.label((int) key.charAt(0));
                        } else {
                            keyLabel = key;
                            // keyLabel = Base64.decode(key, Base64.DEFAULT).toString();
                        }
                        if (pair.length == 1) {
                                set(keyLabel);
                        } else {
                            String value = pair[1];
                            //String value = Base64.decode(pair[1], Base64.DEFAULT).toString(); /* Decode each value Base64 */
                            set(keyLabel, value);
                        }
                }
        }
	}

}
