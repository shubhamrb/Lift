package com.liftPlzz.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.OnClick;
import com.liftPlzz.R;
import com.liftPlzz.activity.HomeActivity;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.presenter.ContactsPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ContactsView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends BaseFragment<ContactsPresenter, ContactsView> implements ContactsView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewHome)
    ImageView imageViewHome;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    SharedPreferences sharedPreferences;

    @Override
    protected int createLayout() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void setPresenter() {
        presenter = new ContactsPresenter();
    }


    @Override
    protected ContactsView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("Contacts");
    }


    @OnClick(R.id.imageViewHome)
    public void onViewClicked() {
        ((HomeActivity) getActivity()).openDrawer();
    }
}
