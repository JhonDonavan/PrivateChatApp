package privatechatapp.com.br.privatechatapp.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import privatechatapp.com.br.privatechatapp.R;
import privatechatapp.com.br.privatechatapp.config.ConfiguracaoFirebase;
import privatechatapp.com.br.privatechatapp.model.Usuario;

public class CadastrarUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;
    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        nome = findViewById(R.id.edit_cadastrar_nome);
        email = findViewById(R.id.edit_cadastrar_email);
        senha = findViewById(R.id.edit_cadastrar_senha);
        botaoCadastrar = findViewById(R.id.btn_cadastrar_usuario);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                if((nome.getText().length() > 0) && (email.getText().length() > 0) && (senha.getText().length() > 0)){
                    cadastrarUsuario();
                }else{
                    if(nome.getText().length() == 0){
                        nome.setError("Favor digite um nome de usuário");
                    }
                    if(email.getText().length() == 0){
                        email.setError("Favor digite um e-mail");
                    }
                    if(senha.getText().length() == 0){
                        senha.setError("Favor digite uma senha de acesso");
                    }
                }

            }
        });
    }

    public void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastrarUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastrarUsuarioActivity.this, "Usuário cadastrado com sucesso!!", Toast.LENGTH_LONG).show();

                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvar();

                    autenticacao.signOut();
                    finish();

                }else{

                    String erroExcecao = " ";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte, contendo mais caracteres com letras e números";
                        senha.setError("Favor digitar uma senha mais forte");
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail  digitado é inválido.";
                        email.setError("O e-mail digitado é inválido");
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "O e-mail já está em uso no app";
                        email.setError("O e-mail já está em uso no app");
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                    }

                    Toast.makeText(CadastrarUsuarioActivity.this, "Erro: " + erroExcecao ,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

