package com.gzyslczx.yslc.fragments.yourui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.FiveRangeFragmentBinding;
import com.gzyslczx.yslc.events.yourui.FiveRangeEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.yourui.sdk.message.use.FiveRangeData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FiveDayStockFragment extends BaseFragment<FiveRangeFragmentBinding> {

    private final int PriceStyle=123;
    private final int VolStyle=456;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mViewBinding = FiveRangeFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 接受五档数据
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFiveRangeEvent(FiveRangeEvent event){
        Log.d(getClass().getSimpleName(), "接收到五档数据");
        //卖出
        DifferenceColor(event.getClosePrice(), event.getData().getSellPrice5(), event.getData().getSellCount5(), 1);
        DifferenceColor(event.getClosePrice(), event.getData().getSellPrice4(), event.getData().getSellCount4(), 2);
        DifferenceColor(event.getClosePrice(), event.getData().getSellPrice3(), event.getData().getSellCount3(), 3);
        DifferenceColor(event.getClosePrice(), event.getData().getSellPrice2(), event.getData().getSellCount2(), 4);
        DifferenceColor(event.getClosePrice(), event.getData().getSellPrice1(), event.getData().getSellCount1(), 5);
        //买入
        DifferenceColor(event.getClosePrice(), event.getData().getBuyPrice1(), event.getData().getBuyCount1(), 6);
        DifferenceColor(event.getClosePrice(), event.getData().getBuyPrice2(), event.getData().getBuyCount2(), 7);
        DifferenceColor(event.getClosePrice(), event.getData().getBuyPrice3(), event.getData().getBuyCount3(), 8);
        DifferenceColor(event.getClosePrice(), event.getData().getBuyPrice4(), event.getData().getBuyCount4(), 9);
        DifferenceColor(event.getClosePrice(), event.getData().getBuyPrice5(), event.getData().getBuyCount5(), 0);

        CountGain(event.getData());
    }


    /*
    * 区分涨跌颜色
    * */
    private void DifferenceColor(double pre, double now, long vol, int index){
        if (pre>now){
            WhichTextView(index, PriceStyle).setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
        }else if (pre<now){
            WhichTextView(index, PriceStyle).setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            WhichTextView(index, PriceStyle).setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
        }
        WhichTextView(index, PriceStyle).setText(String.valueOf(now));
        WhichTextView(index, VolStyle).setText(String.valueOf(vol));
    }
    private TextView WhichTextView(int index, int style){
        if (index==1){
            if (style==PriceStyle){
                return mViewBinding.FiveRangePrice1;
            }
            if (style==VolStyle){
                return mViewBinding.FiveRangeVol1;
            }
        }else if (index==2){
            if (style==PriceStyle){
                return mViewBinding.FiveRangePrice2;
            }
            if (style==VolStyle){
                return mViewBinding.FiveRangeVol2;
            }
        }else if (index==3){
            if (style==PriceStyle){
                return mViewBinding.FiveRangePrice3;
            }
            if (style==VolStyle){
                return mViewBinding.FiveRangeVol3;
            }
        }else if (index==4){
            if (style==PriceStyle){
                return mViewBinding.FiveRangePrice4;
            }
            if (style==VolStyle){
                return mViewBinding.FiveRangeVol4;
            }
        }else if (index==5){
            if (style==PriceStyle){
                return mViewBinding.FiveRangePrice5;
            }
            if (style==VolStyle){
                return mViewBinding.FiveRangeVol5;
            }
        }else if (index==6){
            if (style==PriceStyle){
                return mViewBinding.FiveRangePrice6;
            }
            if (style==VolStyle){
                return mViewBinding.FiveRangeVol6;
            }
        }else if (index==7){
            if (style==PriceStyle){
                return mViewBinding.FiveRangePrice7;
            }
            if (style==VolStyle){
                return mViewBinding.FiveRangeVol7;
            }
        }else if (index==8){
            if (style==PriceStyle){
                return mViewBinding.FiveRangePrice8;
            }
            if (style==VolStyle){
                return mViewBinding.FiveRangeVol8;
            }
        }else if (index==9){
            if (style==PriceStyle){
                return mViewBinding.FiveRangePrice9;
            }
            if (style==VolStyle){
                return mViewBinding.FiveRangeVol9;
            }
        }else {
            if (style==PriceStyle){
                return mViewBinding.FiveRangePrice0;
            }
            else{
                return mViewBinding.FiveRangeVol0;
            }
        }
        return null;
    }

    /*
    * 计算委托分布占比
    * */
    private void CountGain(FiveRangeData date){
        double SumOfSellVol = date.getSellCount1()+date.getSellCount2()+date.getSellCount3()+date.getSellCount4()+date.getSellCount5();
        double SumOfBuyVol = date.getBuyCount1()+date.getBuyCount2()+date.getBuyCount3()+date.getBuyCount4()+date.getBuyCount5();
        double Sum = SumOfBuyVol+SumOfSellVol;
        double RedGain = Math.round(SumOfBuyVol / Sum*100)/100d;
        double GreenGain = Math.round(SumOfSellVol / Sum*100)/100d;
        mViewBinding.FiveRangeOfGain.SetGain(RedGain, GreenGain);
    }



}
