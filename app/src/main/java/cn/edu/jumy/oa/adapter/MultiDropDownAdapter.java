package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.R;


public class MultiDropDownAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private int checkItemPosition = 0;

    public List<Integer> checkedList = new ArrayList<>();

    public void setCheckItem(int position) {
        checkItemPosition = position;
        if (!checkedList.contains(checkItemPosition)) {
            checkedList.add(checkItemPosition);
        } else {
            checkedList.remove(Integer.valueOf(checkItemPosition));
        }
        notifyDataSetChanged();
    }
    public void setSingleCheckItem(int position) {
        checkItemPosition = position;
        checkedList.clear();
        checkedList.add(checkItemPosition);
        notifyDataSetChanged();
    }

    public MultiDropDownAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_multi_drop_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.mText.setText(list.get(position));
        if (checkItemPosition != -1) {
            if (checkedList.contains(position)) {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.pressed));
                viewHolder.mText.setBackgroundResource(R.drawable.check_bg);
            } else {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
                viewHolder.mText.setBackgroundResource(R.drawable.uncheck_bg);
            }
        }
    }

    public String getNeedString() {
        if (checkedList.size() == 0) {
            return "请选择";
        } else if ((checkedList.size() == 1)) {
            return list.get(checkedList.get(0));
        } else {
            return list.get(checkedList.get(0)) + "等";
        }
    }

    static class ViewHolder {
        TextView mText;

        ViewHolder(View view) {
            mText = (TextView) view.findViewById(R.id.text);
        }
    }
}
