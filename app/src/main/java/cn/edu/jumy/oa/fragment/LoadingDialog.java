package cn.edu.jumy.oa.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.edu.jumy.oa.R;

public class LoadingDialog extends DialogFragment {

    public static final String TAG = "LoadingDialog";
    private String mMsg = "加载中...";

    public void setMsg(String msg) {
        this.mMsg = msg;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_loading, null);
        TextView title = (TextView) view.findViewById(R.id.id_dialog_loading_msg);
        title.setText(mMsg);
        Dialog dialog = new Dialog(getActivity(), R.style.loadingDialog);
        dialog.setContentView(view);
        return dialog;
    }
}