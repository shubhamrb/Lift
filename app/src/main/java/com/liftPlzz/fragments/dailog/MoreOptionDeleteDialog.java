package com.liftPlzz.fragments.dailog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.liftPlzz.R;

public class MoreOptionDeleteDialog extends BottomSheetDialogFragment {

    public ItemSelectListiner itemSelectListiner;

    public static MoreOptionDailog newInstance() {
        return new MoreOptionDailog();
    }
    public void listiner(ItemSelectListiner itemSelectListiner){
        this.itemSelectListiner=itemSelectListiner;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.more_option_dialog_delete, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnCancel=(Button) view.findViewById(R.id.btnCancel);
        Button btnEdit=(Button) view.findViewById(R.id.btnEdit);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to cancel it?")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                itemSelectListiner.cancel();

//                                dismissFragment();
                                itemSelectListiner.cancel();
                                dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dismiss();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Alert");
                alert.show();
//
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemSelectListiner.edit();
                dismiss();
            }
        });
    }
    public void dismissFragment(){
        dismiss();
    }

    public interface ItemSelectListiner{
        void cancel();
        void edit();
    }

}
