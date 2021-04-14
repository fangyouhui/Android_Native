package com.lhs.library.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lihongshi
 * 文件保存到了data/data/包名/files
 */

public class SerializableUtil {
    /**
     * Saves a serializable object.
     *
     * @param context      The application context.
     * @param objectToSave The object to save.
     * @param fileName     The name of the file.
     * @param <T>          The type of the object.
     */

    public static <T extends Serializable> boolean saveSerializable(Context context, T objectToSave, String fileName) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(objectToSave);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Loads a serializable object.
     *
     * @param context  The application context.
     * @param fileName The filename.
     * @param <T>      The object type.
     * @return the serializable object.
     */

    public static <T extends Serializable> T readSerializable(Context context, String fileName) {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        T objectToReturn = null;
        try {
            fileInputStream = context.openFileInput(fileName);
            objectInputStream = new ObjectInputStream(fileInputStream);
            objectToReturn = (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //  e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

        return objectToReturn;
    }

    /**
     * Removes a specified file.
     *
     * @param context  The application context.
     * @param filename The name of the file.
     */

    public static boolean removeSerializable(Context context, String filename) {
        return context.deleteFile(filename);
    }


    /**
     * 序列化,List
     */
    public static <T> boolean saveSerializable(Context context, List<T> list, String name) {
        T[] array = (T[]) list.toArray();
        return saveSerializable(context, array, name);
    }

    /**
     * 反序列化,List
     */
    public static <E> List<E> readObjectForList(Context context, String name) {
        E[] object = readSerializable(context, name);
        if (object == null) {
            return new LinkedList<>();
        }
        List list = Arrays.asList(object);//Arrays.asList()转化过来的List的不支持add()和remove()方法
        return new ArrayList<>(list);
    }

}
