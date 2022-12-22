package com.gzyslczx.stockmarketlibrary;

import java.util.HashMap;

public abstract class LineAdapter {

    private HashMap<String, ILine> lineMap;

    public LineAdapter() {
        lineMap = new HashMap<>();
    }

    public int getLineItemSize(){
        return lineMap.size();
    }

    public void addLine(ILine iLine){
        lineMap.put(iLine.getTag(), iLine);
    }

    public ILine getLine(String tag){
        return lineMap.get(tag);
    }


}
