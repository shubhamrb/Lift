package com.liftPlzz.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.liftPlzz.R;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.ReferralDataResponse;
import com.liftPlzz.presenter.ReferPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ReferView;

import butterknife.BindView;
import butterknife.OnClick;

public class ReferFragment extends BaseFragment<ReferPresenter, ReferView> implements ReferView {

    SharedPreferences sharedPreferences;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;

    @BindView(R.id.imageViewHome)
    ImageView imageViewHome;

    @BindView(R.id.textViewCode)
    AppCompatTextView textViewCode;

    @BindView(R.id.textViewPoint)
    AppCompatTextView textViewPoint;

    @BindView(R.id.buttonGoGreen)
    AppCompatButton buttonGoGreen;


    @BindView(R.id.buttonLift)
    AppCompatButton buttonLift;

    String strToken = "";
    private ReferralDataResponse.ReferralModel referralData;

    @Override
    protected int createLayout() {
        return R.layout.fragment_refer;
    }

    @Override
    protected void setPresenter() {
        presenter = new ReferPresenter();
    }

    @Override
    protected ReferView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("Refer Friends");
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        imageViewHome.setVisibility(View.VISIBLE);
        loadData();

    }

    private void loadData() {
        presenter.getData(strToken);
    }

    @OnClick({R.id.imageViewBack, R.id.imageViewHome, R.id.buttonLift, R.id.buttonGoGreen, R.id.textViewCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.imageViewHome:
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.buttonLift:
                if (referralData != null) {
                    buildDynamicLink("https://charpair.page.link/" + referralData.getReffer_id());
                }
                break;
            case R.id.buttonGoGreen:
                presenter.openGoGreenFragment();
                break;
            case R.id.textViewCode:
                try {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("referral code", textViewCode.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), "Code copied.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void buildDynamicLink(String link) {

        String text = "Get Easy, Economic ,Comfortable Lift on CharPair every day!\n" +
                "\n" +
                "Get guaranteed 200 welcome Pair points\n" +
                "\n" +
                "Use referral code " + referralData.getReffer_id() + " & get 50 more points on your first lift.\n" +
                "\n" +
                "Enjoy eco-friendly lift anytime, anywhere with your fuel partner.\n" +
                "\n" +
                "Download the app at ";
        FirebaseDynamicLinks.getInstance().createDynamicLink().setLink(Uri.parse(link)).setDomainUriPrefix("https://charpair.page.link").setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.liftPlzz").build()).buildShortDynamicLink().addOnSuccessListener(shortDynamicLink -> {
            String inviteLink = shortDynamicLink.getShortLink().toString();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text + inviteLink);
            sendIntent.setType("text/*");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }).addOnFailureListener(Throwable::printStackTrace);


    }


    @Override
    public void setData(ReferralDataResponse.ReferralModel referralData) {
        this.referralData = referralData;
        textViewCode.setText(referralData.getReffer_id().toUpperCase());
        textViewPoint.setText(referralData.getWallet_point() + " ( 1 point = 1 ₹ )");
    }
}
