package vn.edu.student.leyennhi_dh52113344;

import static vn.edu.student.leyennhi_dh52113344.R.string.NotGrantPermission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import vn.edu.student.leyennhi_dh52113344.adapter.SpinnerAdapter;
import vn.edu.student.leyennhi_dh52113344.dbhelper.DBHelper;
import vn.edu.student.leyennhi_dh52113344.model.NhanVien;
import vn.edu.student.leyennhi_dh52113344.model.PhongBan;

public class Customize_Adding extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    PhongBan selectedPhongBan;
    List<PhongBan> phongBan;
    private EditText edtTennv;
    private Spinner spinner;
    private ImageView ivImage;
    private Button btnThemAnh, btnThemNV, btnChinhSua;
    private String filePath = "";
    ActivityResultLauncher<Intent> imagePicker = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                filePath = getFilePathFromUri(selectedImageUri);
                                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                                ivImage.setImageBitmap(bitmap);
                            }
                        }
                    }
                }
            }
    );
    private DBHelper dbHelper;
    private NhanVien nv;
    private int PERMISSION_REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customize_adding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        getIntentData();
        addEvents();
        checkStoragePermission();
    }
    private void getIntentData() {
        Intent intent = getIntent();
        nv = (NhanVien) intent.getSerializableExtra("nhanVien");
        if (nv != null) {
            edtTennv.setText(nv.getTennv());
            Bitmap bitmap = BitmapFactory.decodeFile(nv.getHinhanh());
            ivImage.setImageBitmap(bitmap);
        } else {
            btnChinhSua.setEnabled(false);
        }

        if (nv != null) {
            for (int i = 0; i < phongBan.size(); i++) {
                if (phongBan.get(i).getMaphongban() == nv.getMaphongban()) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void addEvents() {
        btnThemAnh.setOnClickListener(v -> moLuaChonAnh());

        btnChinhSua.setOnClickListener(v -> chinhSuaNhanVien(false));
        btnThemNV.setOnClickListener(v -> chinhSuaNhanVien(true));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {

                    selectedPhongBan = (PhongBan) adapterView.getItemAtPosition(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedPhongBan = (PhongBan) adapterView.getItemAtPosition(0);
            }
        });
    }

    private void addControls() {
        dbHelper = new DBHelper(Customize_Adding.this);
        edtTennv = findViewById(R.id.edtTenNV);
        spinner = findViewById(R.id.spinnerPB);
        ivImage = findViewById(R.id.ivNV);
        btnThemAnh = findViewById(R.id.btnThemAnh);
        btnChinhSua = findViewById(R.id.btnChinhSua);
        btnThemNV = findViewById(R.id.btnthemNhanvien);

        phongBan = dbHelper.getAllPhongBan();

        SpinnerAdapter adapter = new SpinnerAdapter(
                Customize_Adding.this,
                R.layout.spinner_layout,
                phongBan
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void moLuaChonAnh() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePicker.launch(intent);
    }

    private void chinhSuaNhanVien(boolean add) {
        String name = edtTennv.getText().toString().trim();
        if (selectedPhongBan == null) {
            selectedPhongBan = (PhongBan) spinner.getSelectedItem();
        }
        int idphongBan = selectedPhongBan.getMaphongban();
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.Information_Empty, Toast.LENGTH_SHORT).show();
            return;
        }

        NhanVien nvien = new NhanVien();
        nvien.setMaphongban(idphongBan);
        nvien.setTennv(name);
        if (!filePath.isEmpty()) {
           nvien.setHinhanh(filePath);
        } else if (!(nv == null)) {
            nvien.setHinhanh(nv.getHinhanh());
        }

        int responseCode = NhanVienList.REQUEST_ADD_NHANVIEN;
        if (nv != null && !add) {
            nvien.setManv(nv.getManv());
            responseCode = NhanVienList.REQUEST_EDIT_NHANVIEN;
        }
        Intent intent = new Intent();
        intent.putExtra("nhanVien", nvien);
        setResult(responseCode, intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(
                R.menu.menu_main,
                menu
        );

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnu_nhanvien) {
            Intent intent = new Intent(getApplicationContext(), NhanVienList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else if (item.getItemId() == R.id.mnu_phongban) {
            Intent intent = new Intent(getApplicationContext(), PhongBanList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else if (item.getItemId() == R.id.mnu_chitiet) {
            Intent intent = new Intent(getApplicationContext(),ChiTiet.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private String getFilePathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return "";
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Customize_Adding.this,
                    new String[]{android.Manifest.permission.MANAGE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_MEDIA_IMAGES},
                    PERMISSION_REQUEST_CODE);
        }
    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                Toast.makeText(this, NotGrantPermission, Toast.LENGTH_SHORT).show();
            }
        }
    }


}