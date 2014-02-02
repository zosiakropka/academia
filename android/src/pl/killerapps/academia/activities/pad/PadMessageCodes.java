package pl.killerapps.academia.activities.pad;

import java.util.HashMap;
import java.util.Map.Entry;

public class PadMessageCodes {

  private static PadMessageCodes mc = null;
  private HashMap<String, Integer> CODES = new HashMap<String, Integer>();

  private PadMessageCodes() {
    CODES.put("purpose", 1);
    CODES.put("login", 2);
    CODES.put("password", 3);
    CODES.put("token", 4);
    CODES.put("message", 5);
    }

    public String label(Integer code) {
        for (Entry<String, Integer> entry : CODES.entrySet()) {
            if (code.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String labelByChar(Character code) {
        return label(Character.getNumericValue(code));
    }

    /**
     *
     * @param label
     * @return code of the msg label; null for absent label
     */
    public Integer code(String label) {
        return CODES.get(label);
    }

    public Character charCode(String label) {
        Integer code = CODES.get(label);
        return code != null ? Character.toChars(code)[0] : null;
    }

    public String stringCode(String label) {
        Integer code = CODES.get(label);
        return code != null ? charCode(label).toString() : label;
    }

    public static PadMessageCodes instance() {
        if (mc == null) {
            mc = new PadMessageCodes();
        }
        return mc != null ? mc : instance();
    }
}
