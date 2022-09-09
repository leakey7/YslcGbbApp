package com.gzyslczx.yslc.adapters.search;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.search.bean.SearchMoreData;
import com.gzyslczx.yslc.tools.NormalActionTool;

import java.util.List;

public class SearchMoreListAdapter extends BaseMultiItemQuickAdapter<SearchMoreData, BaseViewHolder> {

    private StringBuilder searchText;

    public SearchMoreListAdapter(@Nullable List<SearchMoreData> data) {
        super(data);
        addItemType(SearchMoreData.StockType, R.layout.search_normal_stock_item);
        addItemType(SearchMoreData.FundType, R.layout.search_normal_fund_item);
        addItemType(SearchMoreData.ArticleType, R.layout.search_about_item);
        addChildClickViewIds(R.id.StockOption);
    }

    public void setSearchText(String searchText) {
        if (this.searchText==null){
            this.searchText = new StringBuilder();
        }
        if (TextUtils.isEmpty(searchText)){
            this.searchText.delete(0, this.searchText.length());
            return;
        }
        this.searchText.replace(0, this.searchText.length(), searchText);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, SearchMoreData data) {
        switch (data.getItemType()){
            case SearchMoreData.StockType:
                baseViewHolder.setText(R.id.StockName, NormalActionTool.getColorSpannableString(data.getStockData().getStockName(), searchText.toString(),
                        ContextCompat.getColor(getContext(), R.color.main_red)));
                baseViewHolder.setText(R.id.StockCode, NormalActionTool.getColorSpannableString(data.getStockData().getStockCode(), searchText.toString(),
                        ContextCompat.getColor(getContext(), R.color.main_red)));
                if (data.getStockData().isFocus()){
                    baseViewHolder.setBackgroundResource(R.id.StockOption,R.drawable.gray_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.StockOption, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.StockOption, "删自选");
                }else {
                    baseViewHolder.setBackgroundResource(R.id.StockOption,R.drawable.red_border_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.StockOption, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.StockOption, "加自选");
                }
                break;
            case SearchMoreData.FundType:
                baseViewHolder.setText(R.id.FundName, NormalActionTool.getColorSpannableString(data.getFundData().getName(), searchText.toString(),
                        ContextCompat.getColor(getContext(), R.color.main_red)));
                baseViewHolder.setText(R.id.FundCode, NormalActionTool.getColorSpannableString(data.getFundData().getFCode(), searchText.toString(),
                        ContextCompat.getColor(getContext(), R.color.main_red)));
                break;
            case SearchMoreData.ArticleType:
                baseViewHolder.setText(R.id.aboutTitle, NormalActionTool.getColorSpannableString(data.getArtData().getTitle(), searchText.toString(),
                        ContextCompat.getColor(getContext(), R.color.main_red)));
                baseViewHolder.setText(R.id.aboutTitleDes, NormalActionTool.getColorSpannableString(data.getArtData().getDesc(), searchText.toString(),
                        ContextCompat.getColor(getContext(), R.color.main_red)));
                baseViewHolder.setText(R.id.aboutTime, data.getArtData().getDateInfo());
                break;
            default:
                break;
        }
    }
}
