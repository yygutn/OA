package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Utils.CardGenerator;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.bean.Relay;


public class RelayAdapter extends CommonAdapter<Relay> {
    public RelayAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Relay node) {
        ((CardView) holder.getView(R.id.cardView)).setCardBackgroundColor(Color.parseColor("#30DDDDDD"));
        holder.setText(R.id.subtitle, node.docTitle);
        Node temp = new Node(node);
        holder.setText(R.id.supportingText, CardGenerator.getContentString(temp));
    }
    public void setList(ArrayList<Relay> list){
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }
}
