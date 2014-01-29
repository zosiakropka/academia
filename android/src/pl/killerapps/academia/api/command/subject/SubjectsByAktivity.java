package pl.killerapps.academia.api.command.subject;

import android.util.Log;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.killerapps.academia.api.command.ApiCommandAsync;
import pl.killerapps.academia.entities.Aktivity;
import pl.killerapps.academia.entities.Subject;

public abstract class SubjectsByAktivity extends ApiCommandAsync<List<Subject>> {

    public SubjectsByAktivity(String base_url) throws URISyntaxException {
        super(base_url, "/subject/list/");
    }

    @Override
    protected List<Subject> process_json(JSONArray subjects_json) {
        List<Subject> subjects = new ArrayList<Subject>();
        for (int i = 0; i < subjects_json.length(); i++) {
            try {
                JSONObject subject_json = subjects_json.getJSONObject(i);
                Subject subject = new Subject();
                subject.name = subject_json.getString("name");
                subject.abbr = subject_json.getString("abbr");
                subject.aktivities = new ArrayList<Aktivity>();
                JSONArray aktivities_json = subject_json.getJSONArray("activities");
                for (int j = 0; j < aktivities_json.length(); j++) {
                    JSONObject aktivity_json = aktivities_json.getJSONObject(j);
                    Aktivity aktivity = new Aktivity();
                    aktivity.type = aktivity_json.getString("type");
                    aktivity.id = aktivity_json.getInt("pk");
//                    JSONObject supervisor_json = aktivity_json.getJSONObject("supervisor");
//                    aktivity.supervisor = supervisor_json.getString("fistname") + ' ' + supervisor_json.getString("lastname");
                    JSONArray notes_ids_json = aktivity_json.getJSONArray("notes");
                    List<NameValuePair> notes_request_params = new ArrayList<NameValuePair>();
//                    for (int k = 0; k < notes_ids_json.length(); k++) {
//                        int note_id = notes_ids_json.getJSONObject(k).getInt("pk");
//                        notes_request_params.add(new BasicNameValuePair("note_id", Integer.toString(note_id)));
//                        try {
//                            aktivity.notes = (new NoteList(base_url)).get(notes_request_params);
//                        } catch (ClientProtocolException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (URISyntaxException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    subject.aktivities.add(aktivity);
                }
                subjects.add(subject);
            } catch (JSONException ex) {
                Log.e("academia_api", "Can't parse", ex);
            }
        }
        return subjects;
    }

}
