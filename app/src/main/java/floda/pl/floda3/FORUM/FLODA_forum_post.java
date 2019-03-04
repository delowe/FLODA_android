package floda.pl.floda3.FORUM;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import floda.pl.floda3.ListOfPlants;
import floda.pl.floda3.PlantDetail;
import floda.pl.floda3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FLODA_forum_post extends Fragment {
    TextView title, desc, date, score;
    Button profile;
    String owner_id;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout2;
    StringRequest request;
    RequestQueue q;
    FloatingActionButton fab2;

    public FLODA_forum_post() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View w = inflater.inflate(R.layout.fragment_floda_forum_post, container, false);
        Bundle bundle = this.getArguments();
        List<fData> data = new ArrayList<>();
        String ID = bundle.getString("id_post");
        fab2 = w.findViewById(R.id.comment_add);
        title = w.findViewById(R.id.title_post);
        desc = w.findViewById(R.id.desc_post);
        date = w.findViewById(R.id.date_forum);
        score = w.findViewById(R.id.score_post);
        profile = w.findViewById(R.id.name_post);
        mRecycleView = w.findViewById(R.id.for_comm_recycle);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout2 = w.findViewById(R.id.swipeRefreshLayout2);
        q = new RequestQueue(new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        String url = "http://serwer1727017.home.pl/2ti/floda/detail/forum_post.php";
        request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                data.clear();
                JSONArray array = new JSONArray(response);
                JSONObject o1 = array.getJSONObject(0);
                owner_id = o1.getString("owner_id");
                title.setText(o1.getString("title"));
                desc.setText(o1.getString("description"));
                date.setText(o1.getString("date"));
                score.setText(o1.getString("score"));
                profile.setText(o1.getString("name") + " " + o1.getString("surname"));
                for (int i = 1; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    data.add(new fData(o.getString("title"), o.getString("who_date")));
                }
                mAdapter = new ForumCRDV(data);

                mRecycleView.setAdapter(mAdapter);
                mSwipeRefreshLayout2.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("ID", ID);
                return parms;
            }
        };
        mSwipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                q.stop();
                q.add(request);
                q.start();

            }
        });
        fab2.setOnClickListener(v -> {
            LayoutInflater l4 = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            View v4 = l4.inflate(R.layout.add_comment_forum, null);
            AlertDialog alertDialog4;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(v4);
            alertDialog4 = builder.create();
            Button can = v4.findViewById(R.id.add_comment_cancel);
            Button acc = v4.findViewById(R.id.add_comment_add);
            TextView ww=v4.findViewById(R.id.add_comment_type);
            can.setOnClickListener(v1 -> {
                alertDialog4.hide();
            });
            alertDialog4.show();
            acc.setOnClickListener(v1 -> {
                String url_comm="http://serwer1727017.home.pl/2ti/floda/forum/add_comment.php";
                StringRequest comment_req=new StringRequest(Request.Method.POST,url_comm, response -> {
                    alertDialog4.hide();
                    Toast.makeText(getContext(),"Wyslano komentarz, odswiez",Toast.LENGTH_SHORT);
                },error->{

                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parms = new HashMap<>();
                        parms.put("ID_post",ID);
                        parms.put("text",ww.getText().toString());
                        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getContext());
                        parms.put("usr_id",s.getString("ID","0"));
                        return parms;
                    }
                };
                q.add(comment_req);
                q.start();
            });
        });

        q.add(request);
        q.start();
        return w;
    }

    public static class ForumCRDV extends RecyclerView.Adapter<FLODA_forum_post.ForumCRDV.MyViewHolder> {
        static List<FLODA_forum_post.fData> fpData;

        public ForumCRDV(List<FLODA_forum_post.fData> mDataset) {
            fpData = new ArrayList<>();
            this.fpData = mDataset;
        }

        @NonNull
        @Override
        public FLODA_forum_post.ForumCRDV.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forum_comment, viewGroup, false);
            return new FLODA_forum_post.ForumCRDV.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull FLODA_forum_post.ForumCRDV.MyViewHolder myViewHolder, int i) {
            myViewHolder.title.setText(fpData.get(i).title);
            myViewHolder.who_date.setText(fpData.get(i).who_date);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return fpData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView title;
            TextView who_date;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                cv = itemView.findViewById(R.id.forum_comm_cv);
                title = itemView.findViewById(R.id.comment_descc);
                who_date = itemView.findViewById(R.id.comment_who);

            }
        }
    }

    class fData {
        public String title, who_date;

        fData(String title, String who_date) {
            this.who_date = who_date;
            this.title = title;
        }

    }
}