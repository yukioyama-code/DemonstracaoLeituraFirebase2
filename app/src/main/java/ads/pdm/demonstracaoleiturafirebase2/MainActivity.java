package ads.pdm.demonstracaoleiturafirebase2;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText txtNome = findViewById(R.id.txtNome);
        EditText txtSobrenome = findViewById(R.id.txtSobrenome);
        EditText txtIdade = findViewById(R.id.txtIdade);
        Button btnEnviar = findViewById(R.id.btnEnviar);
        Button btnListar = findViewById(R.id.btnListar);

        DatabaseReference bd = FirebaseDatabase.getInstance().getReference();

        btnEnviar.setOnClickListener(view -> {
            String nome = txtNome.getText().toString();
            String sobrenome = txtSobrenome.getText().toString();
            int idade = Integer.parseInt(txtIdade.getText().toString());
            Usuario usuario = new Usuario(nome, sobrenome, idade);
            DatabaseReference dados = bd.child("dados");
            String chave = dados.push().getKey();
            dados.child(chave).setValue(usuario);
        });

        btnListar.setOnClickListener(view -> {
            DatabaseReference dados = bd.child("dados");
            dados.addValueEventListener(new EscutadorFirebase());
        });
    }

    private class EscutadorFirebase implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                String nome, sobrenome;
                int idade;
                for (DataSnapshot dataSnapshotUsuario : dataSnapshot.getChildren()) {
                    Usuario usuario = dataSnapshotUsuario.getValue(Usuario.class);
                    nome = usuario.getNome();
                    sobrenome = usuario.getSobrenome();
                    idade = usuario.getIdade();
                    Toast.makeText(MainActivity.this, "Nome: " + nome + "\nSobrenome: " + sobrenome + "\nIdade: " + idade, Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    }
}



