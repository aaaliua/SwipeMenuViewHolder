# SwipeMenuViewHolder


一个可以滑出条目操作菜单的RecycleView.ViewHolder 。

用法：

添加依赖
```gradle
compile 'cn.easydone.swipemenuviewholder:SwipeMenuViewHolder:0.1'
```

在 Adapter 里继承 SwipeMenuViewHolder ，在 onCreateViewHolder 方法里 inflate 要拖动的布局和拖出来的布局。
```Java
View swipeMenuView = layoutInflater.inflate(R.layout.swipe_menu_view, viewGroup, false);
swipeMenuView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
View captureView = layoutInflater.inflate(R.layout.capture_view, viewGroup, false);
captureView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
return new SwipeViewHolder(context, swipeMenuView, captureView, SwipeMenuViewHolder.EDGE_RIGHT).getDragViewHolder();
```

在 onBindViewHolder 方法里设置拖出来的布局子View的点击事件。
```Java
swipeHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
});
```
运行效果如下图：
![](https://github.com/liangzhitao/SwipeMenuRecyclerView/blob/master/swipe_menu_recyclerview.gif)
