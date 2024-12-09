package vn.edu.student.leyennhi_dh52113344;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import vn.edu.student.leyennhi_dh52113344.dbhelper.DBHelper;
import vn.edu.student.leyennhi_dh52113344.model.NhanVien;
import vn.edu.student.leyennhi_dh52113344.adapter.NhanVienAdapter;

public class NhanVienList extends AppCompatActivity {
    public static final int REQUEST_ADD_NHANVIEN = 1;
    public static final int REQUEST_EDIT_NHANVIEN = 2;
    Toolbar toolbar;
    NhanVien selectedNhanVien = null;
    private ListView nhanVienListView;
    private NhanVienAdapter nhanVienAdapter;
    private List<NhanVien> nhanVienList;
    private Button btnAddNhanVien;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nhan_vien_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        loadNhanVien();
        addEvents();
    }
    private void addControls() {
        toolbar = findViewById(R.id.toolNV);
        setSupportActionBar(toolbar);
        nhanVienListView = findViewById(R.id.lvDsnv);
        btnAddNhanVien = findViewById(R.id.btnthemNV);
        dbHelper = new DBHelper(NhanVienList.this);
        nhanVienList = dbHelper.getAllNhanVien();
        nhanVienAdapter = new NhanVienAdapter(NhanVienList.this, R.layout.nhanvien_stylelist, nhanVienList);
        nhanVienListView.setAdapter(nhanVienAdapter);
    }

    private void addEvents() {
        btnAddNhanVien.setOnClickListener(view -> xuLyThem());
        nhanVienListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNhanVien = nhanVienList.get(i);
                xuLyCapNhat();
            }
        });
        nhanVienListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNhanVien = nhanVienList.get(i);
                xacNhanNhanVien(selectedNhanVien);
                return true;
            }
        });
    }

    private void xuLyCapNhat() {
        if (dbHelper.getAllPhongBan().isEmpty()) {
            Toast.makeText(NhanVienList.this, R.string.DepartmentNotExists, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent editIntent = new Intent(NhanVienList.this, Customize_Adding.class);
        editIntent.putExtra("nhanVien", selectedNhanVien);
        launcher.launch(editIntent);
    }

    private void xuLyThem() {
        if (dbHelper.getAllPhongBan().isEmpty()) {
            Toast.makeText(NhanVienList.this, R.string.DepartmentNotExists, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(NhanVienList.this, Customize_Adding.class);
        launcher.launch(intent);
    }

    private void loadNhanVien() {
        nhanVienList = dbHelper.getAllNhanVien();
        nhanVienAdapter = new NhanVienAdapter(NhanVienList.this, R.layout.nhanvien_stylelist, nhanVienList);
        nhanVienListView.setAdapter(nhanVienAdapter);
    }

    private void xacNhanNhanVien(final NhanVien nhanVien) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.deleteTitle)
                .setMessage(R.string.ConfirmDelete)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long result = dbHelper.deleteNhanVien(nhanVien.getManv());

                        if (result > 0) {
                            nhanVienList.remove(nhanVien);
                            nhanVienAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton(R.string.Cancel, null)
                .show();
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
        if(item.getItemId() == R.id.mnu_nhanvien){
            Intent intent = new Intent(getApplicationContext(), NhanVienList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (item.getItemId() == R.id.mnu_phongban) {
            Intent intent = new Intent(getApplicationContext(), PhongBanList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (item.getItemId() == R.id.mnu_chitiet) {
            Intent intent = new Intent(getApplicationContext(), ChiTiet.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == REQUEST_ADD_NHANVIEN) {
                        NhanVien nhanVien = (NhanVien) o.getData().getSerializableExtra("nhanVien");
                        long result = dbHelper.addNhanVien(nhanVien);
                        if (result > 0) {
                            Toast.makeText(getApplicationContext(), R.string.InsertEmployeeSuccessfull, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.InsertEmployeeFail, Toast.LENGTH_SHORT).show();
                        }

                    } else if (o.getResultCode() == REQUEST_EDIT_NHANVIEN) {
                        NhanVien nhanVien = (NhanVien) o.getData().getSerializableExtra("nhanVien");
                        dbHelper.updateNhanVien(nhanVien);
                    }
                    loadNhanVien();
                }
            }
    );
}