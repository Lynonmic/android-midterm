package vn.edu.student.leyennhi_dh52113344.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.edu.student.leyennhi_dh52113344.R;
import vn.edu.student.leyennhi_dh52113344.dbhelper.DBHelper;
import vn.edu.student.leyennhi_dh52113344.model.NhanVien;
import vn.edu.student.leyennhi_dh52113344.model.PhongBan;

public class NhanVienAdapter extends ArrayAdapter<NhanVien> {
    DBHelper helper;
    private Activity context;
    private int res;
    private List<NhanVien> nhanVienList;

    public NhanVienAdapter(@NonNull Activity context, int resource, @NonNull List<NhanVien> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res = resource;
        this.nhanVienList = objects;
        helper = new DBHelper(context);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.res, null);

        TextView tvId = view.findViewById(R.id.txtMaNv);
        TextView tvName = view.findViewById(R.id.txtTenNv);
        TextView tvphongBan = view.findViewById(R.id.txtPhongBan);
        ImageView ivNhanVien = view.findViewById(R.id.imgNv);

        NhanVien currentNhanVien = nhanVienList.get(position);

        tvId.setText(String.format("%s %d", tvId.getText().toString(), currentNhanVien.getManv()));
        tvName.setText(String.format("%s %s", tvName.getText().toString(), currentNhanVien.getTennv()));
        PhongBan phongBan = helper.getPhongBanById(currentNhanVien.getMaphongban());
        if(phongBan != null){
            tvphongBan.setText(String.format("%s %s", tvphongBan.getText().toString(), phongBan.getTenPhongBan()));
        }else {
            tvphongBan.setText("Không tìm thấy");
        }

        if (currentNhanVien.getHinhanh() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentNhanVien.getHinhanh());
            ivNhanVien.setImageBitmap(bitmap);
        } else {
            ivNhanVien.setImageResource(R.drawable.image_default);
        }

        return view;
    }
}
