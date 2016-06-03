package cn.edu.jumy.oa.timchat.model;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.tencent.TIMConversationType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.edu.jumy.oa.timchat.ui.customview.CircleImageView;
import okhttp3.Call;

/**
 * 会话数据
 */
public abstract class Conversation implements Comparable {

    //会话对象id
    protected String identify;

    //会话类型
    protected TIMConversationType type;

    //会话对象名称
    protected String name;


    /**
     * 获取最后一条消息的时间
     */
    abstract public long getLastMessageTime();

    /**
     * 获取未读消息数量
     */
    abstract public long getUnreadNum();


    /**
     * 将所有消息标记为已读
     */
    abstract public void readAllMessage();


    /**
     * 获取头像
     */
    abstract public int getAvatar();

    /**
     * 设置聊天对象的头像
     */
    public void setAvatarByUrl(final CircleImageView avatar){
        OkHttpUtils.get()
                .url("http://192.168.1.16/ssm/upload.do")
                .addParams("","")
                .addParams("","")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        avatar.setImageResource(getAvatar());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Picasso.with(avatar.getContext())
                                .load(response)
                                .into(avatar);
                    }
                });
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    abstract public void navToDetail(Context context);

    /**
     * 获取最后一条消息摘要
     */
    abstract public String getLastMessageSummary();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentify(){
        return identify;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        if (!identify.equals(that.identify)) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = identify.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }


    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(Object another) {
        if (another instanceof Conversation){
            Conversation anotherConversation = (Conversation) another;
            long timeGap = anotherConversation.getLastMessageTime() - getLastMessageTime();
            if (timeGap > 0) return  1;
            else if (timeGap < 0) return -1;
            return 0;
        }else{
            throw new ClassCastException();
        }
    }



}
