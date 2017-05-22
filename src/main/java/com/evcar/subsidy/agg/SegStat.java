package com.evcar.subsidy.agg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分段统计
 */
public class SegStat {
    protected List<Segment> _listSegments = new ArrayList<>();

    public SegStat(){

    }

    // fMin最小值 fMax最大值 fStep步长 fAccu累积范围
    public void config(float fMin, float fMax, float fStep, float fAccu){
        // fAccu必须是正值
        if(fAccu < 0.0f) throw new IllegalArgumentException("fAccu不能为负数");
        // 不允许出现步长过大的情况
        if(Math.abs(fStep) > fAccu) throw new IllegalArgumentException("fStep的绝对值不能大于fAccu");
        // fStep如果是正值,那么fMax>fMin;如果fStep是负值,就应当fMax<fMin
        if(fStep * (fMax - fMin) < 0.0f) throw new IllegalArgumentException("fStep符号错误");


        float fIdx = fMin + (fAccu/2.0f);
        float fEnd = fMax - (fAccu/2.0f);
        while(fIdx <= fEnd){
            Segment seg = new Segment(fIdx, fAccu);
            _listSegments.add(seg);
            fIdx += fStep;
        }
    }

    // 这个方法支持多线程并行运行
    public void stat(float fValue){
        for(Segment s : _listSegments){
            s.stat(fValue);
        }
    }

    public List<SegmentResult> getResult(){
        List<SegmentResult> listResult = new ArrayList<>(_listSegments.size());
        for(Segment s : _listSegments){
            listResult.add(new SegmentResult(s.getMidValue(), s.getCount()));
        }
        return listResult;
    }
}


class Segment
{
    // 区段中值
    protected float _fMidValue;
    // 区段范围
    protected float _fAccu;
    // 区段最小值
    protected float _fMin;
    // 区段最大值
    protected float _fMax;
    // 区段计数
    private AtomicInteger _nCount;

    public Segment(float fMidValue, float fAccu)
    {
        this._fMidValue = fMidValue;
        this._fAccu = fAccu;
        this._fMin = fMidValue - (fAccu / 2.0f);
        this._fMax = _fMin + fAccu;
        this._nCount = new AtomicInteger(0);
    }

    // 这个方法支持多线程并行运行
    public void stat(float fValue)
    {
        if(fValue >= _fMin && fValue <= _fMax)
            _nCount.incrementAndGet();
    }

    public float getMidValue(){ return this._fMidValue; }
    public int getCount(){ return this._nCount.get(); }
}

class SegmentResult
{
    public float MidValue;
    public int Count;

    public SegmentResult(float fMidValue, int count){
        MidValue = fMidValue;
        Count = count;
    }
}