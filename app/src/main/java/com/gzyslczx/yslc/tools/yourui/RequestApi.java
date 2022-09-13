package com.gzyslczx.yslc.tools.yourui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.events.yourui.DailyKLineEvent;
import com.gzyslczx.yslc.events.yourui.MinuteDealDetailEvent;
import com.gzyslczx.yslc.events.yourui.MinuteTrendEvent;
import com.gzyslczx.yslc.events.yourui.RealTimeEvent;
import com.yourui.sdk.message.YRMarket;
import com.yourui.sdk.message.api.BuildRequestParams;
import com.yourui.sdk.message.api.BuildResponseParams;
import com.yourui.sdk.message.api.protocol.AnsInterface;
import com.yourui.sdk.message.api.protocol.IBuildRequestParams;
import com.yourui.sdk.message.api.protocol.IBuildResponseParams;
import com.yourui.sdk.message.api.protocol.IDataApi;
import com.yourui.sdk.message.api.protocol.IQuoteRequest;
import com.yourui.sdk.message.api.protocol.QuoteConstants;
import com.yourui.sdk.message.client.YRMarketConfig;
import com.yourui.sdk.message.entity.AnsDayData;
import com.yourui.sdk.message.entity.AnsFinanceData;
import com.yourui.sdk.message.entity.AnsGeneralSort;
import com.yourui.sdk.message.entity.AnsHQColValue;
import com.yourui.sdk.message.entity.AnsHisTrend;
import com.yourui.sdk.message.entity.AnsLeadData;
import com.yourui.sdk.message.entity.AnsMarketEvent;
import com.yourui.sdk.message.entity.AnsRealTime;
import com.yourui.sdk.message.entity.AnsReportData;
import com.yourui.sdk.message.entity.AnsSeverCalculate;
import com.yourui.sdk.message.entity.AnsSimpleFile;
import com.yourui.sdk.message.entity.AnsStockBaseData;
import com.yourui.sdk.message.entity.AnsStockTick;
import com.yourui.sdk.message.entity.AnsTransSearch;
import com.yourui.sdk.message.entity.AnsTrendData;
import com.yourui.sdk.message.entity.AnsTrendExtData;
import com.yourui.sdk.message.entity.AnsVirtualAuction;
import com.yourui.sdk.message.entity.AskData;
import com.yourui.sdk.message.entity.CodeInfo;
import com.yourui.sdk.message.entity.CompAskData;
import com.yourui.sdk.message.entity.IndexRealTimeExt;
import com.yourui.sdk.message.entity.PriceVolItem;
import com.yourui.sdk.message.entity.RealTimeComplement;
import com.yourui.sdk.message.entity.RealTimeData;
import com.yourui.sdk.message.entity.ReqDateMarketEvent;
import com.yourui.sdk.message.entity.ReqHisTrend;
import com.yourui.sdk.message.entity.ReqLimitTick;
import com.yourui.sdk.message.entity.ReqMarketMonitor;
import com.yourui.sdk.message.entity.ReqTransSearch;
import com.yourui.sdk.message.entity.StockCompDayDataEx;
import com.yourui.sdk.message.entity.StockCompHistoryData;
import com.yourui.sdk.message.entity.StockHistoryTrendHead;
import com.yourui.sdk.message.entity.StockLeadData;
import com.yourui.sdk.message.entity.StockRealTimeExt;
import com.yourui.sdk.message.entity.StockTick;
import com.yourui.sdk.message.entity.StockVirtualAuction;
import com.yourui.sdk.message.entity.YRHQColItem;
import com.yourui.sdk.message.entity.YRIndexRealTimeData;
import com.yourui.sdk.message.entity.YRStockAfterRealTimeExt;
import com.yourui.sdk.message.entity.YRStockRealTimeData;
import com.yourui.sdk.message.listener.ClientListener;
import com.yourui.sdk.message.listener.CommonTick;
import com.yourui.sdk.message.listener.OnMessageCallback;
import com.yourui.sdk.message.save.Constant;
import com.yourui.sdk.message.use.DtkConfig;
import com.yourui.sdk.message.use.FiveRangeData;
import com.yourui.sdk.message.use.KshAfterData;
import com.yourui.sdk.message.use.Realtime;
import com.yourui.sdk.message.use.Stock;
import com.yourui.sdk.message.use.StockKLine;
import com.yourui.sdk.message.use.StockTickDetail;
import com.yourui.sdk.message.use.StockTickInfo;
import com.yourui.sdk.message.use.TrendDataModel;
import com.yourui.sdk.message.utils.CommUtil;
import com.yourui.sdk.message.utils.DateUtil;
import com.yourui.sdk.message.utils.NumberUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * 项目名称：HQMarketSdk
 * 类描述：
 * 创建人：zhouY
 * 创建时间：2019/11/2
 * 修改人：zhouY
 * 修改时间：2019/11/2
 * 修改备注：
 */
public class RequestApi implements IDataApi {

    static RequestApi instance;
    private IBuildRequestParams buildRequestParams;
    private IBuildResponseParams buildResponseParams;
    private double priceUnit;
    //正式地址
    public static final String SERVER_IP = "quote.youruitech.com";
    public static final int SERVER_PORT = 6688;

    private static final String TAG = "YRMarketConfig";
    /**
     * 指数类型转换
     */
    //上证指数
    public static final String INDEX_ORIGINAL_SH = "1A0001";
    //深圳成指
    public static final String INDEX_ORIGINAL_SZ = "2A01";
    //沪深300
    public static final String INDEX_ORIGINAL_300 = "1B0300";
    //上证50
    public static final String INDEX_ORIGINAL_50 = "1B0016";
    public static final String INDEX_ORIGINAL_KSH_50 = "1B0688";
	public static final String NINE_STR = "9";
    //private ArrayList<CodeInfo> mStockList;
    //private Disposable mRunStockObservable;
    private static final int HALF_UNIT = 10000;

    public static RequestApi getInstance() {
        if (null == instance) {
            instance = new RequestApi();

        }
        return instance;
    }


    private RequestApi() {
        buildRequestParams = new BuildRequestParams();
        buildResponseParams = new BuildResponseParams();
        priceUnit = DtkConfig.getInstance().getPriceUnit();
    }

    public static final boolean isBJS(int codeType){
        if (codeType == QuoteConstants.BOURSE_STOCK_BJ_BJS){
            return true;
        }
        return false;
    }

   /* public ArrayList<CodeInfo> getStockList() {
        return mStockList;
    }*/

    public void initServer(Context context) {
        YRMarketConfig cc = YRMarketConfig.build()
                .setServerHost(SERVER_IP)//必传
                .setServerPort(SERVER_PORT)//必传
                .setJsonFormat(new DefaultJsonFormat())//必传
                .setClientListener(new ClientListener() {
                    @Override
                    public void onConnected() {
                        Log.d(TAG, "建立行情服务链接");
                    }

                    @Override
                    public void onDisConnected() {
                        Log.d(TAG, "断开行情服务链接");
                    }

                    @Override
                    public void onLoginServerOk() {
                        Log.d(TAG, "行情服务登录成功");
                    }

                    @Override
                    public void onSyncTemplateOk() {
                        Log.d(TAG, "行情服务模板同步成功");
                    }


                    @Override
                    public void onLoginServerError(int errorCode, String errorInfo) {
                        Log.d(TAG, String.format("行情服务登录失败，ECode=%d，EInfo=%s", errorCode, errorInfo));
                    }
                });
        YRMarket.getInstance()
                .checkInit(context.getApplicationContext())
                .setClientConfig(cc)
                .startService();

    }


    public IBuildRequestParams getBuildRequestParams() {
        return buildRequestParams;
    }

