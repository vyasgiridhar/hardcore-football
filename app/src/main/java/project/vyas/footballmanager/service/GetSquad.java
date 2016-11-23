package project.vyas.footballmanager.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.vyas.footballmanager.adpater.SquadListAdapter;
import project.vyas.footballmanager.model.Player;

/**
 * Created by vyas on 11/21/16.
 */

public class GetSquad extends AsyncTask<Void, Void, Boolean> {

    private ListView lv;
    private String TeamName;
    private Context context;
    private ProgressDialog pd;
    private ArrayList<Player> squad;

    public GetSquad(ListView listView,String Name,Context context) {
        this.lv = listView;
        this.TeamName = Name;
        this.context = context;
    }
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.show();

    }

    protected Boolean doInBackground(Void... aParams) {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        try {
            Log.d("Here ", "OK");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.43.58:5000/Team/squad/" + TeamName)
                    .build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Player p = new Player();
            try {
                squad = new ArrayList<>();
                JSONArray json = new JSONArray(result);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject player = json.getJSONObject(i);
                    p.setId(player.getString("Player_id"));
                    p.setfName(player.getString("fname"));
                    p.setlName(player.getString("l_name"));
                    p.setImageUrl(player.getString("Image"));
                    p.setPosition(player.getString("Pos"));
                    squad.add(p);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            Log.d("Result : ", "doInBackground: " + result);

            return true;
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return false;
        }
    }

    protected void onPostExecute(Boolean status) {
        pd.dismiss();
        if (status) {
            SquadListAdapter adapter = new SquadListAdapter(squad, context);
            lv.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
        }
    }
}
