package com.databinding.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.databinding.R;
import com.databinding.bean.User;
import com.databinding.databinding.ItemListBinding;

import java.util.List;

/**
 * @Description: 动态绑定
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> list;

    public UserAdapter(List<User> list) {
        this.list = list;
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 创建ViewHolder的时候 绑定
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ItemListBinding ilBinding;

        public UserViewHolder(View itemView) {
            super(itemView);
            ilBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(@NonNull User user) {
            ilBinding.setUser(user);
        }
    }
}
