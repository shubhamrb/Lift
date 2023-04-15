package com.liftPlzz.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.liftPlzz.BuildConfig;
import com.liftPlzz.R;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.ReferralDataResponse;
import com.liftPlzz.presenter.ReferPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ReferView;

import java.io.File;
import java.io.FileOutputStream;

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

    @BindView(R.id.screen)
    FrameLayout screen;

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
        Bitmap bitmap = Bitmap.createBitmap(screen.getWidth(), screen.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        screen.draw(canvas);
        Uri bmpUri = getUriFromBitmap(bitmap);

        String text = "Get Easy, Economic ,Comfortable Lift to your destination on Char Pair\n" +
                "\n" +
                "Use my referral code " + referralData.getReffer_id() + " & get 250 welcome Pair Points to pay for lift.\n" +
                "\n" +
                "Enjoy eco-friendly lift anytime, anywhere with your fuel partner.\n" +
                "\n" +
                "Download the app at ";

        FirebaseDynamicLinks.getInstance().createDynamicLink().setLink(Uri.parse(link)).setDomainUriPrefix("https://charpair.page.link").setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.liftPlzz").build()).buildShortDynamicLink().addOnSuccessListener(shortDynamicLink -> {
            String inviteLink = shortDynamicLink.getShortLink().toString();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);

            if (bmpUri != null) {
                sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            }
            if (bmpUri != null) {
                sendIntent.setType(getMimeType(bmpUri));
            } else {
                sendIntent.setType("text/*");
            }

            sendIntent.putExtra(Intent.EXTRA_TEXT, text + inviteLink);
            sendIntent.setType("text/*");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }).addOnFailureListener(Throwable::printStackTrace);


    }

    public String getMimeType(Uri uri) {
        String mimeType;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = getContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public Uri getUriFromBitmap(Bitmap bmp) {

        Uri bmpUri = null;
        try {
            File file = new File(getContext().getExternalFilesDir(null), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            if (Build.VERSION.SDK_INT >= 24) {
                bmpUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".imagepicker.provider", file);  // use this version for API >= 24
            } else {
                bmpUri = Uri.fromFile(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void setData(ReferralDataResponse.ReferralModel referralData) {
        this.referralData = referralData;
        textViewCode.setText(referralData.getReffer_id().toUpperCase());
        textViewPoint.setText(referralData.getWallet_point() + " ( 1 point = 1 ₹ )");
    }
}
