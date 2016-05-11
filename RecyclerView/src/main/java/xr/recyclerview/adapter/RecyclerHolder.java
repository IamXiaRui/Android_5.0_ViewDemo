package xr.recyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import xr.recyclerview.R;
import xr.recyclerview.bean.ListBean;

/**
 * @Description:ReclyclerView的Holder
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {

    private ImageView itemImage;
    private TextView itemText;

    /**
     * 初始化ViewHolder
     *
     * @param itemView
     */
    public RecyclerHolder(View itemView) {
        super(itemView);
        itemImage = (ImageView) itemView.findViewById(R.id.iv_icon);
        itemText = (TextView) itemView.findViewById(R.id.tv_name);
    }

    /**
     * 设置数据刷新UI
     *
     * @param listBean
     */
    public void setDataAndRefreshUI(ListBean listBean) {
        itemText.setText(listBean.name);
        itemImage.setImageResource(listBean.iconId);
    }
}
