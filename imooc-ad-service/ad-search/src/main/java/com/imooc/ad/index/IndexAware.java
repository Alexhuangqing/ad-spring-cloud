package com.imooc.ad.index;

/**
 * @Author Alex
 * @Desc
 * <p>
 *    利用map 集合建立起多（一）对多的映射关系
 *    完成对索引的增查改删
 *
 * </p>
 * @Date 2019/3/23 9:55
 */
public interface IndexAware<K,V> {

    void add(K key,V value);

    V get(K key);

    void update(K key,V value);


    void delete(K key,V value);
}
