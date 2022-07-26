package com.example.inote.view;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import com.example.inote.models.CheckItem;
import com.example.inote.models.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Converters {

    @TypeConverter
    public static String MyListItemListToString(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<String> stringToMyListItemList(@Nullable String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {
        }.getType();

        Gson gson = new Gson();
        return gson.fromJson(data, listType);
    }


    @TypeConverter
    public static String MyListItemListToObject(List<CheckItem> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<CheckItem> objectToMyListItemList(@Nullable String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<CheckItem>>() {
        }.getType();

        Gson gson = new Gson();
        return gson.fromJson(data, listType);
    }
}