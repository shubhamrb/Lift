package com.liftPlzz.activity;

import static com.liftPlzz.utils.Constants.NOTIFICATION_TYPE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import com.liftPlzz.R;
import com.liftPlzz.adapter.SectionPagerWalkTroughAdapter;
import com.liftPlzz.provider.AppNavigationProvider;
import com.liftPlzz.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppNavigationProvider {

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesIntro;

    @BindView(R.id.textViewSkip)
    AppCompatTextView textViewSkip;

    @BindView(R.id.layoutLine1)
    ImageView layoutLine1;

    @BindView(R.id.layoutLine2)
    ImageView layoutLine2;

    @BindView(R.id.layoutLine3)
    ImageView layoutLine3;

    @BindView(R.id.layoutLine4)
    ImageView layoutLine4;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    String referral_id = null;

    @Override
    public int getPlaceHolder() {
        return R.id.placeHolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferencesIntro =
                getSharedPreferences(Constants.SHARED_PREF_INTRO, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        NOTIFICATION_TYPE = "";
        Intent intent = getIntent();
        if (intent.getStringExtra("referral_id") != null) {
            referral_id = intent.getStringExtra("referral_id");
        }

        if (sharedPreferencesIntro.getBoolean(Constants.IS_INTRO, false)) {
            if (sharedPreferences.getBoolean(Constants.IS_LOGIN, false)) {
                if (intent.getStringExtra("id") != null) {
                    String id = intent.getStringExtra("id");
                    String type = intent.getStringExtra("type");
                    Log.e("ID", "" + id);
                    Log.e("TYPE", "" + type);
                    NOTIFICATION_TYPE = type;
                    startActivity(new Intent(MainActivity.this, HomeActivity.class)
                            .putExtra("id", id)
                            .putExtra("type", type)
                            .putExtra("referral_id", referral_id));
                } else {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class)
                            .putExtra("referral_id", referral_id));
                }
                finish();
            } else {
                startActivity(new Intent(MainActivity.this, AuthActivity.class)
                        .putExtra("referral_id", referral_id));
                finish();
            }

        } else {
            SectionPagerWalkTroughAdapter sectionPagerWalkTroughAdapter =
                    new SectionPagerWalkTroughAdapter(this.getSupportFragmentManager(), 4);
            viewpager.setAdapter(sectionPagerWalkTroughAdapter);
            // viewpager.setCurrentItem(0);
            viewpager.addOnPageChangeListener(
                    new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {
                        }

                        @Override
                        public void onPageSelected(int i) {

                            if (viewpager.getCurrentItem() == 0) {
                                layoutLine1.setImageResource(R.drawable.line_new);
                                layoutLine2.setImageResource(R.drawable.line_sel);
                                layoutLine3.setImageResource(R.drawable.line_sel);
                                layoutLine4.setImageResource(R.drawable.line_sel);
                                viewpager.setCurrentItem(0);
                            } else if (viewpager.getCurrentItem() == 1) {
                                layoutLine1.setImageResource(R.drawable.line_sel);
                                layoutLine2.setImageResource(R.drawable.line_new);
                                layoutLine3.setImageResource(R.drawable.line_sel);
                                layoutLine4.setImageResource(R.drawable.line_sel);
                                viewpager.setCurrentItem(1);
                            } else if (viewpager.getCurrentItem() == 2) {

                                layoutLine1.setImageResource(R.drawable.line_sel);
                                layoutLine2.setImageResource(R.drawable.line_sel);
                                layoutLine3.setImageResource(R.drawable.line_new);
                                layoutLine4.setImageResource(R.drawable.line_sel);

                                viewpager.setCurrentItem(2);
                            } else if (viewpager.getCurrentItem() == 3) {

                                layoutLine1.setImageResource(R.drawable.line_sel);
                                layoutLine2.setImageResource(R.drawable.line_sel);
                                layoutLine3.setImageResource(R.drawable.line_sel);
                                layoutLine4.setImageResource(R.drawable.line_new);

                                viewpager.setCurrentItem(3);
                            } else {
                                sharedPreferencesIntro
                                        .edit()
                                        .putBoolean(Constants.IS_INTRO, true)
                                        .apply();
                                startActivity(new Intent(MainActivity.this, AuthActivity.class)
                                        .putExtra("referral_id", referral_id));
                                finish();
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int i) {
                        }
                    });
        }
    }

    @OnClick({R.id.textViewSkip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewSkip:
                if (viewpager.getCurrentItem() == 0) {
                    viewpager.setCurrentItem(1);
                    layoutLine1.setImageResource(R.drawable.line_sel);
                    layoutLine2.setImageResource(R.drawable.line_new);
                    layoutLine3.setImageResource(R.drawable.line_sel);
                    layoutLine4.setImageResource(R.drawable.line_sel);

                } else if (viewpager.getCurrentItem() == 1) {
                    viewpager.setCurrentItem(2);
                    layoutLine1.setImageResource(R.drawable.line_sel);
                    layoutLine2.setImageResource(R.drawable.line_sel);
                    layoutLine3.setImageResource(R.drawable.line_new);
                    layoutLine4.setImageResource(R.drawable.line_sel);
                } else if (viewpager.getCurrentItem() == 2) {
                    viewpager.setCurrentItem(3);
                    layoutLine1.setImageResource(R.drawable.line_sel);
                    layoutLine2.setImageResource(R.drawable.line_sel);
                    layoutLine3.setImageResource(R.drawable.line_sel);
                    layoutLine4.setImageResource(R.drawable.line_new);
                } else {
                    sharedPreferencesIntro.edit().putBoolean(Constants.IS_INTRO, true).apply();
                    startActivity(new Intent(MainActivity.this, AuthActivity.class)
                            .putExtra("referral_id", referral_id));
                    finish();
                }
                break;
        }
    }
}
