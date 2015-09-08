package cn.easydone.swipemenurecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

import cn.easydone.swipemenuviewholder.SwipeMenuViewHolder;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-07-31
 * Time: 13:28
 */
public class SwipeAdapter extends UltimateViewAdapter {
    private List<String> mList;
    private Context context;
    private LayoutInflater layoutInflater;

    public SwipeAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        View swipeMenuView = layoutInflater.inflate(R.layout.swipe_menu_view, viewGroup, false);
        swipeMenuView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View captureView = layoutInflater.inflate(R.layout.capture_view, viewGroup, false);
        captureView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return new SwipeViewHolder(context, swipeMenuView, captureView, SwipeMenuViewHolder.EDGE_RIGHT).getDragViewHolder();
    }

    @Override
    public int getAdapterItemCount() {
        return mList.size();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final SwipeViewHolder swipeHolder = (SwipeViewHolder) SwipeMenuViewHolder.getHolder(viewHolder);
        String data = mList.get(position);
        swipeHolder.tvDescription.setText(data);
        swipeHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
    }

    class SwipeViewHolder extends SwipeMenuViewHolder {

        private TextView tvDescription;
        private TextView tvDelete;

        public SwipeViewHolder(Context context, View bgView, View topView, int mTrackingEdges) {
            super(context, bgView, topView, mTrackingEdges);
        }

        @Override
        public void initView(View itemView) {
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }
}
