package vn.edu.student.leyennhi_dh52113344;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import vn.edu.student.leyennhi_dh52113344.adapter.PhongBanAdapter;
import vn.edu.student.leyennhi_dh52113344.dbhelper.DBHelper;
import vn.edu.student.leyennhi_dh52113344.dbhelper.DBHelper;
import vn.edu.student.leyennhi_dh52113344.model.NhanVien;
import vn.edu.student.leyennhi_dh52113344.model.PhongBan;

public class PhongBanList extends AppCompatActivity {

    Toolbar toolbar;
    DBHelper dbhelper;
    private ListView lv_phongban;
    private Button btnThem, btnCapNhat;
    private PhongBanAdapter adapter;
    private List<PhongBan> phongban_list;
    private PhongBan selected_phongBan = null;
    private EditText edtTenPB;
    private List<NhanVien> dsnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phong_ban_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
       addEvents();
    }
    private void addControls() {
        toolbar = findViewById(R.id.toolNV);
        setSupportActionBar(toolbar);
        dbhelper = new DBHelper(PhongBanList.this);
        phongban_list = dbhelper.getAllPhongBan();
        adapter = new PhongBanAdapter(PhongBanList.this, R.layout.phongban_item, phongban_list);
        lv_phongban = findViewById(R.id.lvDspb);
        lv_phongban.setAdapter(adapter);
        btnThem = findViewById(R.id.btnthemPB);
        btnCapNhat = findViewById(R.id.btnCapNhatPB);
        edtTenPB = findViewById(R.id.edtTenPB);
    }

    private void addEvents() {
        lv_phongban.setOnItemClickListener((parent, view, position, id) -> {
            CapNhatThongTin(position);
            btnCapNhat.setEnabled(true);
        });

        btnThem.setOnClickListener(v -> xuLyThem());

        btnCapNhat.setOnClickListener(v -> {
            if (selected_phongBan != null) {
                xuLyCapNhat(selected_phongBan);
            } else {
                Toast.makeText(this, R.string.InvalidInformation, Toast.LENGTH_SHORT).show();
            }
        });
        lv_phongban.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selected_phongBan != null) {
                    xacNhanXoaNhanVien(selected_phongBan);
                    return true;
                } else {
                    Toast.makeText(PhongBanList.this, R.string.DeleteFail, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        lv_phongban.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dsnv = dbhelper.getAllNhanVien();
                PhongBan phongBan = phongban_list.get(i);
                if (i >= 0 && i < phongban_list.size()) {
                    for(NhanVien nv: dsnv) {
                        if(nv.getManv() >0 && phongBan.getMaphongban() >0 )
                        {
                            if( nv.getMaphongban() == phongBan.getMaphongban()) {
                                Toast.makeText(PhongBanList.this, R.string.CanNotDelete, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }
                    }
                    xacNhanXoaNhanVien(phongBan);
                }
                return true;
            }
        });

        btnCapNhat.setEnabled(false);
    }

    private void xacNhanXoaNhanVien(final PhongBan phongBan) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.deleteTitle)
                .setMessage(R.string.ConfirmDelete)
                .setPositiveButton(R.string.Yes , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long result = dbhelper.deletePhongBan(phongBan.getMaphongban());

                        if (result > 0) {
                            phongban_list.remove(phongBan);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.DeleteFail, Toast.LENGTH_SHORT).show();
                        }
                        updateText("");
                    }
                })
                .setNegativeButton(R.string.Cancel, null)
                .show();
    }

    private void CapNhatThongTin(int position) {
        if (position >= 0 & position < phongban_list.size()) {
            selected_phongBan = phongban_list.get(position);
            updateText(selected_phongBan.getTenPhongBan());
        }
    }


    private void xuLyCapNhat(PhongBan pb) {
        String name = edtTenPB.getText().toString();
        pb.setTenPhongBan(name);
        dbhelper.updatePhongBan(pb);
        updateText("");
        display();
    }


    private void xuLyThem() {
        String name = edtTenPB.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.Information_Empty, Toast.LENGTH_SHORT).show();
            return;
        }
        PhongBan phongBan = new PhongBan();
        phongBan.setTenPhongBan(name);
        dbhelper.addCategory(phongBan);
        updateText("");
        display();
    }

    private void display() {
        phongban_list = dbhelper.getAllPhongBan();
        adapter = new PhongBanAdapter(PhongBanList.this, R.layout.phongban_item, phongban_list);
        lv_phongban = findViewById(R.id.lvDspb);
        lv_phongban.setAdapter(adapter);
    }

    private void updateText(String text) {
        edtTenPB.setText(text);
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
}