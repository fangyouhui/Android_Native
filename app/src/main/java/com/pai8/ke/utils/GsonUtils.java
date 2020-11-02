package com.pai8.ke.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Gson解析/转换工具类
 * Created by gh on 2018/7/27.
 */

public class GsonUtils {

    private static class GsonUtilsHolder {
        private static final GsonUtils INSTANCE = new GsonUtils();
        private static final Gson GSON = new Gson();
    }

    public static final GsonUtils getInstance() {
        return GsonUtilsHolder.INSTANCE;
    }

    //============================解析Json==============================

    /**
     * 解析Json
     *
     * @param json
     * @param classofT
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T fromJson(String json, Class<T> classofT) throws JsonSyntaxException {
        return GsonUtilsHolder.GSON.fromJson(json, classofT);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classofT) throws JsonSyntaxException {
        return GsonUtilsHolder.GSON.fromJson(json, classofT);
    }

    /**
     * 解析data 格式json
     *
     * @param json
     * @param classofT
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T fromDataJson(String json, Class<T> classofT) throws JsonSyntaxException {
        String data = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            data = jsonObject.get("data").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return GsonUtilsHolder.GSON.fromJson(data, classofT);
    }

    /**
     * 解析data 格式Json转化为ArrayList
     *
     * @param json
     * @param classofT
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> ArrayList<T> fromDataJsonToArrayList(String json, Class<T> classofT) throws
            JsonSyntaxException {
        String data = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            data = jsonObject.get("data").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonToArrayList(data, classofT);
    }

    /**
     * 解析Json数组成List
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
        T[] array = GsonUtilsHolder.GSON.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**
     * 解析Json数组成ArrayList
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = GsonUtilsHolder.GSON.fromJson(json, type);
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(GsonUtilsHolder.GSON.fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    /**
     * Json转List<Map>
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, T>> jsonToListMap(String json) {
        return GsonUtilsHolder.GSON.fromJson(json, new TypeToken<List<Map<String, T>>>() {
        }.getType());
    }

    /**
     * Json转Map
     *
     * @param gsonString
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> jsonToMap(String gsonString) {
        return GsonUtilsHolder.GSON.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
    }

    //=============================转Json==============================

    /**
     * 对象转Json
     *
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {
        return GsonUtilsHolder.GSON.toJson(object);

    }

}
