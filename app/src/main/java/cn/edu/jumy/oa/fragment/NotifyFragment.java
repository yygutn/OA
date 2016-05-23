package cn.edu.jumy.oa.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.card.action.WelcomeButtonAction;
import com.dexafree.materialList.card.provider.ListCardProvider;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.edu.jumy.oa.R;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by Jumy on 16/5/19 12:07.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 * *****************************************************
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #                佛祖保佑         永无BUG
 * #                                                   #
 * *****************************************************
 */
@EFragment(R.layout.fragment_notify)
public class NotifyFragment extends Fragment {
    private Context mContext;
    @ViewById(R.id.notify_listView)
    MaterialListView mListView;

    @AfterViews
    void start(){
        mContext = getActivity();

        // Bind the MaterialListView to a variable
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);

        final ImageView emptyView = (ImageView) getActivity().findViewById(R.id.imageView);
        emptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mListView.setEmptyView(emptyView);
        Picasso.with(mContext)
                .load("https://www.skyverge.com/wp-content/uploads/2012/05/github-logo.png")
                .resize(100, 100)
                .centerInside()
                .into(emptyView);

        // Fill the array withProvider mock content
        fillArray();

        // Set the dismiss listener
        mListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull Card card, int position) {
                // Show a toast
                Toast.makeText(mContext, "You have dismissed a " + card.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the ItemTouchListener
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int position) {
                Log.d("CARD_TYPE", "" + card.getTag());
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {
                Log.d("LONG_CLICK", "" + card.getTag());
            }
        });
    }




    private void fillArray() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            cards.add(getRandomCard(4));
        }
        mListView.getAdapter().addAll(cards);
    }

    private Card getRandomCard(final int position) {
        String title = "Card number " + (position + 1);
        String description = "Lorem ipsum dolor sit amet";

        switch (position % 7) {
            case 0: {
                return new Card.Builder(mContext)
                        .setTag("SMALL_IMAGE_CARD")
                        .setDismissible()
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_small_image_card)
                        .setTitle(title)
                        .setDescription(description)
                        .setDrawable(R.drawable.sample_android)
                        .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                            @Override
                            public void onImageConfigure(@NonNull final RequestCreator requestCreator) {
                                requestCreator.rotate(position * 90.0f)
                                        .resize(150, 150)
                                        .centerCrop();
                            }
                        })
                        .endConfig()
                        .build();
            }
            case 1: {
                return new Card.Builder(mContext)
                        .setTag("BIG_IMAGE_CARD")
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_big_image_card_layout)
                        .setTitle(title)
                        .setSubtitle(description)
                        .setSubtitleGravity(Gravity.END)
                        .setDrawable("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png")
                        .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                            @Override
                            public void onImageConfigure(@NonNull final RequestCreator requestCreator) {
                                requestCreator.rotate(position * 45.0f)
                                        .resize(200, 200)
                                        .centerCrop();
                            }
                        })
                        .endConfig()
                        .build();
            }
            case 2: {
                final CardProvider provider = new Card.Builder(mContext)
                        .setTag("BASIC_IMAGE_BUTTON_CARD")
                        .setDismissible()
                        .withProvider(new CardProvider<>())
                        .setLayout(R.layout.material_basic_image_buttons_card_layout)
                        .setTitle(title)
                        .setTitleGravity(Gravity.END)
                        .setDescription(description)
                        .setDescriptionGravity(Gravity.END)
                        .setDrawable(R.drawable.dog)
                        .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                            @Override
                            public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                                requestCreator.fit();
                            }
                        })
                        .addAction(R.id.left_text_button, new TextViewAction(mContext)
                                .setText("确认")
                                .setTextResourceColor(R.color.black_button)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                                        card.getProvider().setTitle("CHANGED ON RUNTIME");
                                    }
                                }))
                        .addAction(R.id.right_text_button, new TextViewAction(mContext)
                                .setText("删除卡片")
                                .setTextResourceColor(R.color.orange_button)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "You have pressed the right button on card " + card.getProvider().getTitle(), Toast.LENGTH_SHORT).show();
                                        card.dismiss();
                                    }
                                }));

                if (position % 2 == 0) {
                    provider.setDividerVisible(true);
                }

                return provider.endConfig().build();
            }
            case 3: {
                final CardProvider provider = new Card.Builder(mContext)
                        .setTag("BASIC_BUTTONS_CARD")
                        .setDismissible()
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_basic_buttons_card)
                        .setTitle(title)
                        .setDescription(description)
                        .addAction(R.id.left_text_button, new TextViewAction(mContext)
                                .setText("确认")
                                .setTextResourceColor(R.color.black_button)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                                    }
                                }))
                        .addAction(R.id.right_text_button, new TextViewAction(mContext)
                                .setText("*>_<*")
                                .setTextResourceColor(R.color.accent_material_dark)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "You have pressed the right button", Toast.LENGTH_SHORT).show();
                                    }
                                }));

                if (position % 2 == 0) {
                    provider.setDividerVisible(true);
                }

                return provider.endConfig().build();
            }
            case 4: {
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String time=   sdf.format( new  Date());
                final CardProvider provider = new Card.Builder(mContext)
                        .setTag("WELCOME_CARD")
                        .setDismissible()
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_welcome_card_layout)
                        .setTitle("通知")
                        .setTitleColor(Color.WHITE)
                        .setDescription(time+"在"+new Random().nextInt(1000)+"开会")
                        .setDescriptionColor(Color.WHITE)
                        .setSubtitle("会议")
                        .setSubtitleColor(Color.WHITE)
                        .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                        .addAction(R.id.ok_button, new WelcomeButtonAction(mContext)
                                .setText("未读!")
                                .setTextColor(Color.WHITE)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        try {
                                            Toast.makeText(mContext, "Welcome!", Toast.LENGTH_SHORT).show();
                                            WelcomeButtonAction action = (WelcomeButtonAction) card.getProvider().getAction(R.id.ok_button);
                                            TextView textView = (TextView) view;
                                            Drawable drawable = textView.getCompoundDrawables()[0];
                                            drawable.setColorFilter(action.getTextColor(), PorterDuff.Mode.SRC_IN);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                            textView.setText("已读");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }));
