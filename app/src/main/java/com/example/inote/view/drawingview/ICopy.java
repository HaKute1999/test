package com.example.inote.view.drawingview;

import java.util.List;

public interface ICopy {
    void onFinish(List<String> data);

    void onProgress(String path);
}
