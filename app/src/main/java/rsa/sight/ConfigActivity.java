package rsa.sight;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigActivity extends AppCompatActivity {
    EditText server, port, username, password, dbname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        server = (EditText) findViewById(R.id.ipCampo);
        port = (EditText) findViewById(R.id.portCampo);
        username = (EditText) findViewById(R.id.db_userCampo);
        password = (EditText) findViewById(R.id.db_passCampo);
        dbname = (EditText) findViewById(R.id.db_nameCampo);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.preferences), MODE_PRIVATE);
        server.setText(prefs.getString("ip_server", getString(R.string.ip_server)));
        port.setText(prefs.getString("port_server", getString(R.string.port_server)));
        dbname.setText(prefs.getString("database", getString(R.string.database)));
        username.setText(prefs.getString("username", getString(R.string.username)));
        password.setText(prefs.getString("password", getString(R.string.password)));
    }

    public void aceptar(View v) {
        if (TextUtils.isEmpty(server.getText())) {

            Toast mensaje = Toast.makeText(getApplicationContext(), getString(R.string.error_ip_requerido), Toast.LENGTH_SHORT);
            mensaje.show();
            server.setText("");
        }else{
            if (TextUtils.isEmpty(username.getText())) {

                Toast mensaje = Toast.makeText(getApplicationContext(), getString(R.string.error_db_user_requerido), Toast.LENGTH_SHORT);
                mensaje.show();
                username.setText("");
            }else{
                if (TextUtils.isEmpty(password.getText())) {

                    Toast mensaje = Toast.makeText(getApplicationContext(), getString(R.string.error_db_pass_requerido), Toast.LENGTH_SHORT);
                    mensaje.show();
                    password.setText("");
                }else{
                    if (TextUtils.isEmpty(dbname.getText())) {

                        Toast mensaje = Toast.makeText(getApplicationContext(), getString(R.string.error_db_name_requerido), Toast.LENGTH_SHORT);
                        mensaje.show();
                        dbname.setText("");
                    }else{
                        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preferences), MODE_PRIVATE).edit();
                        editor.putString("ip_server", server.getText().toString());
                        editor.putString("port_server", port.getText().toString());
                        editor.putString("database", dbname.getText().toString());
                        editor.putString("username", username.getText().toString());
                        editor.putString("password", password.getText().toString());
                        editor.apply();
                        finish();
                    }
                }
            }
        }
    }
}
