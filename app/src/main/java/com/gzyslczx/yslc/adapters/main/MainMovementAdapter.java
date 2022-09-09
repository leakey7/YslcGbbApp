package com.gzyslczx.yslc.adapters.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gzyslczx.yslc.databinding.MainMovementItemBinding;
import com.gzyslczx.yslc.modes.response.ResMainFinancingBuy;
import com.gzyslczx.yslc.modes.response.ResMainInstitutionBuy;
import com.gzyslczx.yslc.modes.response.ResMainNorthBuy;

public class MainMovementAdapter extends BaseAdapter {

    public final static int NorthItem = 1;
    public final static int InstitutionItem = 2;
    public final static int FinancingItem = 3;
    private ResMainNorthBuy northBuy;
    private ResMainInstitutionBuy institutionsBuy;
    private ResMainFinancingBuy financingBuy;
    private int ItemType=1;
    private Context context;


    public MainMovementAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (ItemType==NorthItem && northBuy!=null){
            return northBuy.getResultObj().size();
        }
        if (ItemType==InstitutionItem && institutionsBuy!=null){
            return institutionsBuy.getResultObj().size();
        }
        if (ItemType==FinancingItem && financingBuy!=null){
            return financingBuy.getResultObj().size();
        }
        return 3;
    }

    @Override
    public Object getItem(int i) {
        if (ItemType==NorthItem && northBuy!=null && northBuy.getResultObj().size()>0){
            return northBuy.getResultObj().get(i);
        }
        if (ItemType==InstitutionItem && institutionsBuy!=null && institutionsBuy.getResultObj().size()>0){
            return institutionsBuy.getResultObj().get(i);
        }
        if (ItemType==FinancingItem && financingBuy!=null && financingBuy.getResultObj().size()>0){
            return financingBuy.getResultObj().get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MainMovementItemBinding binding;
        if (view == null) {
            binding = MainMovementItemBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (MainMovementItemBinding) view.getTag();
        }
        if (ItemType==NorthItem && northBuy!=null){
            binding.MovementStockName.setText(northBuy.getResultObj().get(i).getStock_name());
            binding.MovementMarketValue.setText(northBuy.getResultObj().get(i).getOverweight_market_value_esti());
            binding.MovementRatio.setText(northBuy.getResultObj().get(i).getOw_share_num_to_floatshare_ratio());
            return view;
        }
        if (ItemType==InstitutionItem && institutionsBuy!=null){
            binding.MovementStockName.setText(institutionsBuy.getResultObj().get(i).getStockName());
            binding.MovementMarketValue.setText(institutionsBuy.getResultObj().get(i).getMrje());
            binding.MovementRatio.setText(institutionsBuy.getResultObj().get(i).getZpb());
            return view;
        }
        if (ItemType==FinancingItem && financingBuy!=null){
            binding.MovementStockName.setText(financingBuy.getResultObj().get(i).getStock_name());
            binding.MovementMarketValue.setText(financingBuy.getResultObj().get(i).getRz_buy_amount());
            binding.MovementRatio.setText(financingBuy.getResultObj().get(i).getRz_balance_to_floatmktval_ratio());
            return view;
        }
        binding.MovementStockName.setText("");
        binding.MovementMarketValue.setText("");
        binding.MovementRatio.setText("");
        return view;
    }

    public void setNorthBuy(ResMainNorthBuy northBuy) {
        this.northBuy = northBuy;
    }

    public void setInstitutionsBuy(ResMainInstitutionBuy institutionsBuy) {
        this.institutionsBuy = institutionsBuy;
    }

    public void setFinancingBuy(ResMainFinancingBuy financingBuy) {
        this.financingBuy = financingBuy;
    }

    public void setItemType(int itemType) {
        ItemType = itemType;
    }

    public int getItemType() {
        return ItemType;
    }

    public ResMainNorthBuy getNorthBuy() {
        return northBuy;
    }

    public ResMainInstitutionBuy getInstitutionsBuy() {
        return institutionsBuy;
    }

    public ResMainFinancingBuy getFinancingBuy() {
        return financingBuy;
    }
}
