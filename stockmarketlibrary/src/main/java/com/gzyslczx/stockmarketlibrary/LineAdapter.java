package com.gzyslczx.stockmarketlibrary;

import java.util.ArrayList;
import java.util.List;

public abstract class LineAdapter<T> {

    private List<IPoint<T>> points;
    private int lineColor;

    public int getListSize(){
        return points==null ? 0 : points.size();
    };

    public T getItemData(int point){
        if (getListSize()!=0 && points.size()>point){
            return points.get(point).getExtraData();
        }
        return null;
    };

    public float getXValue(int point){
        if (getListSize()!=0 && points.size()>point){
            return points.get(point).getXValue();
        }
        return 0;
    };

    public float getYValue(int point){
        if (getListSize()!=0 && points.size()>point){
            return points.get(point).getYValue();
        }
        return 0;
    };

    public abstract void SetLineColor(int color);

    public abstract void SetXValue(int point);

    public abstract void SetYValue(int point);

    public void AddPoint(IPoint<T> iPoint){
        if (points==null){
            points = new ArrayList<>();
        }
        points.add(iPoint);

    };

    public void AddAllPoint(List<IPoint<T>> list){
        if (points==null){
            points = new ArrayList<>();
        }
        points.addAll(list);
    }

    public void SetPoints(List<IPoint<T>> list){
        if (points==null){
            points = new ArrayList<>();
        }else {
            points.clear();
        }
        points.addAll(list);
    }

}
