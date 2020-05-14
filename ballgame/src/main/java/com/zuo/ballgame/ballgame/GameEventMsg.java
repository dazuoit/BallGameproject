package com.zuo.ballgame.ballgame;



public class GameEventMsg {
    public String mTag;//消息tag
    public Object mData;//消息内容

    public GameEventMsg(String tag, Object object) {
        mTag = tag;
        mData = object;
    }

    public String getTag() {
        return mTag;
    }

    public Object getData() {
        return mData;
    }
}
