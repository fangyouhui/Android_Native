/**
 * Copyright 2014 Zhenguo Jin
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pai8.ke.utils;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.pai8.ke.app.MyApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * 资源操作工具类
 * Created by gh on 2018/7/27.
 */
public final class ResUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private ResUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 获取color资源
     *
     * @param resourceId
     * @return
     */
    public static int getColor(int resourceId) {
        return ContextCompat.getColor(MyApp.getMyApp(), resourceId);
    }

    /**
     * 获取text资源
     *
     * @param resourceId
     * @return
     */
    public static String getText(int resourceId) {
        return MyApp.getMyApp().getResources().getText(resourceId).toString();
    }

    /**
     * 获取drawable资源
     *
     * @param resourceId
     * @return
     */
    public static Drawable getDrawable(int resourceId) {
        return ContextCompat.getDrawable(MyApp.getMyApp(), resourceId);
    }

    /**
     * 从Assets获取File资源
     *
     * @param fileName
     * @return
     */
    public static String geFileFromAssets(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }

        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in =
                    new InputStreamReader(MyApp.getMyApp().getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从Raw获取File资源
     *
     * @param resId
     * @return
     */
    public static String geFileFromRaw(int resId) {
        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in =
                    new InputStreamReader(MyApp.getMyApp().getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从Assets获取File资源列表
     *
     * @param fileName
     * @return
     */
    public static List<String> geFileToListFromAssets(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }

        List<String> fileContent = new ArrayList<String>();
        try {
            InputStreamReader in =
                    new InputStreamReader(MyApp.getMyApp().getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.add(line);
            }
            br.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从Raw获取File资源列表
     *
     * @param resId
     * @return
     */
    public static List<String> geFileToListFromRaw(int resId) {
        List<String> fileContent = new ArrayList<String>();
        BufferedReader reader;
        try {
            InputStreamReader in = new InputStreamReader(MyApp.getMyApp().getResources().openRawResource(resId));
            reader = new BufferedReader(in);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
