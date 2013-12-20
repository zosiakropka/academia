package pl.killerapps.academia.api.command.note;

import android.util.Log;

import java.io.IOException;
import java.net.URI;
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

public abstract class NoteListBySubjectByAktivity extends ApiCommandAsync<List<Subject>> {

    public NoteListBySubjectByAktivity(URI uri) {
        super(uri);
    }

    public NoteListBySubjectByAktivity(String base_url) throws URISyntaxException {
        super(base_url, "/subject/list/");
        //super(base_url, "/fail/");
    }

    @Override
    protected List<Subject> process_json(JSONArray subjects_json) {
        List<Subject> subjects = new ArrayList<Subject>();
        for (int i = 0; i < subjects_json.length(); i++) {
            try {
                JSONObject subject_json = subjects_json.getJSONObject(i).getJSONObject("fields");
                Subject subject = new Subject();
                subject.name = subject_json.getString("name");
                subject.abbr = subject_json.getString("abbr");
                subject.aktivities = new ArrayList<Aktivity>();
                JSONArray aktivities_json = subject_json.getJSONArray("activities");
                for (int j = 0; j < aktivities_json.length(); j++) {
                    JSONObject aktivity_json = aktivities_json.getJSONObject(j);
                    Aktivity aktivity = new Aktivity();
                    JSONObject supervisor_json = aktivity_json.getJSONObject("supervisor");
                    aktivity.supervisor = supervisor_json.getString("fistname") + ' ' + supervisor_json.getString("lastname");
                    subject.aktivities.add(aktivity);
                    JSONArray notes_json = aktivity_json.getJSONArray("notes");
                    List<NameValuePair> notes_request_params = new ArrayList<NameValuePair>();
                    for (int k = 0; k < notes_json.length(); k++) {
                        int note_id = notes_json.getJSONObject(k).getInt("pk");
                        notes_request_params.add(new BasicNameValuePair("note_id", Integer.toString(note_id)));
                        try {
                            aktivity.notes = (new NoteList(uri)).send_request_get_response(notes_request_params);
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
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
