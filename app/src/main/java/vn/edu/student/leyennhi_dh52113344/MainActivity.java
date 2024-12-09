package vn.edu.student.leyennhi_dh52113344;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText ten,mk;
    Button btnDn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        addEvents();
    }
    private void addEvents() {
        btnDn.setOnClickListener(view -> processAuth());
    }

    private void processAuth() {
        String username = ten.getText().toString();
        String password = mk.getText().toString();

        if (username.equals("admin") && password.equals("1234")) {
            Toast.makeText(MainActivity.this, R.string.login_message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, NhanVienList.class);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, R.string.LoginFail, Toast.LENGTH_SHORT).show();
        }
    }

    private void addControls() {
        ten = findViewById(R.id.edtUsername);
        mk = findViewById(R.id.edtPassword);
        btnDn = findViewById(R.id.btnDangNhap);
    }
}