package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.getFaq.FaqData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {


    List<FaqData> arrayListFaq;
    private final Context context;


    public FAQAdapter(Context context, List<FaqData> arrayListFaq) {
        this.context = context;
        this.arrayListFaq = arrayListFaq;
    }

    @Override
    public int getItemCount() {
        return arrayListFaq.size();

    }

    @Override
    public FAQAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_faq, parent, false);
        return new FAQAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FAQAdapter.ViewHolder holder, final int position) {
        FaqData datum = arrayListFaq.get(position);
        holder.tvQuestion.setText(datum.getQuestion());
        holder.tvAnswer.setText(datum.getAnswer());
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_question)
        AppCompatTextView tvQuestion;
        @BindView(R.id.tv_answer)
        AppCompatTextView tvAnswer;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}