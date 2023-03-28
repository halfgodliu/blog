package com.lyjava.utils;

import com.lyjava.domain.entity.Article;
import com.lyjava.domain.vos.HotArticle;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    public BeanCopyUtils() {
    }

    /**
     * 拷贝（转换）bean的方法
     * @param source 被拷贝的对象
     * @param clazz 要拷贝成的class对象
     * @param <V> 拷贝成的类型，最后返回的数据也是这个类型
     * @return
     */
    public static <V> V copyBean(Object source,Class<V> clazz) {
        V result = null;
        try {
            //获取最终要拷贝成（转换成）的类型的的实例化对象
            result = clazz.newInstance();
            //完成拷贝
            BeanUtils.copyProperties(source, result);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <O,V> List<V> copyBeanList(List<O> list,Class<V> clazz){
        //使用Stream流来完成拷贝
        List<V> collect = list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());

        return collect;
    }

    /**
     * 只是用来测试拷贝方法的
     * @param args
     */
    public static void main(String[] args) {
        Article article = new Article();
        article.setId(10L);
        article.setTitle("6666");
        article.setContent("哈哈哈哈");
        HotArticle hotArticle = BeanCopyUtils.copyBean(article, HotArticle.class);
        System.out.println(hotArticle);
    }
}
