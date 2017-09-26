package privatechatapp.com.br.privatechatapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import privatechatapp.com.br.privatechatapp.R;
import privatechatapp.com.br.privatechatapp.helper.Preferencias;

public class ValidadorActivity extends AppCompatActivity {

    private Button botaoValidar;
    private EditText codigoValidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codigoValidar = (EditText) findViewById(R.id.edit_codigo_validar);
        botaoValidar = (Button) findViewById(R.id.edit_button_validar);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigoValidar = new MaskTextWatcher(codigoValidar, simpleMaskFormatter);

        codigoValidar.addTextChangedListener(mascaraCodigoValidar);

        botaoValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recuperar dados das preferencias do usuario
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                HashMap<String, String> usuario = new HashMap<String, String>();
                usuario = preferencias.getDadosusuario();

                String tokenValido = usuario.get("token");
                String tokenDigitado = codigoValidar.getText().toString();

                if(tokenDigitado.equals(tokenValido)){
                    Toast.makeText(ValidadorActivity.this, "TOKEN valido", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ValidadorActivity.this, "TOKEN inválido", Toast.LENGTH_LONG).show();;
                }
            }
        });




    }
}
