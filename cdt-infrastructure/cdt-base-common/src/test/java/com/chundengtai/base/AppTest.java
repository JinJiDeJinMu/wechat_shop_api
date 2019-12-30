package com.chundengtai.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.chundengtai.base.node.ListNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void ListNode() {
        Gson gson = new Gson();

        LinkedList<String> llist = new LinkedList<String>();
        llist.add("A");//添加元素
        llist.add("B");
        llist.add("C");
        llist.add("D");


        String gsonJson1 = gson.toJson(llist);
        Type jsonType1 = new TypeToken<LinkedList<String>>() {
        }.getType();
        LinkedList<String> llist1 = gson.fromJson(gsonJson1, jsonType1);

        System.out.println("llist------->" + llist1);
        System.out.println("llist------->" + llist);
        llist.add(1, "E");// 将“E”添加到第索引为1的个位置
        System.out.println("添加E到索引为1的个位置后的llist------->" + llist);

        llist.addFirst("X");//  将“X”添加到第一个位置。  失败的话，抛出异常！
        System.out.println("在最前面添加X后的llist------->" + llist);
        System.out.println("删除第一个元素并获得并返回被删除元素----->" + llist.removeFirst());  // 将第一个元素删除。
        System.out.println("删除第一个元素后的llist------->" + llist);

        llist.offerFirst("Y");// 将“Y”添加到第一个位置。  返回true。
        System.out.println("将Y添加到第一个位置后的llist------->" + llist);
        System.out.println("删除第一个元素并获得并返回被删除元素------>" + llist.pollFirst()); // 将第一个元素删除。
        System.out.println("将第一个元素删除后的llist------->" + llist);
        llist.removeLast();
        System.out.println("移除最后一个元素后的llist------->" + llist);

        llist.offerLast("Z"); //   将“Z”添加到最后一个位置
        System.out.println("在最后添加Z后的llist------->" + llist);
        llist.set(2, "M");// 将第3个元素设置M。
        System.out.println("将第3个元素设置M后的llist------->" + llist);
        System.out.println("size:" + llist.size());// LinkedList大小
        llist.clear();// 清空LinkedList

        ListNode<String> myList = new ListNode<String>();
        myList.add("a");
        myList.add("b");
        myList.add("c");
        myList.add("d");
        myList.add("e");
        myList.add("f");
        System.out.println("第三个元素是:" + myList.get(3));


        String json = JSON.toJSONString(myList, SerializerFeature.PrettyFormat,
                SerializerFeature.DisableCircularReferenceDetect,

                SerializerFeature.WriteMapNullValue,
                SerializerFeature.BrowserCompatible,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteEnumUsingToString);


        String gsonJson = gson.toJson(myList);
        ListNode<String> chain = JSON.parseObject(json, new TypeReference<ListNode<String>>() {
        });

        Type jsonType = new TypeToken<ListNode<String>>() {
        }.getType();
        ListNode<String> chain1 = gson.fromJson(gsonJson, jsonType);

        System.out.println("第1个元素是:" + chain1.get(1));
        System.out.println("第2个元素是:" + chain1.get(2));
        System.out.println("第三个元素是:" + chain1.get(3));
        System.out.println("第4个元素是:" + chain1.get(4));
        System.out.println("第5个元素是:" + chain1.get(5));
        System.out.println("第6个元素是:" + chain1.get(6));
        myList.remove(3);

        System.out.println("删除之后，第三个元素是:" + myList.get(3));
        System.out.println("删除之后，第三个元素是:" + chain.get(3));
        System.out.println("-----------替换之后--------");
        myList.replace(1, "b11");
        System.out.println(myList.get(1));
    }
}
