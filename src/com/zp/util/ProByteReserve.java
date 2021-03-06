/*
 * ProByteReserve
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.util;

import com.zp.protocol.ProtocolData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 工具类
 * 实现ProtocolData 与　byte[] 转化
 */
public class ProByteReserve {
    /**
     * 对象转数组
     *使用对象输出流转化为流进而转化为byte数组
     * @param obj 待转化对象
     * @return byte[]
     */
    public static byte[] toByteArray(ProtocolData obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     *逆过程
     * @param bytes byte数组
     * @return ProtocolData
     */
    public static ProtocolData toProData(byte[] bytes) {
        ProtocolData obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj =(ProtocolData) ois.readObject();
            ois.close();
            bis.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return obj;
    }
}