package com.liftPlzz.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.BlockListAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.partnerdetails.User;
import com.liftPlzz.presenter.BlockPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.BlockView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends BaseFragment<BlockPresenter, BlockView> implements BlockView, BlockListAdapter.ItemListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.btn_add)
    ImageView btn_add;
    @BindView(R.id.recyclerViewNotification)
    RecyclerView recyclerViewNotification;
    private String token = "";

    @Override
    protected int createLayout() {
        return R.layout.fragment_block;
    }

    @Override
    protected void setPresenter() {
        presenter = new BlockPresenter();
    }


    @Override
    protected BlockView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        token = sharedPreferences.getString(Constants.TOKEN, "");
        toolBarTitle.setText("Select user to Block");
        loadBlockUsers();

        btn_add.setVisibility(View.GONE);
    }

    private void loadBlockUsers() {
        presenter.getBlockedUsers(token);
    }


    @OnClick(R.id.imageViewBack)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void setBlockData(List<User> users) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (!users.get(i).isIs_block()) {
                list.add(users.get(i));
            }
        }
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNotification.setAdapter(new BlockListAdapter(getContext(), list, this));
    }

    @Override
    public void onSuccessBlock() {
        showMessage("Blocked");
        loadBlockUsers();
    }

    @Override
    public void onclick(Integer user_id) {
        reasonDialog(user_id);
    }

    public void reasonDialog(Integer user_id) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.block_reason_dialog);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);

        buttonSubmit.setOnClickListener(v -> {
            if (editTextPoints.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter the reason", Toast.LENGTH_SHORT).show();
            } else {
                presenter.getBlockUser(token, user_id, editTextPoints.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
