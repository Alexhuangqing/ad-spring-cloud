package com.imooc.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Qinyi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Geo {
    //维度
    private Float latitude;
    //经度
    private Float longitude;

    private String city;
    private String province;
}
