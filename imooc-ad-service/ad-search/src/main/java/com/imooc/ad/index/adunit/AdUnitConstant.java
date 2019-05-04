package com.imooc.ad.index.adunit;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/5/2 23:03
 */
public class AdUnitConstant {

    //二进制位，便于做“位&” 与 “位|” 运算
    public static class POSITION_TYPE {

        public static final int KAIPING = 1;  //app打开
        public static final int TIEPIAN = 2;  //电影开始前的广告
        public static final int TIEPIAN_MIDDLE = 4;  //电影中间的广告
        public static final int TIEPIAN_PAUSE = 8;   //
        public static final int TIEPIAN_POST = 16;
    }
}
