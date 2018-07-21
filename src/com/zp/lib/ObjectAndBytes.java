/*
 * ObjectAndBytes
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.lib;

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
public class ObjectAndBytes {
    /**
     * 对象转数组
     *
     * @param obj
     * @return
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
     *
     * @param bytes
     * @return
     */
    public static ProtocolData toObject(byte[] bytes) {
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