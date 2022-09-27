package com.aosp_repo.utils.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class JsonTranslator {

    public static Map<String, Object> translateJsonToMap(String json) {
        return new Gson().fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
    }

    public static List<Map<String, Object>> translateJsonToList(String json) {
        return new Gson().fromJson(json, new TypeToken<List<Map<String, Object>>>(){}.getType());
    }

}
