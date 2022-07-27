package com.liftPlzz.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.liftPlzz.R;
import com.liftPlzz.activity.ChatActivity;
import com.liftPlzz.activity.HomeActivity;
import com.liftPlzz.activity.TicketListActivity;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.chatuser.ChatUser;
import com.liftPlzz.presenter.HelpPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.HelpView;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpFragment extends BaseFragment<HelpPresenter, HelpView> implements HelpView {

    SharedPreferences sharedPreferences;
    String strToken = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.itemFaq)
    RelativeLayout itemFaq;
    @BindView(R.id.itemUse)
    RelativeLayout itemUse;
    @BindView(R.id.itemContact)
    RelativeLayout itemContact;
    @BindView(R.id.itemReport)
    RelativeLayout itemReport;
    @BindView(R.id.itemCustomerSupport)
    RelativeLayout itemCustomerSupport;

    @Override
    protected int createLayout() {
        return R.layout.fragment_help;
    }

    @Override
    protected void setPresenter() {
        presenter = new HelpPresenter();
    }

    @Override
    protected HelpView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("Help & Support");
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
    }

    @OnClick({R.id.imageViewBack, R.id.itemFaq, R.id.itemUse, R.id.itemContact, R.id.itemReport,R.id.itemCustomerSupport})
    public void onViewClicked(View view) {
        HomeActivity activity = (HomeActivity) getActivity();
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.itemFaq:
                if (activity != null) {
                    activity.openFaqFragment(BaseActivity.PerformFragment.REPLACE);
                }
                break;
            case R.id.itemUse:
                if (activity != null) {
                    activity.openVideosFragment(BaseActivity.PerformFragment.REPLACE);
                }
                break;
            case R.id.itemContact:
                showCCDialog();
                break;
            case R.id.itemReport:
                startActivity(new Intent(getActivity(), TicketListActivity.class));
                break;
            case R.id.itemCustomerSupport:
                ChatUser user = new ChatUser();
                user.setId(0);
                user.setName("CharPair Support");
                user.setMobile("9876543210");
                user.setImage("https://charpair.com/website/assets/img/Char_Pair_logo.jpg");

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Constants.USER_ID, String.valueOf(user.getId()));
                intent.putExtra("charuser", new Gson().toJson(user));
                startActivity(intent);

                break;
        }
    }

    public void showCCDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_care_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        AppCompatTextView tv_number = dialog.findViewById(R.id.tv_number);
        AppCompatTextView tv_email = dialog.findViewById(R.id.tv_email);
        tv_number.setText("9876543210");
        tv_email.setText("support@charpair.com ");
        dialog.show();
    }

}
