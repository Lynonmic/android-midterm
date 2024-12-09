package vn.edu.student.leyennhi_dh52113344.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.edu.student.leyennhi_dh52113344.R;
import vn.edu.student.leyennhi_dh52113344.model.PhongBan;

public class PhongBanAdapter extends ArrayAdapter<PhongBan> {
    private Activity context;
    private int res;
    private List<PhongBan> phongBanList;

    public PhongBanAdapter(@NonNull Activity context, int resource, @NonNull List<PhongBan> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res = resource;
        this.phongBanList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.res, null);

        // Get references to the views in the layout
        TextView tvId = view.findViewById(R.id.tvMaPb);
        TextView tvName = view.findViewById(R.id.tvTenPb);


        // Get the current product
        PhongBan phongBan = phongBanList.get(position);

        // Set the data
        tvId.setText(String.valueOf(phongBan.getMaphongban()));
        tvName.setText(phongBan.getTenPhongBan());


        return view;
    }
}
