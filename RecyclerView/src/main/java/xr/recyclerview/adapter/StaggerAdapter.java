package xr.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xr.recyclerview.R;
import xr.recyclerview.bean.ListBean;

/**
 * @Description:ReclyclerView的列表适配器
 */
public class StaggerAdapter extends RecyclerView.Adapter<StaggerAdapter.RecyclerHolder> {

    private Context context;
    private List<ListBean> listData;

    public StaggerAdapter(Context context, List<ListBean> listData) {
        this.context = context;
        this.listData = listData;
    }

    /**
     * 创建一个ViewHolder对象
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建一个根布局
        View itemView = View.inflate(context, R.layout.item_stagger, null);
        return new RecyclerHolder(itemView);
    }

    /**
     * 绑定一个ViewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        holder.setDataAndRefreshUI(listData.get(position));
    }

    /**
     * 列表的Item的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (listData != null) {
            return listData.size();
        }
        return 0;
    }

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
            itemImage = (ImageView) itemView.findViewById(R.id.item_staggered_iv);
            itemText = (TextView) itemView.findViewById(R.id.item_staggered_tv);
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

}
