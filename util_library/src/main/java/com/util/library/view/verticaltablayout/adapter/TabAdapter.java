package com.util.library.view.verticaltablayout.adapter;


import com.util.library.view.verticaltablayout.widget.ITabView;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */
public interface TabAdapter {
    int getCount();


    ITabView.TabIcon getIcon(int position);

    ITabView.TabTitle getTitle(int position);

    int getBackground(int position);

}
