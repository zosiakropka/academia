/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.killerapps.academia.api.command.aktivity;

import android.util.SparseArray;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.killerapps.academia.api.command.ApiCommand;
import pl.killerapps.academia.api.command.ApiCommandAsync;
import pl.killerapps.academia.entities.Aktivity;
import pl.killerapps.academia.entities.Subject;

/**
 *
 * @author zosia
 */
public abstract class AktivityListBySubject extends ApiCommandAsync<SparseArray<Subject>> {

    public AktivityListBySubject(String base_url) throws URISyntaxException {
        super(base_url, "/activity/list/");
    }

    @Override
    protected SparseArray<Subject> process_json(JSONArray json) {
        SparseArray<Subject> subjects = new SparseArray<Subject>();
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject subject_json = json.getJSONObject(i);
                Subject subject = new Subject();
                subject.id = subject_json.getInt("pk");
                subject.name = subject_json.getString("name");
                subject.abbr = subject_json.getString("abbr");
                subject.aktivities = new ArrayList<Aktivity>();
                JSONArray aktivities_json = subject_json.getJSONArray("activities");
                for (int j = 0; j < aktivities_json.length(); j++) {
                    JSONObject aktivity_json;
                    aktivity_json = aktivities_json.getJSONObject(j);
                    JSONObject supervisor_json = aktivity_json.getJSONObject("supervisor");
                    Aktivity aktivity = new Aktivity();
                    aktivity.supervisor = supervisor_json.getString("firstname") + ' ' + supervisor_json.getString("lastname");
                    aktivity.type = subject_json.getString("type");
                    subject.aktivities.add(aktivity);
                }
                subjects.append(subject.id, subject);
            }
        } catch (JSONException ex) {
            Logger.getLogger(AktivityListBySubject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return subjects;
    }

}
