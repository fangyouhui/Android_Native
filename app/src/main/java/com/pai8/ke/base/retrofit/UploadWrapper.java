package com.pai8.ke.base.retrofit;


import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UploadWrapper {

    public static RequestBody uploadFileRespBody(String path) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return requestFile;
    }

}
