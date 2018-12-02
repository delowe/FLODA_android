package floda.pl.floda3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Floda_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    User_info usr;
    String idd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.appbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Option unavailable yet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent i = getIntent();

        idd=i.getStringExtra("ID");
        getDetail(idd,new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack())));
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_fram, new ListOfPlants());
        t.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.floda_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();

        if (id == R.id.p_list) {
            t.replace(R.id.content_fram, new ListOfPlants());

        } else if (id == R.id.log_out) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor e= preferences.edit();
            e.remove("ID");
            e.commit();
            Intent i = new Intent(this,Floda_LOGIN.class);
            startActivity(i);
        }
        t.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void getDetail(String id, RequestQueue q) {
        String sql="http://www.serwer1727017.home.pl/2ti/floda/floda_ifo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sql, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    JSONObject o = j.getJSONObject(0);
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View header= navigationView.getHeaderView(0);
                    usr = new User_info(o.getString("Name"),o.getString("email"),o.getString("ID"),o.getString("nick"),o.getString("Surname"),Boolean.getBoolean(o.getString("su")=="0"?"false":"true"));
                    Log.e("cos",usr.getMail());
                    TextView t= (TextView) header.findViewById(R.id.nav_name);
                    TextView e= (TextView) header.findViewById(R.id.nav_email);
                    t.setText(o.getString("Name")+" "+o.getString("Surname")+" ("+o.getString("nick")+")");
                    e.setText(o.getString("email"));
                    Log.e("id",o.getString("email"));
                    if(o.getString("su").contentEquals("1")){
                        t.setTextColor(Color.RED);
                    }
                    if(usr.isSu()){

                    }
                } catch (JSONException e) {
                    Log.e("json",e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("blad",error.toString());
                //foo[0] =null;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> h = new HashMap<>();
                h.put("ID",idd);

                return h;
            }
        };
        q.add(stringRequest);

        q.start();

        Log.e("r", id);

    }
}
