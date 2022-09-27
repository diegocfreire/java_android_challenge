package com.example.list_app.ui.list_pull;

import static com.example.list_app.common.time.DateTimeFormat.DATETIME;
import static com.example.list_app.common.time.DateTimeFormat.ISO8601_DATETIME;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.list_app.R;
import com.example.list_app.common.time.AppDownloadImage;
import com.example.list_app.common.time.DateTime;
import com.example.list_app.common.time.LocalDate;
import com.example.list_app.common.time.LocalDateFormat;
import com.example.list_app.data.entities.Item;
import com.example.list_app.data.entities.PullRequests;
import com.example.list_app.databinding.ItemMainBinding;
import com.example.list_app.databinding.ItemPullRequestBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PullRequestsAdapter extends RecyclerView.Adapter<PullRequestsAdapter.ViewHolder> {

    private ArrayList<PullRequests> localDataSet;

    public final ObservableField<PullRequests> dataOnClick = new ObservableField<PullRequests>();

    public PullRequestsAdapter(ArrayList<PullRequests> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_pull_request, viewGroup, false);

        return new ViewHolder(Objects.requireNonNull(DataBindingUtil.bind(view)));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        PullRequests item = localDataSet.get(position);

        viewHolder.bind(item);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataOnClick.set(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void updateDataSet(List<PullRequests> data, boolean b) {
        localDataSet = new ArrayList(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemPullRequestBinding mBinding;

        public ViewHolder(ItemPullRequestBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(PullRequests item) {

            mBinding.title.setText(item.title);
            mBinding.descricao.setText(item.body);
            mBinding.username.setText(item.user.login);
            try {
                DateTime create = DateTime.parse(item.created_at,ISO8601_DATETIME);
                mBinding.dataCreate.setText(create.toString(DATETIME));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            AppDownloadImage.GetAppDownloadImage().getImage(item.user.avatar_url,mBinding.image);

        }

    }
}
