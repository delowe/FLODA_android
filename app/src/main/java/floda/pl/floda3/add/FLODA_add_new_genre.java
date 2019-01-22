package floda.pl.floda3.add;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import floda.pl.floda3.R;

public class FLODA_add_new_genre extends AppCompatActivity {
    CheckBox bool_naslonecznienie, bool_temperatura, bool_podlewanie, bool_wilgotnosc, bool_poradnik;
    LinearLayout naslonecznienie, podlewanie, temperatura, wilgotnosc, poradnik;
    RadioButton pod_dni, pod_warn, sun_man, sun_sett;
    EditText pod_il_dni, sun_min, sun_max, min_temp, max_temp, min_wilg, max_wilg, link_poradnik, genre_name;
    Spinner pod_warnspin, naslonecz_spinner;
    ConstraintLayout con_naslonecznienie;
    Button send_button;
    int activated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda_add_new_genre);
        sun_min = findViewById(R.id.sun_min);
        sun_max = findViewById(R.id.sun_max);
        min_temp = findViewById(R.id.min_temp);
        max_temp = findViewById(R.id.max_temp);
        min_wilg = findViewById(R.id.min_wilg);
        max_wilg = findViewById(R.id.max_wilg);

        con_naslonecznienie = findViewById(R.id.con_naslonecznienie);
        bool_naslonecznienie = findViewById(R.id.bool_naslonecznienie);
        bool_temperatura = findViewById(R.id.bool_temperatura);
        bool_podlewanie = findViewById(R.id.bool_podlewanie);
        bool_wilgotnosc = findViewById(R.id.bool_wilgotnosc);
        bool_poradnik = findViewById(R.id.bool_poradnik);
        naslonecznienie = findViewById(R.id.naslonecznienie);
        podlewanie = findViewById(R.id.podlewanie);
        temperatura = findViewById(R.id.temperatura);
        naslonecz_spinner = findViewById(R.id.naslonecz_spinner);
        genre_name = findViewById(R.id.genre_name);
        wilgotnosc = findViewById(R.id.wilgotnosc);
        poradnik = findViewById(R.id.poradnik);
        pod_dni = findViewById(R.id.pod_dni);
        send_button = findViewById(R.id.send_button);
        pod_warn = findViewById(R.id.pod_warn);
        sun_man = findViewById(R.id.sun_man);
        sun_sett = findViewById(R.id.sun_sett);
        pod_il_dni = findViewById(R.id.pod_il_dni);
        pod_warnspin = findViewById(R.id.pod_warnspin);
        link_poradnik = findViewById(R.id.link_poradnik);
        pod_dni.setOnClickListener(v -> {
            pod_il_dni.setVisibility(View.VISIBLE);
            pod_warnspin.setVisibility(View.INVISIBLE);
        });
        pod_warn.setOnClickListener(v -> {
            pod_il_dni.setVisibility(View.INVISIBLE);
            pod_warnspin.setVisibility(View.VISIBLE);
        });
        sun_sett.setOnClickListener(v -> {
            con_naslonecznienie.setVisibility(View.INVISIBLE);
            naslonecz_spinner.setVisibility(View.VISIBLE);
        });
        sun_man.setOnClickListener(v -> {
            con_naslonecznienie.setVisibility(View.VISIBLE);
            naslonecz_spinner.setVisibility(View.INVISIBLE);
        });
        bool_naslonecznienie.setOnClickListener(v -> {
            if (bool_naslonecznienie.isChecked()) {
                naslonecznienie.setBackground(getDrawable(R.drawable.formbackgroundstrong));
                activated++;
            } else {
                naslonecznienie.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));
                activated--;

            }
            check();

        });
        bool_temperatura.setOnClickListener(v -> {
            if (bool_temperatura.isChecked()) {
                temperatura.setBackground(getDrawable(R.drawable.formbackgroundstrong));
                activated++;

            } else {
                temperatura.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));
                activated--;

            }
            check();

        });
        bool_podlewanie.setOnClickListener(v -> {
            if (bool_podlewanie.isChecked()) {
                podlewanie.setBackground(getDrawable(R.drawable.formbackgroundstrong));
                activated++;

            } else {
                podlewanie.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));
                activated--;

            }
            check();

        });
        bool_wilgotnosc.setOnClickListener(v -> {
            if (bool_wilgotnosc.isChecked()) {
                wilgotnosc.setBackground(getDrawable(R.drawable.formbackgroundstrong));
                activated++;

            } else {
                wilgotnosc.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));
                activated--;

            }
            check();
        });
        bool_poradnik.setOnClickListener(v -> {
            if (bool_poradnik.isChecked()) {
                poradnik.setBackground(getDrawable(R.drawable.formbackgroundstrong));
                activated++;

            } else {
                poradnik.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));
                activated--;

            }
            check();
        });
        send_button.setOnClickListener(v -> {
            String sql = "http://serwer1727017.home.pl/2ti/floda/add/add_genre.php?";
            if (bool_podlewanie.isChecked()) {
                if (pod_dni.isChecked()) {
                    if (pod_il_dni.getText().toString().length() > 0) {
                        sql += "podlweanie=" + pod_il_dni.getText().toString();
                    } else {
                        Toast.makeText(this, "Pusta ilosc dni w podlewaniu!", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    if (pod_warn.isChecked()) {
                        if (pod_warnspin.getSelectedItemPosition() > 2) {
                            sql += "podlweanie=w" + (pod_warnspin.getSelectedItemPosition() - 1);
                        } else {
                            Toast.makeText(this, "Nie wybrales pozycji z listy rozwijanej podlewania", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(this, "Nie ustalono w podlewaniu zasad", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            if (bool_naslonecznienie.isChecked()) {
                if (sun_man.isChecked()) {
                    if (Integer.valueOf(sun_min.getText().toString()) < Integer.valueOf(sun_max.getText().toString()) && sun_min.getText().toString() != "" && sun_max.getText().toString() != "") {
                        sql += "&naslonecznieniemin=" + sun_min.getText().toString() + "&naslonecznieniemax=" + sun_max.getText().toString();
                    } else {
                        Toast.makeText(this, "Nieprawidłowa wartość wpisana w nasloneczneniu", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    if (sun_sett.isChecked()) {
                        if (naslonecz_spinner.getSelectedItemPosition() > 0) {
                            sql += "&naslonecznienie=n" + (naslonecz_spinner.getSelectedItemPosition());
                        } else {
                            Toast.makeText(this, "Nie wybrales pozycji z listy rozwijanej naslonecznienia", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(this, "Nie ustalono w naslonecznieniu zasad", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            if (bool_temperatura.isChecked()) {
                if (Integer.valueOf(min_temp.getText().toString()) < Integer.valueOf(max_temp.getText().toString()) && min_temp.getText().toString() != "" && max_temp.getText().toString() != "") {
                    sql += "&tempmin=" + min_temp.getText().toString() + "&tempmax=" + max_temp.getText().toString();
                } else {
                    Toast.makeText(this, "Nieprawidłowa wartość wpisana w temperaturze", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (bool_wilgotnosc.isChecked()) {

                if (Integer.valueOf(min_wilg.getText().toString()) < Integer.valueOf(max_wilg.getText().toString()) && min_wilg.getText().toString() != "" && max_wilg.getText().toString() != "") {
                    sql += "&wilgmin=" + min_wilg.getText().toString() + "&wilgmax=" + max_wilg.getText().toString();
                } else {
                    Toast.makeText(this, "Nieprawidłowa wartość wpisana w wilgotnosci", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (bool_poradnik.isChecked()) {
                if (link_poradnik.getText().toString().contains("http://") && link_poradnik.getText().toString().length() > 7) {
                    sql += "&www=" + link_poradnik.getText().toString();
                } else {
                    Toast.makeText(this, "Usunieto http:// lub nic nie zostalo dopisane", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (genre_name.getText().length() > 0) {
                genre_name.setText(genre_name.getText().toString().replaceAll("[0-9]", ""));
                sql += "&name=" + genre_name.getText().toString();

            } else {
                Toast.makeText(this, "Nazwa nie moze byc pusta!", Toast.LENGTH_LONG).show();
                return;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, sql, response -> {
                Intent i = getIntent();
                i.putExtra("ID", response);
                setResult(RESULT_OK, i);
                finish();
            }, error -> {
                Log.e("error", error.toString());
            });
            RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
            q.add(stringRequest);
            q.start();

        });
    }

    void check() {
        if (activated > 0) {
            send_button.setEnabled(true);
            send_button.setBackground(getDrawable(R.drawable.formbackgroundstrong));
        } else {
            send_button.setEnabled(false);
            send_button.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));

        }
    }

}
