package com.gzyslczx.yslc.adapters.stockmarket;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gzyslczx.yslc.databinding.RealTimeQuotesItemBinding;

import java.util.List;

public class MoreRealTimeGridAdapter extends BaseAdapter {

    private int type;
    public static final int NormalStyle = 1;
    public static final int KeChuangStyle = 2;
    private Context context;
    private List<String> valueList;
    private String DefValue="--";

    public MoreRealTimeGridAdapter(int type, Context context) {
        this.type = type;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (type==NormalStyle){
            return 18;
        }else if (type==KeChuangStyle){
            return 24;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (valueList!=null){
            if (type==NormalStyle && valueList.size()>=18) {
                return valueList.get(position);
            }else if (type==KeChuangStyle && valueList.size()>=24){
                return valueList.get(position);
            }
        }
        return DefValue;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RealTimeQuotesItemBinding binding;
        if (convertView == null) {
            binding = RealTimeQuotesItemBinding.inflate(LayoutInflater.from(context));
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (RealTimeQuotesItemBinding) convertView.getTag();
        }
        switch (position){
            case 0:
                binding.LeftTag.setText("均价");
                break;
            case 1:
                binding.LeftTag.setText("量比");
                break;
            case 2:
                binding.LeftTag.setText("成交量");
                break;
            case 3:
                binding.LeftTag.setText("昨收");
                break;
            case 4:
                binding.LeftTag.setText("委比");
                break;
            case 5:
                binding.LeftTag.setText("成交额");
                break;
            case 6:
                binding.LeftTag.setText("涨停");
                break;
            case 7:
                binding.LeftTag.setText("外盘");
                break;
            case 8:
                binding.LeftTag.setText("市盈率(静)");
                break;
            case 9:
                binding.LeftTag.setText("跌停");
                break;
            case 10:
                binding.LeftTag.setText("内盘");
                break;
            case 11:
                binding.LeftTag.setText("市盈率(动)");
                break;
            case 12:
                String baseString = "市盈率";
                String extendString = "TTM";
                SpannableStringBuilder cs = new SpannableStringBuilder(baseString + extendString);
                int start = baseString.length();
                cs.setSpan(new SuperscriptSpan(),
                        start,
                        start + extendString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                cs.setSpan(new RelativeSizeSpan(0.3f),
                        start,
                        start + extendString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.LeftTag.setText(cs);
                break;
            case 13:
                binding.LeftTag.setText("流通股");
                break;
            case 14:
                    binding.LeftTag.setText("流通值");
                    break;

            case 15:
                binding.LeftTag.setText("市净率");
                break;
            case 16:
                binding.LeftTag.setText("总股本");
                break;
            case 17:
                binding.LeftTag.setText("总市值");
                break;
            case 18:
                binding.LeftTag.setText("盘后量");
                break;
            case 19:
                binding.LeftTag.setText("注册股本");
                break;
            case 20:
                binding.LeftTag.setText("是否注册制");
                break;
            case 21:
                binding.LeftTag.setText("盘后额");
                break;
            case 22:
                binding.LeftTag.setText("发行股本");
                break;
            case 23:
            binding.LeftTag.setText("总市直");
            break;

        }
        if (valueList != null && valueList.size()>0) {
            binding.RightValue.setText(valueList.get(position));
        }else {
            binding.RightValue.setText(DefValue);
        }
        return convertView;
    }

    public List<String> getValueList() {
        return valueList;
    }

    public void setValueList(List<String> valueList) {
        this.valueList = valueList;
    }
}
