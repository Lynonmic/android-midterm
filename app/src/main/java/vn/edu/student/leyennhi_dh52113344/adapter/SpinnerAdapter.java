package vn.edu.student.leyennhi_dh52113344.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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

public class SpinnerAdapter extends ArrayAdapter<PhongBan> {
    private Activity context;
    private int res;
    private List<PhongBan> list;

    public SpinnerAdapter(@NonNull Activity context, int resource, @NonNull List<PhongBan> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.res, null);

        PhongBan phongBan = list.get(position);
        TextView spn_category = view.findViewById(R.id.spn_Pb);
        spn_category.setText(String.valueOf(phongBan.getTenPhongBan()));


        return view;
    }
}
