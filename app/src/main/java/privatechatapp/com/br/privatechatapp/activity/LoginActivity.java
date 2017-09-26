package privatechatapp.com.br.privatechatapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Random;
import privatechatapp.com.br.privatechatapp.R;
import privatechatapp.com.br.privatechatapp.config.ConfiguracaoFirebase;
import privatechatapp.com.br.privatechatapp.helper.Permissao;
import privatechatapp.com.br.privatechatapp.helper.Preferencias;
import privatechatapp.com.br.privatechatapp.model.Usuario;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.telephony.SmsManager;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    private DatabaseReference referenciaFirebase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar = findViewById(R.id.bt_logar);



        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = new Usuario();

                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                if((email.getText().length()) > 0 && (senha.getText().length() > 0)){
                    validarLogin();
                }else{
                    if(email.getText().length() == 0){
                        email.setError("Favor informar o e-mail");
                    }
                    if(senha.getText().length() == 0){
                        senha.setError("Favor informar a senha");
                    }
                }
            }
        });
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() !=  null){
            abrirTelaPrincipal();
        }
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao realizar  o login!!", Toast.LENGTH_LONG).show();
                }else{

                    String erroExcecao = " ";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Senha digitada é inválida";
                    } catch (FirebaseAuthInvalidUserException e) {
                        erroExcecao = "E-mail não existe em nosso banco de dados ou foi desativado!!";
                    } catch (Exception e) {

                        erroExcecao = "Erro ao efetuar o login";
                    }

                    Toast.makeText(LoginActivity.this, "ERRO: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastrarUsuarioActivity.class);
        startActivity(intent);
    }

}

