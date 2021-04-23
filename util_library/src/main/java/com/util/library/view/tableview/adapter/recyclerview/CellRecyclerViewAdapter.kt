/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.util.library.view.tableview.adapter.recyclerview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.util.library.view.tableview.ITableView
import com.util.library.view.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.util.library.view.tableview.layoutmanager.ColumnLayoutManager
import com.util.library.view.tableview.listener.itemclick.CellRecyclerViewItemClickListener
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by evrencoskun on 10/06/2017.
 */
class CellRecyclerViewAdapter<C>(context: Context, itemList: List<C>?, private val mTableView: ITableView) : AbstractRecyclerViewAdapter<C>(context, itemList) {
    private val mRecycledViewPool: RecycledViewPool

    // This is for testing purpose
    private var mRecyclerViewId = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {

        // Create a RecyclerView as a Row of the CellRecyclerView
        val recyclerView = CellRecyclerView(mContext)

        // Use the same view pool
        recyclerView.setRecycledViewPool(mRecycledViewPool)
        if (mTableView.isShowHorizontalSeparators) {
            // Add divider
            recyclerView.addItemDecoration(mTableView.horizontalItemDecoration)
        }

        // To get better performance for fixed size TableView
        recyclerView.setHasFixedSize(mTableView.hasFixedWidth())

        // set touch mHorizontalListener to scroll synchronously
        recyclerView.addOnItemTouchListener(mTableView.horizontalRecyclerViewListener)

        // Add Item click listener for cell views
        if (mTableView.isAllowClickInsideCell) {
            recyclerView.addOnItemTouchListener(CellRecyclerViewItemClickListener(recyclerView,
                    mTableView))
        }

        // Set the Column layout manager that helps the fit width of the cell and column header
        // and it also helps to locate the scroll position of the horizontal recyclerView
        // which is row recyclerView
        recyclerView.layoutManager = ColumnLayoutManager(mContext, mTableView)


        // Create CellRow adapter
        recyclerView.adapter = CellRowRecyclerViewAdapter<Any>(mContext, mTableView)
        recyclerView.adapter!!.registerAdapterDataObserver(mTableView.adapter!!.adapterDataObserver)
        // This is for testing purpose to find out which recyclerView is displayed.
        recyclerView.id = mRecyclerViewId
        mRecyclerViewId++
        return CellRowViewHolder(recyclerView)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, yPosition: Int) {
        val viewHolder = holder as CellRowViewHolder
        val viewAdapter = viewHolder.recyclerView.adapter as CellRowRecyclerViewAdapter<*>?

        // Get the list
        val rowList = mItemList[yPosition] as List<C>

        // Set Row position
        viewAdapter!!.yPosition = yPosition

        // Set the list to the adapter
        viewAdapter.setItems(rowList as List<Nothing>)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder) {
        super.onViewAttachedToWindow(holder)
        val viewHolder = holder as CellRowViewHolder
        val scrollHandler = mTableView.scrollHandler

        // The below code helps to display a new attached recyclerView on exact scrolled position.
        (viewHolder.recyclerView.layoutManager as ColumnLayoutManager?)
                ?.scrollToPositionWithOffset(scrollHandler.columnPosition, scrollHandler
                        .columnPositionOffset)
        val selectionHandler = mTableView.selectionHandler
        if (selectionHandler.isAnyColumnSelected) {
            val cellViewHolder = viewHolder.recyclerView
                    .findViewHolderForAdapterPosition(selectionHandler.selectedColumnPosition) as AbstractViewHolder?
            if (cellViewHolder != null) {
                // Control to ignore selection color
                if (!mTableView.isIgnoreSelectionColors) {
                    cellViewHolder.setBackgroundColor(mTableView.selectedColor)
                }
                cellViewHolder.setSelected(AbstractViewHolder.SelectionState.SELECTED)
            }
        } else if (selectionHandler.isRowSelected(holder.getAdapterPosition())) {
            selectionHandler.changeSelectionOfRecyclerView(viewHolder.recyclerView,
                    AbstractViewHolder.SelectionState.SELECTED, mTableView.selectedColor)
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder) {
        super.onViewDetachedFromWindow(holder)

        // Clear selection status of the view holder
        mTableView.selectionHandler.changeSelectionOfRecyclerView((holder as CellRowViewHolder).recyclerView, AbstractViewHolder.SelectionState.UNSELECTED, mTableView.unSelectedColor)
    }

    override fun onViewRecycled(holder: AbstractViewHolder) {
        super.onViewRecycled(holder)
        val viewHolder = holder as CellRowViewHolder
        // ScrolledX should be cleared at that time. Because we need to prepare each
        // recyclerView
        // at onViewAttachedToWindow process.
        viewHolder.recyclerView.clearScrolledX()
    }

    class CellRowViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        @JvmField
        val recyclerView: CellRecyclerView = itemView as CellRecyclerView

    }

    fun notifyCellDataSetChanged() {
        val visibleRecyclerViews = mTableView.cellLayoutManager
                .visibleCellRowRecyclerViews
        if (visibleRecyclerViews.isNotEmpty()) {
            for (cellRowRecyclerView in visibleRecyclerViews) {
                if (cellRowRecyclerView != null) {
                    val adapter = cellRowRecyclerView.adapter
                    adapter?.notifyDataSetChanged()
                }
            }
        } else {
            notifyDataSetChanged()
        }
    }

    /**
     * This method helps to get cell item model that is located on given column position.
     *
     * @param columnPosition
     */
    fun getColumnItems(columnPosition: Int): List<C> {
        val cellItems: MutableList<C> = ArrayList()
        for (i in mItemList.indices) {
            val rowList = mItemList[i] as List<C>
            if (rowList.size > columnPosition) {
                cellItems.add(rowList[columnPosition])
            }
        }
        return cellItems
    }

    fun removeColumnItems(column: Int) {
        for (i in 0 until itemCount) {
            val cellList = getItem(i) as ArrayList<*>
            cellList.removeAt(column)
        }

        notifyCellDataSetChanged()
    }

    fun addColumnItems(column: Int, cellColumnItems: List<C>) {
        // It should be same size with exist model list.
        if (cellColumnItems.size > mItemList.size) {
            val count = cellColumnItems.size - mItemList.size
            for (i in 0 until count) {
                mItemList.add(null)
            }
        }

        // Lets change the model list silently
        val cellItems: MutableList<List<C?>> = ArrayList()
        for (i in mItemList.indices) {
            val rowList: MutableList<C?> = ArrayList(mItemList[i] as List<C?>)
            if (rowList.size > column) {
                rowList.add(column, cellColumnItems.getOrNull(i))
            } else {
                val count = column - rowList.size
                for (j in 0 until count) {
                    rowList.add(null)
                }
                rowList.add(cellColumnItems.getOrNull(i))
            }
            cellItems.add(rowList)
        }

        // Change data without notifying. Because we already did for visible recyclerViews.
        setItems((cellItems as List<C>), true)
    }

    init {

        // Create view pool to share Views between multiple RecyclerViews.
        mRecycledViewPool = RecycledViewPool()
        //TODO set the right value.
        //mRecycledViewPool.setMaxRecycledViews(0, 110);
    }
}