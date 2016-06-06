package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import java.util.List;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/6/6  下午5:46
 */
public class StringAdapter extends CommonAdapter<String> {
    public StringAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(android.R.id.text1,s);
        holder.setTextColor(android.R.id.text1, Color.WHITE);
    }
}