    @Override
    public void loadRealTime(ArrayList<Stock> stockArrayList, final Handler handler) {
        ArrayList<IQuoteRequest> quoteRequestArrayList = buildRequest(stockArrayList);
        IQuoteRequest realTimeData = buildRequestParams.buildRealTime(quoteRequestArrayList);
        YRMarket.getInstance().sendMsg(realTimeData, ansInterface -> {
            try {
                AnsRealTime ansRealTime = buildResponseParams.buildRealTimeInfo(ansInterface);
                // AnsRealTime ansRealTime = (AnsRealTime) ansInterface;
                List<Realtime> realtimeList = new ArrayList<>();
                for (RealTimeData realTimeDataItem : ansRealTime.getRealTimeData()) {
                    if (null != realTimeDataItem) {
                        Realtime realtime = settingRealtimeData(realTimeDataItem, realTimeDataItem.getCodeInfo());
                        realtimeList.add(realtime);
                    }
                }
//                Logger.d(realtimeList);
//                sendMessage(realtimeList, handler, QuoteConstants.RT_REALTIME);
                EventBus.getDefault().post(new RealTimeEvent(realtimeList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 返回A股股票总数
     *
     * @return param  mCodeType 市场类型不传默认查上海、深圳市场的数量
     */
    public long requestAllStockCount(String... mCodeType) {
        return YRMarket.getInstance().getStockCount(mCodeType);
    }

    /**
     * 查询股票类型对应的股票
     *
     * @return param  mCodeType
     */
    public List<RealTimeComplement> findStockList(String... mCodeType) {
        return YRMarket.getInstance().findStockList(mCodeType);
    }

    public List<RealTimeComplement> findByArgs(String... mCodeType) {
        return YRMarket.getInstance().findByArgs(mCodeType);
    }

    public List<RealTimeComplement> findByArgs(String orderBy, String... mCodeType) {
        return YRMarket.getInstance().findByArgs(mCodeType, orderBy);
    }


    public ArrayList<IQuoteRequest> buildRequest(ArrayList<Stock> stockArrayList) {
        ArrayList<IQuoteRequest> quoteRequestArrayList = new ArrayList<>();
        for (int i = 0; i < stockArrayList.size(); i++) {
            Stock stock = stockArrayList.get(i);
            if (null != stock) {
                quoteRequestArrayList.add(buildCodeInfo(stock));
            }
        }
        return quoteRequestArrayList;
    }

    public IQuoteRequest buildCodeInfo(Stock stock) {
        CodeInfo mCodeInfo = new CodeInfo();
        mCodeInfo.setCode(stock.getStockcode());
        mCodeInfo.setCodeType((short) stock.getCodeType());
        return mCodeInfo;
    }

    public String transitionChangeIndexCode(String stockCode, int codeType) {
        String mReturnStr = stockCode;
        if (CommUtil.isIndex(codeType)) {
            RealTimeComplement mRealTimeComplement = YRMarket.getInstance().findByAliasStockCode(stockCode,codeType);
            if (null != mRealTimeComplement) {
                mReturnStr = mRealTimeComplement.getCode();
            }
        }
        return mReturnStr;
    }

    public boolean isFund(int codeType) {
        if (codeType == QuoteConstants.BOURSE_STOCK_SH_FUND || codeType == QuoteConstants.BOURSE_STOCK_SH_LOF_FUND
                || codeType == QuoteConstants.BOURSE_STOCK_SH_ETF_FUND
                || codeType == QuoteConstants.BOURSE_STOCK_SZ_FUND
                || codeType == QuoteConstants.BOURSE_STOCK_SZ_LOF_FUND
                || codeType == QuoteConstants.BOURSE_STOCK_SZ_ETF_FUND) {
            return true;
        }
        return false;
    }

    public Realtime settingRealtimeData(RealTimeData realTimeData, CodeInfo codeInfo) {
        Realtime realtime = new Realtime();
        if (null != realTimeData) {
            if (null != codeInfo) {
                int mCodeType = codeInfo.getCodeType();
                if (mCodeType == QuoteConstants.BOURSE_STOCK_GD_KSH) {
                    mCodeType = QuoteConstants.BOURSE_STOCK_KSH;
                }
                realtime.getStock().setCodeType(mCodeType);//股票类型
                realtime.getStock().setStockcode(codeInfo.getCode());//股票代码
            }
            if (null != realTimeData.getOtherData()) {
                realtime.setSecond(realTimeData.getOtherData().getSecond());//交易时间内当前秒
                realtime.setTradeMinutes(realTimeData.getOtherData().getTime());//据开盘时间的分钟数
                realtime.setCurrent(realTimeData.getOtherData().getCurrent());//现量
                realtime.setInside(realTimeData.getOtherData().getInside());//内盘
                realtime.setOutside(realTimeData.getOtherData().getOutside());//外盘
            }

            if (null == realTimeData.getData()) {
                realtime.setPriceChange("0.00");
                realtime.setPriceChangePercent("0.00%");
                return realtime;
            }
            realtime.setTotalVolume(realTimeData.getData().getTotal());//总成交量
            double mNewPrice = realTimeData.getData().getNewPrice() / priceUnit;
            if (isFund(realtime.getStock().getCodeType())) {
                mNewPrice = NumberUtil.formatFundNumber(mNewPrice);
            } else {
                mNewPrice = NumberUtil.formatNumber(mNewPrice);
            }
            realtime.setNewPrice(mNewPrice);//最新价
            realtime.setHighPrice(realTimeData.getData().getMaxPrice() / priceUnit);//最高价
            realtime.setLowPrice(realTimeData.getData().getMinPrice() / priceUnit);//最低价
            realtime.setOpenPrice(realTimeData.getData().getOpen() / priceUnit);//开盘价
            realtime.setHand(realTimeData.getData().getHand());//每手股数

            realtime.setNationDebtratio(realTimeData.getData().getNationDebtratio());//国债利率
            realtime.setTotalMoney(realTimeData.getData().getTotalAmount());//总成交金额


            if (null != realTimeData.getRealTimeComplement()) {
                realtime.getStock().setAliasCode(realTimeData.getRealTimeComplement().getmCode2());
                long mTotalValue = realtime.getTotalVolume() / 100;
                double mHandValue = calcChangeHand(realTimeData.getRealTimeComplement().getMfLtag(), mTotalValue);
                long mMl5DayVol = realTimeData.getRealTimeComplement().getMl5DayVol();
                if (realTimeData.getData() instanceof IndexRealTimeExt ||
                        realTimeData.getData() instanceof YRIndexRealTimeData) {
                    mMl5DayVol = mMl5DayVol / 100;
                }
                float mVolumeRatio = calcVolumeRatio(mTotalValue, realtime.getTradeMinutes(),
                        mMl5DayVol);
                realtime.setTurnoverRatio(mHandValue);//换手
                realtime.setVolumeRatio(mVolumeRatio);//量比
                realtime.setBlockName(realTimeData.getRealTimeComplement().getBlockName());

                realtime.getStock().setStockName(realTimeData.getRealTimeComplement().getCodeName());//个股票名称
                realtime.setPreClosePrice(NumberUtil.formatNumber(realTimeData.getRealTimeComplement().getPreClose() / priceUnit));//昨收
                realtime.setHighLimitPrice(realTimeData.getRealTimeComplement().getUpPrice() / priceUnit);//涨停
                realtime.setLowLimitPrice(realTimeData.getRealTimeComplement().getDownPrice() / priceUnit);//跌停
                if (realtime.getNewPrice() == 0) {
                    realtime.setNewPrice(realtime.getNewPrice());
                    if (realTimeData.getData() instanceof StockRealTimeExt) {
                        StockRealTimeExt stockRealTimeExt = (StockRealTimeExt) realTimeData.getData();
                        if (stockRealTimeExt.getStockOther().getStopFlag() == 1) {
                            realtime.setNewPrice(realtime.getPreClosePrice());
                        }
                    }
                    realtime.setPriceChange("0.00");
                    realtime.setPriceChangePercent("0.00%");
                } else {
                    double mPriceChange = calcPriceChange(realtime.getNewPrice(), realtime.getPreClosePrice());
                    double mPriceChangePercent = calcPriceChangePercent(mPriceChange, realtime.getPreClosePrice());
                    realtime.setPriceChangePercent(NumberUtil.getNumA(mPriceChangePercent, 2) + "%");//涨跌幅
                    realtime.setPriceChange(NumberUtil.getNumA(mPriceChange, 2));//涨跌额
                }

                double mAmplitude = (realtime.getHighPrice() - realtime.getLowPrice()) / realtime.getPreClosePrice() * 100;
                realtime.setAmplitude(mAmplitude);//振幅
            }


            if (realTimeData.getData() instanceof YRStockRealTimeData) {
                realtime.setFiveRangeData(settingFiveRange((YRStockRealTimeData) realTimeData.getData()));//五档
            } else if (realTimeData.getData() instanceof StockRealTimeExt) {
                StockRealTimeExt stockRealTimeExt = (StockRealTimeExt) realTimeData.getData();
                realtime.setFiveRangeData(settingFiveRange(stockRealTimeExt.getStockRealTime()));
                realtime.setTradeStatus((int) stockRealTimeExt.getStockOther().getStopFlag());
                realtime.setFiveSpeedUp(stockRealTimeExt.getStockOther().getSpeed().getSpeedUp());//涨速
                realtime.setMainFundFlow(stockRealTimeExt.getStockOther().getMainFundFlow());
                realtime.setFundFlow3(stockRealTimeExt.getStockOther().getFundFlow3());
                realtime.setFundFlow5(stockRealTimeExt.getStockOther().getFundFlow5());
                realtime.setFundFlow10(stockRealTimeExt.getStockOther().getFundFlow10());
                YRStockAfterRealTimeExt yrStockAfterRealTimeExt = stockRealTimeExt.getStockAfterRealTimeExt();
                if (null != yrStockAfterRealTimeExt) {
                    KshAfterData kshAfterData = new KshAfterData();
                    kshAfterData.setATPMoney(yrStockAfterRealTimeExt.getATPMoney());
                    kshAfterData.setATPTotal(yrStockAfterRealTimeExt.getATPTotal());
                    kshAfterData.setClosePrice(yrStockAfterRealTimeExt.getClosePrice() / priceUnit);
                    kshAfterData.setCodeInfo(yrStockAfterRealTimeExt.getCodeInfo());
                    kshAfterData.setCurrentMin(yrStockAfterRealTimeExt.getCurrentMin());
                    kshAfterData.setIsATP(yrStockAfterRealTimeExt.getIsATP());
                    kshAfterData.setSecond(yrStockAfterRealTimeExt.getSecond());
                    kshAfterData.setMoney(yrStockAfterRealTimeExt.getMoney());
                    kshAfterData.setTotal(yrStockAfterRealTimeExt.getTotal());
                    kshAfterData.setStockAfterRealTimeExtOther(yrStockAfterRealTimeExt.getHsStockAfterRealTimeExtOther());
                    realtime.setKshAfterData(kshAfterData);
                }
            } else if (realTimeData.getData() instanceof IndexRealTimeExt ||
                    realTimeData.getData() instanceof YRIndexRealTimeData) {

                YRIndexRealTimeData indexRealTimeExt = null;
                if (realTimeData.getData() instanceof IndexRealTimeExt) {
                    indexRealTimeExt = ((IndexRealTimeExt) realTimeData.getData()).getIndexRealTime();
                } else if (realTimeData.getData() instanceof YRIndexRealTimeData) {
                    indexRealTimeExt = (YRIndexRealTimeData) realTimeData.getData();
                }
                realtime.setRiseCount(indexRealTimeExt.getRiseCount());
                realtime.setFallCount(indexRealTimeExt.getFallCount());
                realtime.setRiseTrend(indexRealTimeExt.getRiseTrend());
                realtime.setFallTrend(indexRealTimeExt.getFallTrend());
                realtime.setLead(indexRealTimeExt.getLead());
                realtime.setType(indexRealTimeExt.getType());
                realtime.setTotalStocks(indexRealTimeExt.getTotalStock());
                realtime.setTotalStock2(indexRealTimeExt.getTotalStock2());
                realtime.setADL(indexRealTimeExt.getAdl());
                realtime.setSellCount(indexRealTimeExt.getSellCount1());
                realtime.setBuyCount(indexRealTimeExt.getBuyCount1());
            }
            if (null != realTimeData.getFinancialZEntity()) {
                realtime.setFinancialZEntity(realTimeData.getFinancialZEntity());//财务数据
            }
        } else {
            setRealtimeDefaultValue(realtime);
        }
        return realtime;
    }

    private double calcNewPrice(int newPrice) {
        double mNewPrice = newPrice / priceUnit;
        mNewPrice = NumberUtil.formatNumber(mNewPrice);
        return mNewPrice;
    }

    private double calcPriceChangePercent(double mPriceChange, double preClosePrice) {
        double mPriceChangePercent = mPriceChange / preClosePrice * 100;
        return mPriceChangePercent;
    }

    private double calcPriceChange(double newPrice, double preClosePrice) {
        double mPriceChange = newPrice - preClosePrice;
        return mPriceChange;
    }


    public static final double calcChangeHand(double mAStockValue, long totalVolume) {
        if (mAStockValue > 0) {
            BigDecimal mAStockBig = new BigDecimal(totalVolume);
            mAStockBig = mAStockBig.divide(BigDecimal.valueOf(mAStockValue), 2, BigDecimal.ROUND_HALF_UP);
            double mHandValue = mAStockBig.doubleValue();
            return mHandValue;
        }
        return 0;
    }

    public static final float calcVolumeRatio(double mTradeHand, int startMarketTime, long fiveTradeVolume) {
        if (fiveTradeVolume > 0) {
            int mUsrStartTime = startMarketTime + 1;
            BigDecimal mTradeHandBig = new BigDecimal(mTradeHand);
            mTradeHandBig = mTradeHandBig.multiply(BigDecimal.valueOf(QuoteConstants.TRADE_TIME_TOTAL));
            if (mUsrStartTime > QuoteConstants.TRADE_TIME_TOTAL) {
                mUsrStartTime = QuoteConstants.TRADE_TIME_TOTAL;
            }
            mTradeHandBig = mTradeHandBig.divide(BigDecimal.valueOf(mUsrStartTime), BigDecimal.ROUND_HALF_UP);
            mTradeHandBig = mTradeHandBig.divide(BigDecimal.valueOf(fiveTradeVolume), 2, BigDecimal.ROUND_HALF_UP);
            return mTradeHandBig.floatValue();
        }
        return 0;
    }

    private FiveRangeData settingFiveRange(YRStockRealTimeData data) {
        FiveRangeData fiveRangeData = new FiveRangeData();
        fiveRangeData.setBuyCount1(data.getBuyCount1());
        fiveRangeData.setBuyCount2(data.getBuyCount2());
        fiveRangeData.setBuyCount3(data.getBuyCount3());
        fiveRangeData.setBuyCount4(data.getBuyCount4());
        fiveRangeData.setBuyCount5(data.getBuyCount5());

        fiveRangeData.setBuyPrice1(data.getBuyPrice1() / priceUnit);
        fiveRangeData.setBuyPrice2(data.getBuyPrice2() / priceUnit);
        fiveRangeData.setBuyPrice3(data.getBuyPrice3() / priceUnit);
        fiveRangeData.setBuyPrice4(data.getBuyPrice4() / priceUnit);
        fiveRangeData.setBuyPrice5(data.getBuyPrice5() / priceUnit);

        fiveRangeData.setSellCount1(data.getSellCount1());
        fiveRangeData.setSellCount2(data.getSellCount2());
        fiveRangeData.setSellCount3(data.getSellCount3());
        fiveRangeData.setSellCount4(data.getSellCount4());
        fiveRangeData.setSellCount5(data.getSellCount5());

        fiveRangeData.setSellPrice1(data.getSellPrice1() / priceUnit);
        fiveRangeData.setSellPrice2(data.getSellPrice2() / priceUnit);
        fiveRangeData.setSellPrice3(data.getSellPrice3() / priceUnit);
        fiveRangeData.setSellPrice4(data.getSellPrice4() / priceUnit);
        fiveRangeData.setSellPrice5(data.getSellPrice5() / priceUnit);
        return fiveRangeData;
    }

    private void setRealtimeDefaultValue(Realtime realtime) {
        String DEFAULT_STR = "--";
        double DEFAULT_DOB = 0.00;
        realtime.setTradeMinutes(0);//成交分钟数
        realtime.setTradeStatus(0);  //交易状态
        realtime.setPreClosePrice(DEFAULT_DOB); //昨收价
        realtime.setOpenPrice(DEFAULT_DOB);        //今开价
        realtime.setNewPrice(DEFAULT_DOB);         //最新成交价
        realtime.setHighPrice(DEFAULT_DOB);        //最高价
        realtime.setLowPrice(DEFAULT_DOB);          //最低价
        realtime.setHighLimitPrice(DEFAULT_DOB);  //涨停价
        realtime.setLowLimitPrice(DEFAULT_DOB); //跌停价

        realtime.setTradeNumber(0);   //成交笔数
        realtime.setTotalVolume(0);//总成交量
        realtime.setTotalMoney(0);    //总成交金额
        realtime.setCurrent(0);      //现手 （最近成交量）
        realtime.setInside(0);    //内盘成交量
        realtime.setOutside(0);
        realtime.setHand(0); //每手 股数
        //涨跌幅/额
        realtime.setPriceChange(DEFAULT_STR);        //涨跌额
        realtime.setPriceChangePercent(DEFAULT_STR); //涨跌幅     float
        //委买档位- H5 沪深使用
    }


    @Override
    public void loadRealTimeExt(ArrayList<Stock> stockArrayList, final Handler handler) {
        ArrayList<IQuoteRequest> quoteRequestArrayList = buildRequest(stockArrayList);
        IQuoteRequest realTimeExtData = buildRequestParams.buildRealTimeExt(quoteRequestArrayList);
        YRMarket.getInstance().sendMsg(realTimeExtData, ansInterface -> {
            try {
                AnsRealTime ansRealTime = (AnsRealTime) ansInterface;
                List<Realtime> realtimeList = new ArrayList<>();
                for (RealTimeData realTimeDataItem : ansRealTime.getRealTimeData()) {
                    if (null != realTimeDataItem) {
                        Realtime realtime = settingRealtimeData(realTimeDataItem, realTimeDataItem.getCodeInfo());
                        realtimeList.add(realtime);
                    }
                }
//                Logger.d(realtimeList);
                sendMessage(realtimeList, handler, ansRealTime.getDataHead().getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void autoPushRealTime(ArrayList<Stock> stockArrayList, final Handler handler) {
        ArrayList<IQuoteRequest> quoteRequestArrayList = buildRequest(stockArrayList);
        AskData autoPushData = buildRequestParams.buildAutoPushRealTime(quoteRequestArrayList);
        YRMarket.getInstance().sendMsg(autoPushData, ansInterface -> {
            try {
                AnsRealTime ansRealTime = buildResponseParams.buildRealTimeInfo(ansInterface);
                List<Realtime> realtimeList = new ArrayList<>();
                for (RealTimeData realTimeDataItem : ansRealTime.getRealTimeData()) {
                    if (null != realTimeDataItem) {
                        Realtime realtime = settingRealtimeData(realTimeDataItem, realTimeDataItem.getCodeInfo());
                        realtimeList.add(realtime);
                    }
                }
                EventBus.getDefault().post(new RealTimeEvent(realtimeList));
//                sendMessage(realtimeList, handler, ansRealTime.getDataHead().getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void autoPushRealTimeExt(ArrayList<Stock> stockArrayList, short subscriptionModel, final Handler handler) {
        ArrayList<IQuoteRequest> quoteRequestArrayList = buildRequest(stockArrayList);
        AskData autoPushData = buildRequestParams.buildAutoPushRealTimeExt(quoteRequestArrayList);
        autoPushData.setOption(subscriptionModel);
        YRMarket.getInstance().sendMsg(autoPushData, ansInterface -> {
            try {
                AnsRealTime ansRealTime = buildResponseParams.buildRealTimeExtInfo(ansInterface);
                List<Realtime> realtimeList = new ArrayList<>();
                for (RealTimeData realTimeDataItem : ansRealTime.getRealTimeData()) {
                    if (null != realTimeDataItem) {
                        Realtime realtime = settingRealtimeData(realTimeDataItem, realTimeDataItem.getCodeInfo());
                        realtimeList.add(realtime);
                    }
                }
                sendMessage(realtimeList, handler, ansRealTime.getDataHead().getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void marketEventPush(ReqMarketMonitor reqMarketMonitor,short isSubscription, Handler handler) {
        AskData marketEventPushData = buildRequestParams.buildMarketEventPush(reqMarketMonitor);
        marketEventPushData.setOption(isSubscription);
        YRMarket.getInstance().sendMsg(marketEventPushData, ansInterface -> {
            try {
                AnsMarketEvent ansMarketEvent = buildResponseParams.buildMarketEvent(ansInterface);
//                Logger.d(ansMarketEvent);
                sendMessage(ansMarketEvent, handler, ansMarketEvent.getDataHead().getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void dateMarketEvent(ReqDateMarketEvent reqDateMarketEvent, Handler handler) {
        AskData dateMarketEventData = buildRequestParams.buildMarketDateMarketEvent(reqDateMarketEvent);
        YRMarket.getInstance().sendMsg(dateMarketEventData, ansInterface -> {
            try {
                AnsMarketEvent ansReportData = buildResponseParams.buildMarketEvent(ansInterface);
//                Logger.d(ansReportData);
                sendMessage(ansReportData, handler, ansReportData.getDataHead().getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void cancelAutoPushRealTime(Handler handler) {
        AskData cancelAutoPushData = buildRequestParams.buildCancelAutoPush();
        YRMarket.getInstance().sendMsg(cancelAutoPushData, ansInterface -> {

        });
    }

    @Override
    public void cancelAutoPushRealTimeExt(Handler handler) {
        AskData cancelAutoPushData = buildRequestParams.buildCancelAutoPushExt();
        YRMarket.getInstance().sendMsg(cancelAutoPushData, ansInterface -> {

        });
    }

    @Override
    public void reportSort(short marketType, int sortTypeId, int begin, int count, boolean sortDirection, ArrayList<CodeInfo> codeInfoArrayList, final Handler handler) {
        AskData reportSortData = buildRequestParams.buildReportSort(marketType, sortTypeId, begin, count, sortDirection, (byte) 1, codeInfoArrayList);
        YRMarket.getInstance().sendMsg(reportSortData, ansInterface -> {
            try {
                AnsReportData ansReportData = buildResponseParams.buildReportSortInfo(ansInterface);
                List<Realtime> realtimeList = new ArrayList<>();
                for (RealTimeData realTimeDataItem : ansReportData.getRealTimeData()) {
                    if (null != realTimeDataItem) {
                        Realtime realtime = settingRealtimeData(realTimeDataItem, realTimeDataItem.getCodeInfo());
                        realtimeList.add(realtime);
                    }
                }
//                Logger.d(realtimeList);
                sendMessage(realtimeList, handler, ansReportData.getDataHead().getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void generalSortEx(short marketType, short sortType, int begin, int count, final Handler handler) {
        AskData generalSortData = buildRequestParams.buildGeneralSortEx(marketType, sortType, begin, count);
        YRMarket.getInstance().sendMsg(generalSortData, new OnMessageCallback() {
            @Override
            public void onResponse(AnsInterface ansInterface) {
                try {
                    AnsGeneralSort ansGeneralSort = buildResponseParams.buildGeneralSortInfo(ansInterface);
                    //sendMessage(ansGeneralSort, handler, ansGeneralSort.getDataHead().getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void loadTrend(Stock codeInfo, final Handler handler) {
        IQuoteRequest iQuoteRequest = buildCodeInfo(codeInfo);
        final AskData trendData = buildRequestParams.buildTrend(iQuoteRequest);
        YRMarket.getInstance().sendMsg(trendData, new OnMessageCallback() {
            @Override
            public void onResponse(AnsInterface ansInterface) {
                try {
                    AnsTrendData trendExtData = buildResponseParams.buildTrendInfo(ansInterface);
                    if (null != trendExtData) {
                        boolean isPanelOpen = true;
                        if (trendExtData.getOtherData() != null) {
                            String mTime = String.valueOf(trendExtData.getOtherData().getData2());
                            if (mTime.startsWith(NINE_STR)) {
                                mTime = mTime.substring(1, 3);
                                Integer mSmallTime = Integer.parseInt(mTime);
                                if (mSmallTime < 30) {
                                    isPanelOpen = false;
                                }
                            }
                        }
                        List<TrendDataModel> trendDataModelList = new ArrayList<>();
                        long beforeVol = 0;
                        long vol;
                        double sumPrice = 0;
                        Calendar calendar = Calendar.getInstance();
                        Date mStartDate = DateUtil.formatDate(Constant.START_TIME, DateUtil.DEFAULT_MS_SHOW_STYLE);
                        calendar.setTime(mStartDate);
                        RealTimeComplement realTimeComplement = CommUtil.settingNeedCalc(codeInfo.getStockcode());
                        float mPreClosePrice = null != realTimeComplement ? realTimeComplement.getPreClose() : 0;
                        TrendExtEntity mTrendExtEntity = new TrendExtEntity();
                        mTrendExtEntity.setPreClosePrice((float) (mPreClosePrice / priceUnit));

                        if (null != trendExtData.getStockRealTime()) {
                            double mNewPrice = calcNewPrice(trendExtData.getStockRealTime().getNewPrice());
                            mTrendExtEntity.setNewPrice(mNewPrice);
                            double mMaxPrice = calcNewPrice(trendExtData.getStockRealTime().getMaxPrice());
                            if (mMaxPrice == 0) {
                                mMaxPrice = mNewPrice;
                            }
                            mTrendExtEntity.setStockCode(codeInfo.getStockcode());
                            mTrendExtEntity.setMaxPrice((float) mMaxPrice);
                            double mMinPrice = calcNewPrice(trendExtData.getStockRealTime().getMinPrice());
                            mTrendExtEntity.setMinPrice((float) mMinPrice);
                            double mPriceChange = calcPriceChange(mNewPrice, mTrendExtEntity.getPreClosePrice());
                            mTrendExtEntity.setTotalAmount(trendExtData.getStockRealTime().getTotalAmount());
                            mTrendExtEntity.setPriceChange(NumberUtil.getNumA(mPriceChange, 2));
                            double mPriceChangePercent = calcPriceChangePercent(mPriceChange, mTrendExtEntity.getPreClosePrice());
                            mTrendExtEntity.setPriceChangePercent(NumberUtil.getNumA(mPriceChangePercent, 2) + "%");
                        }
                        for (int i = 0; i < trendExtData.getSize(); i++) {
                            PriceVolItem priceVolItem = trendExtData.getPriceVolItems().get(i);
                            if (null != priceVolItem) {
                                TrendDataModel trendDataModel = new TrendDataModel();

                                long currentVol = priceVolItem.getTotal();
                                if (priceVolItem.getNewPrice() == 0) {
                                    priceVolItem.setNewPrice((long) (mPreClosePrice));
                                }
                                if (i == 0) {
                                    vol = currentVol;
                                } else {
                                    vol = currentVol - beforeVol;
                                }
                                trendDataModel.setPrice((float) (priceVolItem.getNewPrice() / priceUnit));
                                beforeVol = currentVol;
                                sumPrice += priceVolItem.getNewPrice() * vol;
                                vol = new BigDecimal(vol).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_DOWN).longValue();
                                trendDataModel.setTradeAmount(vol);
                                if (currentVol != 0) {
                                    BigDecimal bigDecimal = new BigDecimal(sumPrice)
                                            .divide(BigDecimal.valueOf(currentVol), 4, BigDecimal.ROUND_DOWN)
                                            .divide(BigDecimal.valueOf(priceUnit), 3, BigDecimal.ROUND_HALF_UP);

                                    trendDataModel.setAvgPrice(bigDecimal.floatValue());
                                } else {
                                    trendDataModel.setAvgPrice(trendDataModel.getPrice());
                                }
                                trendDataModel.setTime(DateUtil.optimizeDateFormat(calendar, 1));
                                trendDataModelList.add(trendDataModel);
                            }
                        }
                        if (!isPanelOpen) {
                            if (trendDataModelList.size() == 2) {
                                trendDataModelList.remove(trendDataModelList.size() - 1);
                            }
                        }
                        mTrendExtEntity.setTrendDataModelList(trendDataModelList);
//                        Logger.d(mTrendExtEntity);
                        EventBus.getDefault().post(new MinuteTrendEvent(mTrendExtEntity));
//                        sendMessage(mTrendExtEntity, handler, trendExtData.getDataHead().getType());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void loadTrendEx(Stock codeInfo, final Handler handler) {
        IQuoteRequest iQuoteRequest = buildCodeInfo(codeInfo);
        AskData trendExData = buildRequestParams.buildTrendEx(iQuoteRequest);
        YRMarket.getInstance().sendMsg(trendExData, new OnMessageCallback() {
            @Override
            public void onResponse(AnsInterface ansInterface) {
                try {
                    AnsTrendExtData trendExtData = buildResponseParams.buildTrendExtInfo(ansInterface);
                    if (null != trendExtData) {
                        List<TrendDataModel> trendDataModelList = new ArrayList<>();
                        long beforeVol = 0;
                        long vol;
                        double sumPrice = 0;
                        Calendar calendar = Calendar.getInstance();
                        Date mStartDate = DateUtil.formatDate(Constant.START_TIME, DateUtil.DEFAULT_MS_SHOW_STYLE);
                        calendar.setTime(mStartDate);
                        RealTimeComplement realTimeComplement = CommUtil.settingNeedCalc(codeInfo.getStockcode());
                        float mPreClosePrice = null != realTimeComplement ? realTimeComplement.getPreClose() : 0;
                        for (int i = 0; i < trendExtData.getSize(); i++) {
                            PriceVolItem priceVolItem = trendExtData.getPriceVolItems().get(i);
                            if (null != priceVolItem) {
                                TrendDataModel trendDataModel = new TrendDataModel();

                                long currentVol = priceVolItem.getTotal();
                                if (i == 0) {
                                    if (priceVolItem.getNewPrice() == 0) {
                                        priceVolItem.setNewPrice((long) (mPreClosePrice));
                                    }
                                    vol = currentVol;
                                } else {
                                    vol = currentVol - beforeVol;
                                }
                                trendDataModel.setPrice((float) (priceVolItem.getNewPrice() / priceUnit));
                                beforeVol = currentVol;
                                sumPrice += priceVolItem.getNewPrice() * vol;
                                vol = new BigDecimal(vol).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_DOWN).longValue();
                                trendDataModel.setTradeAmount(vol);
                                if (currentVol != 0) {
                                    BigDecimal bigDecimal = new BigDecimal(sumPrice)
                                            .divide(BigDecimal.valueOf(currentVol), 4, BigDecimal.ROUND_DOWN)
                                            .divide(BigDecimal.valueOf(priceUnit), 3, BigDecimal.ROUND_HALF_UP);
                                    trendDataModel.setAvgPrice(bigDecimal.floatValue());
                                } else {
                                    trendDataModel.setAvgPrice(trendDataModel.getPrice());
                                }
                                trendDataModel.setTime(DateUtil.optimizeDateFormat(calendar, 1));
                                trendDataModelList.add(trendDataModel);
                            }
                        }
                        sendMessage(trendDataModelList, handler, trendExtData.getDataHead().getType());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadHistoryTrend(Stock stock, int requestTime, Handler handler) {
        IQuoteRequest iQuoteRequest = buildCodeInfo(stock);
        try {
            ReqHisTrend mReqHisTrend = new ReqHisTrend(iQuoteRequest);
            mReqHisTrend.setRequestDate(requestTime);
            AskData trendExData = buildRequestParams.buildHistoryTrend(mReqHisTrend);
            YRMarket.getInstance().sendMsg(trendExData, ansInterface -> {
                try {
                    AnsHisTrend mAnsLeadData = buildResponseParams.buildHistoryTrendInfo(ansInterface);
                    if (null != mAnsLeadData) {
                        List<TrendDataModel> trendDataModelList = new ArrayList<>();
                        long beforeVol = 0;
                        long vol;
                        double sumPrice = 0;
                        Calendar calendar = Calendar.getInstance();
                        Date mStartDate = DateUtil.formatDate(Constant.START_TIME, DateUtil.DEFAULT_MS_SHOW_STYLE);
                        calendar.setTime(mStartDate);
                        StockHistoryTrendHead mStockHistoryTrendHead = mAnsLeadData.getHisTrendData().getStockHistoryTrendHead();
                        HisTrendExtEntity mTrendExtEntity = new HisTrendExtEntity();
                        float mPreClosePrice = null != mStockHistoryTrendHead ? mStockHistoryTrendHead.getPreClosePrice() : 0;
                        mTrendExtEntity.setPreClosePrice((float) (mPreClosePrice / priceUnit));
                        mTrendExtEntity.setHistoryTime(mStockHistoryTrendHead.getRequestDate());
                        if (null != mStockHistoryTrendHead.getRealTime()) {
                            double mNewPrice = calcNewPrice(mStockHistoryTrendHead.getRealTime().getNewPrice());
                            mTrendExtEntity.setNewPrice(mNewPrice);
                            double mMaxPrice = calcNewPrice(mStockHistoryTrendHead.getRealTime().getMaxPrice());
                            if (mMaxPrice == 0) {
                                mMaxPrice = mNewPrice;
                            }
                            mTrendExtEntity.setMaxPrice((float) mMaxPrice);
                            double mMinPrice = calcNewPrice(mStockHistoryTrendHead.getRealTime().getMinPrice());
                            mTrendExtEntity.setMinPrice((float) mMinPrice);
                            double mPriceChange = calcPriceChange(mNewPrice, mTrendExtEntity.getPreClosePrice());
                            mTrendExtEntity.setTotalAmount(mStockHistoryTrendHead.getRealTime().getTotalAmount());
                            mTrendExtEntity.setPriceChange(NumberUtil.getNumA(mPriceChange, 2));
                            double mPriceChangePercent = calcPriceChangePercent(mPriceChange, mTrendExtEntity.getPreClosePrice());
                            mTrendExtEntity.setPriceChangePercent(NumberUtil.getNumA(mPriceChangePercent, 2) + "%");
                        }

                        for (int i = 0; i < mStockHistoryTrendHead.getSize(); i++) {
                            StockCompHistoryData priceVolItem = mAnsLeadData.getHisTrendData().getStockCompHistoryData().get(i);
                            if (null != priceVolItem) {
                                TrendDataModel trendDataModel = new TrendDataModel();
                                long currentVol = priceVolItem.getTotal();
                                if (priceVolItem.getNewPrice() == 0) {
                                    priceVolItem.setNewPrice((long) (mPreClosePrice));
                                }
                                if (i == 0) {
                                    vol = currentVol;
                                } else {
                                    vol = currentVol - beforeVol;
                                }
                                trendDataModel.setPrice((float) (priceVolItem.getNewPrice() / priceUnit));
                                beforeVol = currentVol;
                                sumPrice += priceVolItem.getNewPrice() * vol;
                                vol = new BigDecimal(vol).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_DOWN).longValue();
                                trendDataModel.setTradeAmount(vol);
                                if (currentVol != 0) {
                                    BigDecimal bigDecimal = new BigDecimal(sumPrice)
                                            .divide(BigDecimal.valueOf(currentVol), 4, BigDecimal.ROUND_DOWN)
                                            .divide(BigDecimal.valueOf(priceUnit), 3, BigDecimal.ROUND_HALF_UP);
                                    trendDataModel.setAvgPrice(bigDecimal.floatValue());
                                } else {
                                    trendDataModel.setAvgPrice(trendDataModel.getPrice());
                                }
                                trendDataModel.setTime(DateUtil.optimizeDateFormat(calendar, 1));
                                trendDataModelList.add(trendDataModel);
                            }
                        }
                        mTrendExtEntity.setTrendDataModelList(trendDataModelList);
                        sendMessage(mTrendExtEntity, handler, mAnsLeadData.getDataHead().getType());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void loadTrendLead(Stock codeInfo, Handler handler) {
        IQuoteRequest iQuoteRequest = buildCodeInfo(codeInfo);
        AskData trendLeadExData = buildRequestParams.buildTrendLead(iQuoteRequest);
        YRMarket.getInstance().sendMsg(trendLeadExData, new OnMessageCallback() {
            @Override
            public void onResponse(AnsInterface ansInterface) {
                try {
                    AnsLeadData trendLeadData = buildResponseParams.buildTrendLeadInfo(ansInterface);
                    if (null != trendLeadData) {
                        List<TrendDataModel> trendDataModelList = new ArrayList<>();
                        long beforeVol = 0;
                        long vol;
                        Calendar calendar = Calendar.getInstance();
                        Date mStartDate = DateUtil.formatDate(Constant.START_TIME, DateUtil.DEFAULT_MS_SHOW_STYLE);
                        calendar.setTime(mStartDate);
                        RealTimeComplement realTimeComplement = CommUtil.settingNeedCalc(codeInfo.getStockcode());
                        TrendExtEntity mTrendExtEntity = new TrendExtEntity();
                        float mPreClosePrice = null != realTimeComplement ? realTimeComplement.getPreClose() : 0;
                        mTrendExtEntity.setPreClosePrice((float) (mPreClosePrice / priceUnit));
                        if (null != trendLeadData.getRealTime()) {
                            double mNewPrice = calcNewPrice(trendLeadData.getRealTime().getNewPrice());
                            mTrendExtEntity.setNewPrice(mNewPrice);
                            double mMaxPrice = calcNewPrice(trendLeadData.getRealTime().getMaxPrice());
                            if (mMaxPrice == 0) {
                                mMaxPrice = mNewPrice;
                            }
                            mTrendExtEntity.setStockCode(codeInfo.getStockcode());
                            mTrendExtEntity.setMaxPrice((float) mMaxPrice);
                            double mMinPrice = calcNewPrice(trendLeadData.getRealTime().getMinPrice());
                            mTrendExtEntity.setMinPrice((float) mMinPrice);
                            double mPriceChange = calcPriceChange(mNewPrice, mTrendExtEntity.getPreClosePrice());
                            mTrendExtEntity.setTotalAmount(trendLeadData.getRealTime().getTotalAmount());
                            mTrendExtEntity.setPriceChange(NumberUtil.getNumA(mPriceChange, 2));
                            double mPriceChangePercent = calcPriceChangePercent(mPriceChange, mTrendExtEntity.getPreClosePrice());
                            mTrendExtEntity.setPriceChangePercent(NumberUtil.getNumA(mPriceChangePercent, 2) + "%");
                        }
                        for (int i = 0; i < trendLeadData.getHistoryLength(); i++) {
                            StockLeadData priceVolItem = trendLeadData.getStockLeadDataList().get(i);
                            if (null != priceVolItem) {
                                TrendDataModel trendDataModel = new TrendDataModel();
                                long currentVol = priceVolItem.getTotal();
                                if (i == 0) {
                                    if (priceVolItem.getNewPrice() == 0) {
                                        priceVolItem.setNewPrice((long) (mPreClosePrice));
                                    }
                                    vol = currentVol;
                                } else {
                                    vol = currentVol - beforeVol;
                                }
                                trendDataModel.setPrice((float) (priceVolItem.getNewPrice() / priceUnit));
                                double mLeadPrice = ((double) priceVolItem.getLead()) / HALF_UNIT + 1;
                                trendDataModel.setAvgPrice((float) (mLeadPrice * mPreClosePrice / priceUnit));
                                beforeVol = currentVol;
                                vol = new BigDecimal(vol).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_DOWN).longValue();
                                trendDataModel.setTradeAmount(vol);
                                trendDataModel.setTime(DateUtil.optimizeDateFormat(calendar, 1));
                                trendDataModelList.add(trendDataModel);
                            }
                        }
                        mTrendExtEntity.setTrendDataModelList(trendDataModelList);
                        sendMessage(mTrendExtEntity, handler, trendLeadData.getDataHead().getType());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadFinance(ArrayList<Stock> stockArrayList, final Handler handler) {
        ArrayList<IQuoteRequest> codeInfo = buildRequest(stockArrayList);
        AskData financeData = buildRequestParams.buildFinance(codeInfo);
        YRMarket.getInstance().sendMsg(financeData, ansInterface -> {
            try {
                AnsFinanceData ansFinanceData = buildResponseParams.buildFinanceInfo(ansInterface);
                sendMessage(ansFinanceData, handler, QuoteConstants.RT_SIMPLE_FINANCE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void loadLimitTick(final ReqLimitTick codeInfo, final Handler handler) {
        AskData limitTickData = buildRequestParams.buildLimitTick(codeInfo);
        YRMarket.getInstance().sendMsg(limitTickData, new OnMessageCallback() {
            @Override
            public void onResponse(AnsInterface ansInterface) {
                try {
                    AnsStockTick ansStockTick = buildResponseParams.buildLimitTickInfo(ansInterface);
                    StockTickDetail stockTickDetail = new StockTickDetail();
                    Stock stock = new Stock();
                    if (!TextUtils.isEmpty(codeInfo.getCodeInfo().getCode())) {
                        stock.setStockcode(codeInfo.getCodeInfo().getCode());
                    }
                    stock.setCodeType(codeInfo.getCodeInfo().getCodeType());
                    stockTickDetail.setStock(stock);
                    Date mStartDate = DateUtil.formatDate(Constant.START_TIME, DateUtil.DEFAULT_MS_SHOW_STYLE);
                    List<CommonTick> stockTickInfoList = new ArrayList<>();
                    int mIndex = 0;
                    for (StockTick stockTick : ansStockTick.getData()) {
                        if (null != stockTick) {
                            StockTickInfo stockTickInfo = new StockTickInfo();
                            stockTickInfo.setSecond(stockTick.getSecond());
                            stockTickInfo.setChicang(stockTick.getChicang());
                            stockTickInfo.setVolume(stockTick.getVolume());
                            BigDecimal unitBigDecimal = BigDecimal.valueOf(priceUnit);
                            BigDecimal sellBigDecimal = new BigDecimal(stockTick.getSellPrice())
                                    .divide(unitBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
                            stockTickInfo.setSellPrice(sellBigDecimal.doubleValue());
                            BigDecimal buyBigDecimal = new BigDecimal(stockTick.getBuyPrice())
                                    .divide(unitBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
                            stockTickInfo.setBuyPrice(buyBigDecimal.doubleValue());
                            BigDecimal priceBigDecimal = new BigDecimal(stockTick.getPrice())
                                    .divide(unitBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
                            stockTickInfo.setPrice(priceBigDecimal.doubleValue());
                            stockTickInfo.setTime(DateUtil.optimizeDateMoreFormat(mStartDate, stockTick.getTime()));
                            if (mIndex != 0) {
                                CommonTick mPreStockTickInfo = stockTickInfoList.get(mIndex - 1);
                                if (null != mPreStockTickInfo) {
                                    byte mBuyOrSell = setPriceState(mPreStockTickInfo.getBuyPrice(),
                                            mPreStockTickInfo.getSellPrice(), stockTickInfo.getPrice(),
                                            stockTickInfo.getBuyPrice(), stockTickInfo.getSellPrice());
                                    stockTickInfo.setBuyOrSell(mBuyOrSell);
                                }
                            }
                            stockTickInfoList.add(stockTickInfo);
                            mIndex++;
                        }
                    }

                    stockTickDetail.setStockTickInfoList(stockTickInfoList);
                    Log.d(TAG,String.format("明细数量:%d;Count=%d",stockTickDetail.getStockTickInfoList().size(), codeInfo.getCount()));
                    MinuteDealDetailEvent event = new MinuteDealDetailEvent();
                    event.setStockTickDetail(stockTickDetail);
                    EventBus.getDefault().post(event);
//                    sendMessage(stockTickDetail, handler, QuoteConstants.RT_LIMITTICK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Deprecated
    @Override
    public void loadTickSearch(ReqTransSearch reqTransSearch, final Handler handler) {
        AskData tickSearchData = buildRequestParams.buildTickSearch(reqTransSearch);
        YRMarket.getInstance().sendMsg(tickSearchData, ansInterface -> {
            try {
                AnsTransSearch ansTransSearch = buildResponseParams.buildTranSearchInfo(ansInterface);
                StockTickDetail stockTickDetail = new StockTickDetail();
                Stock stock = new Stock();
                if (!TextUtils.isEmpty(reqTransSearch.getCodeInfo().getCode())) {
                    stock.setStockcode(reqTransSearch.getCodeInfo().getCode());
                }
                stock.setCodeType(reqTransSearch.getCodeInfo().getCodeType());
                stockTickDetail.setStock(stock);
                Date mStartDate = DateUtil.formatDate(Constant.START_TIME, DateUtil.DEFAULT_MS_SHOW_STYLE);
                List<CommonTick> stockTickInfoList = new ArrayList<>();
                int mIndex = 0;
                int mTotalLength = ansTransSearch.getTotalSize() - 1;
                for (StockTick stockTick : ansTransSearch.getTickList()) {
                    if (null != stockTick) {
                        CommonTick stockTickInfo = new StockTickInfo();
                        stockTickInfo.setBuyOrSell(stockTick.getBuyOrSell());
                        stockTickInfo.setSecond(stockTick.getSecond());
                        stockTickInfo.setChicang(stockTick.getChicang());
                        stockTickInfo.setVolume(stockTick.getVolume());
                        BigDecimal unitBigDecimal = BigDecimal.valueOf(priceUnit);
                        BigDecimal sellBigDecimal = new BigDecimal(stockTick.getSellPrice())
                                .divide(unitBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
                        stockTickInfo.setSellPrice(sellBigDecimal.doubleValue());
                        BigDecimal buyBigDecimal = new BigDecimal(stockTick.getBuyPrice())
                                .divide(unitBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
                        stockTickInfo.setBuyPrice(buyBigDecimal.doubleValue());
                        BigDecimal priceBigDecimal = new BigDecimal(stockTick.getPrice())
                                .divide(unitBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
                        stockTickInfo.setPrice(priceBigDecimal.doubleValue());
                        stockTickInfo.setTime(DateUtil.optimizeDateMoreFormat(mStartDate, stockTick.getTime()));
                        if (mIndex != 0) {
                            CommonTick mPreStockTickInfo = stockTickInfoList.get(mIndex - 1);
                            if (null != mPreStockTickInfo) {
                                byte mBuyOrSell = setPriceState(stockTickInfo.getBuyPrice(), stockTickInfo.getSellPrice(),
                                        mPreStockTickInfo.getPrice(), mPreStockTickInfo.getBuyPrice(),
                                        mPreStockTickInfo.getSellPrice());
                                stockTickInfoList.get(mIndex - 1).setBuyOrSell(mBuyOrSell);
                            }
                        }
                        stockTickInfoList.add(stockTickInfo);
                    }
                    mIndex++;
                }
                stockTickDetail.setStockTickInfoList(stockTickInfoList);
                sendMessage(stockTickDetail, handler, ansTransSearch.getDataHead().getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private byte setPriceState(double mPreBuyPrice, double mPreSellPrice, double mNewPrice, double mBuyPrice, double mSellPrice) {
        byte mBuySell = 2;
        if (mPreBuyPrice != 0) {
            if (mNewPrice <= mPreBuyPrice) {
                mBuySell = 0;
            } else if (mNewPrice >= mPreSellPrice) {
                mBuySell = 1;
            } else {
                if (mNewPrice <= mBuyPrice) {
                    mBuySell = 0;
                } else if (mNewPrice >= mSellPrice) {
                    mBuySell = 1;
                }
            }
        } else {
            if (mNewPrice <= mBuyPrice) {
                mBuySell = 0;
            } else if (mNewPrice >= mSellPrice) {
                mBuySell = 1;
            }
        }
        return mBuySell;
    }

    /**
     * @param stock     股票信息
     * @param period    K线类型 {@link QuoteConstants #PERIOD_TYPE_MINUTE1、PERIOD_TYPE_DAY...}
     * @param remitMode 复权类型 {@link QuoteConstants # BEFORE_REMIT_MODE(前复权) AFTER_REMIT_MODE(后复权) NO_REMIT_MODE(不复权) }
     * @param offset    K线按偏移使用 起始位置，-1表示当前位置，向前索引
     * @param count     申请个数，向前取值
     * @param beginTime K线按时间使用 日线、周线、月线的日期格式为YYYYMMDD 分钟K线的格式为YYMMDDhhmm，
     *                  YY为年份 - 1990；如2012年9月13日14点10分的1分钟K线的日期为2209131410
     * @param endTime
     * @param handler
     */
    @Override
    public void loadKLine(final Stock stock, short period, short remitMode, int offset, int count, long beginTime, long endTime, final Handler handler) {
        if (stock.getCodeType() == QuoteConstants.BOURSE_STOCK_GD_KSH) {
            stock.setCodeType(QuoteConstants.BOURSE_STOCK_KSH);
        }
        stock.setStockcode(stock.getStockcode());
        IQuoteRequest quoteRequest = buildRequestParams.buildKLine(stock, period, remitMode, offset, count, beginTime, endTime);

        YRMarket.getInstance().sendMsg(quoteRequest, new OnMessageCallback() {
            @Override
            public void onResponse(AnsInterface ansInterface) {
                try {
                    AnsDayData answerData = (AnsDayData) buildResponseParams.buildKLineInfo(ansInterface);
                    if (null != answerData && null != answerData.getData()
                            && answerData.getData().size() > 0) {
                        List<StockKLine> stockKLineList = new ArrayList<>();
                        for (StockCompDayDataEx stockCompDayDataEx : answerData.getData()) {
                            StockKLine mStockKLine = calcKlineValue(stockCompDayDataEx);
                            if (null != mStockKLine) {
                                stockKLineList.add(mStockKLine);
                            }
                        }
                        KlineEntity mKlineEntity = new KlineEntity();
                        CodeInfo mCodeInfo = answerData.getDataHead().getPrivateKey();
                        if (null != mCodeInfo) {
                            mKlineEntity.setStockCode(mCodeInfo.getCode());
                            mKlineEntity.setStockCodeType(mCodeInfo.getCodeType());
                        }
                        mKlineEntity.setStockKLineList(stockKLineList);
                        List<StockKLine> kLineList =  mKlineEntity.getStockKLineList();
                        Collections.reverse(kLineList);
                        EventBus.getDefault().post(new DailyKLineEvent(mKlineEntity, offset, count, period));
//                        sendMessage(mKlineEntity, handler, QuoteConstants.RT_TECHDATA_EX);
//                        KlineMACD klineMACD = new KlineMACD(stockKLineList);
//                        KlineKDJ klineKDJ = new KlineKDJ(stockKLineList);
//                        KlinePSY klinePSY = new KlinePSY(stockKLineList);
//                        KlineVOL klineVOL = new KlineVOL(stockKLineList);
//                        KlineWR klineWR = new KlineWR(stockKLineList);
//                        JSONArray jsonArray = new JSONArray();

//                        for (int i = stockKLineList.size()-1; i >=0; i--) {
//                            JSONObject jsonObjectWR = new JSONObject();
//                            jsonObjectWR.put("WR", klineWR.getWR(KlineWR.PARAM_VALUE[0], i));
//                            jsonObjectWR.put("date", stockKLineList.get(i).date);
//                            jsonArray.put(jsonObjectWR);
//                            JSONObject jsonObjectDay = new JSONObject();
//                            jsonObjectDay.put("5日", klineVOL.getVOL(5,i));
//                            jsonObjectDay.put("10日", klineVOL.getVOL(10,i));
//                            jsonObjectDay.put("20日", klineVOL.getVOL(20,i));
//                            jsonObjectDay.put("date", stockKLineList.get(i).date);
//                            jsonArray.put(jsonObjectDay);
//                            JSONObject jsonObjectPsy = new JSONObject();
//                            jsonObjectPsy.put("PSY", klinePSY.getPSY(i));
//                            jsonObjectPsy.put("PSYMA", klinePSY.getPSYMA(i));
//                            jsonObjectPsy.put("date", stockKLineList.get(i).date);
//                            jsonArray.put(jsonObjectPsy);
//                            JSONObject jsonObjectKdj = new JSONObject();
//                            jsonObjectKdj.put("K",klineKDJ.getKData(i));
//                            jsonObjectKdj.put("D",klineKDJ.getDData(i));
//                            jsonObjectKdj.put("J",klineKDJ.getJData(i));
//                            jsonObjectKdj.put("date", stockKLineList.get(i).date);
//                            jsonArray.put(jsonObjectKdj);
//                            JSONObject jsonObjectMacd = new JSONObject();
//                            jsonObjectMacd.put("macd", klineMACD.getMACD(i));
//                            jsonObjectMacd.put("dea", klineMACD.getDea(i));
//                            jsonObjectMacd.put("diff", klineMACD.getDIFF(i));
//                            jsonObjectMacd.put("date", stockKLineList.get(i).date);

//                        }
//                        Log.d("566", jsonArray.toString());
                    }else {
                        Log.d(getClass().getSimpleName(), "无数据");
                        DailyKLineEvent event = new DailyKLineEvent(null, offset, count, period);
                        event.setEnd(true);
                        EventBus.getDefault().post(event);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private StockKLine calcKlineValue(StockCompDayDataEx stockCompDayDataEx) {
        double priceUnit = DtkConfig.getInstance().getPriceUnit();
        StockKLine stockKLine = new StockKLine();
        if (stockCompDayDataEx == null) {
            stockKLine.setClosePrice(0.00);
            stockKLine.setOpenPrice(0.00);
            stockKLine.setLowPrice(0.00);
            stockKLine.setHighPrice(0.00);
            stockKLine.setMoney(0);
            stockKLine.setVolume(0);
            stockKLine.setDate(116);
            stockKLine.setAtpMoney(0);
            stockKLine.setAtpVolume(0);
        } else {
            if (stockCompDayDataEx.getMoney() == 0
                    && stockCompDayDataEx.getTotal() == 0) {
                return null;
            }
            stockKLine.setClosePrice(calcPrice(stockCompDayDataEx.getClosePrice() / priceUnit));
            stockKLine.setOpenPrice(calcPrice(stockCompDayDataEx.getOpenPrice() / priceUnit));
            stockKLine.setLowPrice(calcPrice(stockCompDayDataEx.getMinPrice() / priceUnit));
            stockKLine.setHighPrice(calcPrice(stockCompDayDataEx.getMaxPrice() / priceUnit));
            stockKLine.setMoney(stockCompDayDataEx.getMoney());
            stockKLine.setVolume(stockCompDayDataEx.getTotal());
            stockKLine.setAtpMoney(stockCompDayDataEx.getATPMoney());
            stockKLine.setAtpVolume(stockCompDayDataEx.getATPTotal());
            stockKLine.setOrginDate(stockCompDayDataEx.getDate());
            if (0 != stockCompDayDataEx.getDateOfHour()) {
                String mValue = stockCompDayDataEx.getDate() + "";
                String mYear = mValue.substring(0, 2);
                String mMonthDay = mValue.substring(2, 6);
                String mMinute = mValue.substring(6);
                int mYearValue = 0;
                if (!TextUtils.isEmpty(mYear)) {
                    mYearValue = Integer.parseInt(mYear);
                    mYearValue = mYearValue + QuoteConstants.YEAR_BEGIN;
                }
                String mDate = mYearValue + mMonthDay;
                stockKLine.setDate(Integer.parseInt(mDate));
                stockKLine.setTime(Integer.parseInt(mMinute));
            } else {
                stockKLine.setDate(stockCompDayDataEx.getDate_YYYYMMDD());
            }
        }
        return stockKLine;
    }


    @Override
    public void loadExRightData(ArrayList<Stock> stockArrayList, final Handler handler) {
        ArrayList<IQuoteRequest> codeInfo = buildRequest(stockArrayList);
        AskData rxRightData = buildRequestParams.buildExRightData(codeInfo);
        YRMarket.getInstance().sendMsg(rxRightData, new OnMessageCallback() {
            @Override
            public void onResponse(AnsInterface ansInterface) {
                try {
                    AnsSimpleFile ansSimpleFile = buildResponseParams.buildExRightInfo(ansInterface);
                    //sendMessage(ansSimpleFile, handler, ansSimpleFile.getDataHead().getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadCompAskData(CompAskData compAskData, final Handler handler) {
        YRMarket.getInstance().sendMsg(compAskData, new OnMessageCallback() {
            @Override
            public void onResponse(AnsInterface ansInterface) {

            }
        });
    }

    @Override
    public void loadHQColValueData(IQuoteRequest iQuoteRequest, Handler handler) {
        AskData askData = buildRequestParams.buildHQColValueData(iQuoteRequest);
        //10022
        YRMarket.getInstance().sendMsg(askData, new OnMessageCallback() {
            @Override
            public void onResponse(AnsInterface ansInterface) {
                if (null != ansInterface) {
                    try {
                        AnsHQColValue ansHQColValue = buildResponseParams.buildHQColInfo(ansInterface);
                        JSONArray jsonArray = new JSONArray();
                        for (YRHQColItem yrhqColItem : ansHQColValue.getColItemList()) {
                            if (null != yrhqColItem) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("STOCK_TAG_HIGH_SPEED", yrhqColItem.getInt64(QuoteConstants.STOCK_TAG_HIGH_SPEED));
                                jsonObject.put("STOCK_TAG_VOL_RATIO", yrhqColItem.getInt32(QuoteConstants.STOCK_TAG_VOL_RATIO));
                                jsonObject.put("STOCK_TAG_PE_RATE", yrhqColItem.getInt32(QuoteConstants.STOCK_TAG_PE_RATE));
                                jsonObject.put("STOCK_TAG_DYN_PB_RATE", yrhqColItem.getInt32(QuoteConstants.STOCK_TAG_DYN_PB_RATE));
                                jsonObject.put("STOCK_TAG_CIRCULATION_VALUE", yrhqColItem.getInt32(QuoteConstants.STOCK_TAG_CIRCULATION_VALUE));
                                jsonObject.put("STOCK_TAG_MARKET_VALUE", yrhqColItem.getInt64(QuoteConstants.STOCK_TAG_MARKET_VALUE));
                                jsonObject.put("STOCK_TAG_PRECLOSE_PX", yrhqColItem.getInt32(QuoteConstants.STOCK_TAG_PRECLOSE_PX));
                                jsonObject.put("STOCK_TAG_AMPLITUDE", yrhqColItem.getStringData(QuoteConstants.STOCK_TAG_AMPLITUDE));
                                jsonArray.put(jsonObject);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void loadServerCalc(ArrayList<Stock> stockArrayList, final Handler handler) {
        ArrayList<IQuoteRequest> codeInfo = buildRequest(stockArrayList);
        AskData askData = buildRequestParams.buildServerCalcData(codeInfo);
        YRMarket.getInstance().sendMsg(askData, new OnMessageCallback() {
            @Override
            public void onResponse(AnsInterface ansInterface) {
                if (null != ansInterface) {
                    try {
                        AnsSeverCalculate ansSeverCalculate = buildResponseParams.buildServerCalcInfo(ansInterface);
                        //sendMessage(ansSeverCalculate, handler, ansSeverCalculate.getDataHead().getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void loadStockInfo(ArrayList<Stock> stockArrayList, Handler handler) {
        ArrayList<IQuoteRequest> codeInfo = buildRequest(stockArrayList);
        AskData askData = buildRequestParams.buildStockInfoData(codeInfo);
        YRMarket.getInstance().sendMsg(askData, ansInterface -> {
            try {
                AnsStockBaseData ansStockBaseData = buildResponseParams.buildStockInfo(ansInterface);
                sendMessage(ansStockBaseData, handler, QuoteConstants.RT_SIMPLE_STOCKINFO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void loadBeforeStockTick(Stock codeInfo, Handler handler) {
        IQuoteRequest iQuoteRequest = buildCodeInfo(codeInfo);
        AskData askData = buildRequestParams.buildBeforeStockTickData(iQuoteRequest);
        YRMarket.getInstance().sendMsg(askData, ansInterface -> {
            try {
                AnsVirtualAuction ansStockBaseData = buildResponseParams.buildStockTick(ansInterface);
                List<CommonTick> stockTickInfoList = new ArrayList<>();
                for (StockVirtualAuction stockTick : ansStockBaseData.getStockVirtualAuctions()) {
                    if (null != stockTick) {
                        StockTickInfo stockTickInfo = new StockTickInfo();
                        stockTickInfo.setIsTick(true);
                        stockTickInfo.setVirtualQuantity(Float.valueOf(stockTick.getQtyLeft()).longValue());
                        stockTickInfo.setVolume(Float.valueOf(stockTick.getQty()).longValue());
                        BigDecimal unitBigDecimal = BigDecimal.valueOf(priceUnit);
                        BigDecimal priceBigDecimal = BigDecimal.valueOf(stockTick.getPrice())
                                .divide(unitBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
                        stockTickInfo.setPrice(priceBigDecimal.doubleValue());
                        long mTime = stockTick.getTime() / 1000;
                        String mTimeStr = "0" + mTime;
                        stockTickInfo.setBuyOrSell((byte) (stockTick.getQtyLeft() >= 0 ? 1 : 0));
                        stockTickInfo.setTime(DateUtil.optimizeFormatDate(mTimeStr, DateUtil.DEFAULT_MSS_SHOW_STYLE, DateUtil.DEFAULT_M_S_S_SHOW_STYLE));
                        stockTickInfoList.add(stockTickInfo);
                    }
                }
                sendMessage(stockTickInfoList, handler, QuoteConstants.RT_VIRTUAL_AUCTION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadBeforeOtherStockTick(Stock codeInfo, Handler handler) {
        IQuoteRequest iQuoteRequest = buildCodeInfo(codeInfo);
        AskData askData = buildRequestParams.buildBeforeStockTickData(iQuoteRequest);
        YRMarket.getInstance().sendMsg(askData, ansInterface -> {
            try {
                AnsVirtualAuction ansStockBaseData = buildResponseParams.buildStockTick(ansInterface);
                List<CommonTick> stockTickInfoList = new ArrayList<>();
                for (StockVirtualAuction stockTick : ansStockBaseData.getStockVirtualAuctions()) {
                    if (null != stockTick) {
                        StockTickInfo stockTickInfo = new StockTickInfo();
                        stockTickInfo.setIsTick(true);
                        stockTickInfo.setVirtualQuantity(Float.valueOf(stockTick.getQtyLeft()).longValue());
                        stockTickInfo.setVolume(Float.valueOf(stockTick.getQty()).longValue());
                        stockTickInfo.setVirtualQuantity(Float.valueOf(stockTick.getQtyLeft()).longValue());
                        BigDecimal unitBigDecimal = BigDecimal.valueOf(priceUnit);
                        BigDecimal priceBigDecimal = BigDecimal.valueOf(stockTick.getPrice())
                                .divide(unitBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
                        stockTickInfo.setPrice(priceBigDecimal.doubleValue());
                        stockTickInfo.setChicang(stockTick.getTime());
                        stockTickInfo.setBuyOrSell((byte) (stockTick.getQtyLeft() >= 0 ? 1 : 0));
                        stockTickInfoList.add(stockTickInfo);
                    }
                }
                sendMessage(stockTickInfoList, handler, QuoteConstants.RT_VIRTUAL_AUCTION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private int getCallauctionMinByTime(long lTime) {
        int nHour = (int) (lTime / 10000000);
        int nMin = (int) ((lTime % 10000000) / 100000);
        int nSec = (int) (lTime % 100000);
        int nStarthour = 9;
        int nStartMin = 15;
        int nSpan = (nHour - nStarthour) * 60 + nMin - nStartMin;
        if (nSec > 0) {
            nSpan += 1;
        }
        if (nSpan < 0) {
            nSpan = 0;
        }
        return nSpan;
    }

    private double calcPrice(double value) {
        return NumberUtil.formatNumber(value);
    }


    protected void sendMessage(Object data, Handler handler, int msgId) {
        if (handler == null) {
            return;
        }
        Message message = handler.obtainMessage();
        message.what = msgId;
        message.obj = data;
        handler.sendMessage(message);
    }


}