//
//                if (new Random().nextInt(100) % 3 == 0) {
//                    provider.setBackgroundResourceColor(android.R.color.background_dark);
//                    provider.addAction(R.id.ok_button, new WelcomeButtonAction(mContext)
//                    .setText("已读")
//                    .setTextColor(Color.WHITE));
//                }
                return provider.endConfig().build();
            }
            case 5: {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1);
                adapter.add("Hello");
                adapter.add("World");
                adapter.add("!");

                return new Card.Builder(mContext)
                        .setTag("LIST_CARD")
                        .setDismissible()
                        .withProvider(new ListCardProvider())
                        .setLayout(R.layout.material_list_card_layout)
                        .setTitle("List Card")
                        .setDescription("Take a list")
                        .setAdapter(adapter)
                        .endConfig()
                        .build();
            }
            default: {
                final CardProvider provider = new Card.Builder(mContext)
                        .setTag("BIG_IMAGE_BUTTONS_CARD")
                        .setDismissible()
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_image_with_buttons_card)
                        .setTitle(title)
                        .setDescription(description)
                        .setDrawable(R.drawable.photo)
                        .addAction(R.id.left_text_button, new TextViewAction(mContext)
                                .setText("add card")
                                .setTextResourceColor(R.color.black_button)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Log.d("ADDING", "CARD");

                                        mListView.getAdapter().add(generateNewCard());
                                        Toast.makeText(mContext, "Added new card", Toast.LENGTH_SHORT).show();
                                    }
                                }))
                        .addAction(R.id.right_text_button, new TextViewAction(mContext)
                                .setText("right button")
                                .setTextResourceColor(R.color.accent_material_dark)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "You have pressed the right button", Toast.LENGTH_SHORT).show();
                                    }
                                }));

                if (position % 2 == 0) {
                    provider.setDividerVisible(true);
                }

                return provider.endConfig().build();
            }
        }
    }

    private Card generateNewCard() {
        return new Card.Builder(mContext)
                .setTag("BASIC_IMAGE_BUTTONS_CARD")
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_basic_image_buttons_card_layout)
                .setTitle("I'm new")
                .setDescription("I've been generated on runtime!")
                .setDrawable(R.drawable.dog)
                .endConfig()
                .build();
    }

    private void addMockCardAtStart() {
        mListView.getAdapter().addAtStart(new Card.Builder(mContext)
                .setTag("BASIC_IMAGE_BUTTONS_CARD")
                .setDismissible()
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_basic_image_buttons_card_layout)
                .setTitle("Hi there")
                .setDescription("I've been added on top!")
                .addAction(R.id.left_text_button, new TextViewAction(mContext)
                        .setText("left")
                        .setTextResourceColor(R.color.black_button))
                .addAction(R.id.right_text_button, new TextViewAction(mContext)
                        .setText("right")
                        .setTextResourceColor(R.color.orange_button))
                .setDrawable(R.drawable.dog)
                .endConfig()
                .build());
    }
}