package pl.killerapps.academia;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.text.TextUtils;

public class MessageEnDec {
	
	static MessageCodes CODES = MessageCodes.instance();

	static final char INNER_DELIMITER = '\u001F';
	static final String UNIT_DELIMITER = "\u001F\u001F";
	
	static final int VERSION = 1;
	
	private HashMap<String, String> data = new HashMap<String, String>();
	private Set<String> flags = new HashSet<String>();
	
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
	
	public MessageEnDec() {
		System.out.println("Creating message endec");
	}
	
	public String encode() {
		Character ver = Character.toChars(VERSION)[0];
		Set<String> msgs = new HashSet<String>();
		
	    
	    for (Iterator<Entry<String, String>> i = data.entrySet().iterator(); i.hasNext();) {
	        HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>)i.next();
	        
	        String code = CODES.stringCode((entry.getKey()));
	        String value = Base64.encode(entry.getValue().getBytes());
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
}

class Base64 {

    private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    private static int[] toInt = new int[128];

    static {
        for (int i = 0; i < ALPHABET.length; i++) {
            toInt[ALPHABET[i]] = i;
        }
    }

    /**
     * Translates the specified byte array into Base64 string.
     *
     * @param buf the byte array (not null)
     * @return the translated Base64 string (not null)
     */
    public static String encode(byte[] buf) {
        int size = buf.length;
        char[] ar = new char[((size + 2) / 3) * 4];
        int a = 0;
        int i = 0;
        while (i < size) {
            byte b0 = buf[i++];
            byte b1 = (i < size) ? buf[i++] : 0;
            byte b2 = (i < size) ? buf[i++] : 0;

            int mask = 0x3F;
            ar[a++] = ALPHABET[(b0 >> 2) & mask];
            ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
            ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
            ar[a++] = ALPHABET[b2 & mask];
        }
        switch (size % 3) {
            case 1:
                ar[--a] = '=';
            case 2:
                ar[--a] = '=';
        }
        return new String(ar);
    }

    /**
     * Translates the specified Base64 string into a byte array.
     *
     * @param s the Base64 string (not null)
     * @return the byte array (not null)
     */
    public static byte[] decode(String s) {
        int delta = s.endsWith("==") ? 2 : s.endsWith("=") ? 1 : 0;
        byte[] buffer = new byte[s.length() * 3 / 4 - delta];
        int mask = 0xFF;
        int index = 0;
        for (int i = 0; i < s.length(); i += 4) {
            int c0 = toInt[s.charAt(i)];
            int c1 = toInt[s.charAt(i + 1)];
            buffer[index++] = (byte) (((c0 << 2) | (c1 >> 4)) & mask);
            if (index >= buffer.length) {
                return buffer;
            }
            int c2 = toInt[s.charAt(i + 2)];
            buffer[index++] = (byte) (((c1 << 4) | (c2 >> 2)) & mask);
            if (index >= buffer.length) {
                return buffer;
            }
            int c3 = toInt[s.charAt(i + 3)];
            buffer[index++] = (byte) (((c2 << 6) | c3) & mask);
        }
        return buffer;
    }

}