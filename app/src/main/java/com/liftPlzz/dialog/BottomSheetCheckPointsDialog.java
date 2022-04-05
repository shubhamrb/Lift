package com.liftPlzz.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.liftPlzz.R;
import com.liftPlzz.adapter.CheckPointsListAdapter;
import com.liftPlzz.model.CheckPoints;

import java.util.List;


public class BottomSheetCheckPointsDialog extends BottomSheetDialogFragment {
    BottomSheetCheckPointsDialog bottomSheetCheckPointsDialog;
    CallBackSelectionCheckPoints callBackSelection;
    List<CheckPoints> groupLists;
    CheckPointsListAdapter checkPointsListAdapter;

    public void setGroupList(List<CheckPoints> groupLists) {
        this.groupLists = groupLists;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_bottom_sheet_checkpoints, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.rvBottomSheet);
        AppCompatTextView textViewCancel = v.findViewById(R.id.textViewCancel);
        AppCompatImageView imageViewAddLocation = v.findViewById(R.id.imageViewAddLocation);
        bottomSheetCheckPointsDialog = this;

        if (groupLists != null) {
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkPointsListAdapter = new CheckPointsListAdapter(getContext(), groupLists, new CheckPointsListAdapter.ItemListener() {
            @Override
            public void onclick(int s) {
                callBackSelection.setCallBackSelectionCheckPoints(s);
            }

            @Override
            public void onDeleteClick(CheckPoints s) {
                groupLists.remove(s);
                checkPointsListAdapter.notifyDataSetChanged();
                callBackSelection.setCallBackSelectionCheckPointsDelete(groupLists.size());
                if (!s.getAddress().contains("Select Checkpoints")) {
                    callBackSelection.getRemainingList(groupLists);
                }
            }
        });
        recyclerView.setAdapter(checkPointsListAdapter);
        textViewCancel.setOnClickListener(
                v1 -> {
                    if (bottomSheetCheckPointsDialog != null) {
                        bottomSheetCheckPointsDialog.dismiss();
                    }
                });
        imageViewAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupLists.size() < 3) {
                    CheckPoints checkPoints = new CheckPoints();
                    checkPoints.setId(groupLists.size() + 1);
                    checkPoints.setAddress("Select Checkpoints " + (groupLists.size() + 1));
                    groupLists.add(checkPoints);
                    checkPointsListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "You can not add more the 3 checkpoints", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    public void nofityAdapter() {
        checkPointsListAdapter.notifyDataSetChanged();
    }

    public void setSelectionListner(CallBackSelectionCheckPoints callBackSelection) {
        this.callBackSelection = callBackSelection;
    }

    public interface CallBackSelectionCheckPoints {
        void setCallBackSelectionCheckPoints(int preferredCallingMode);

        void setCallBackSelectionCheckPointsDelete(int preferredCallingMode);

        void getRemainingList(List<CheckPoints> groupLists);

        void onDeleteCheckList(int preferredCallingMode);
    }
}
