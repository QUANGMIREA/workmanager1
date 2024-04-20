package com.example.workmanager1;

import android.content.Context;


import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AddWorker extends Worker {
    public AddWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Đọc kết quả từ công việc trước
        int input = getInputData().getInt("input", 0);
        int result = input + 10;

        // Truyền kết quả cho công việc tiếp theo
        Data outputData = new Data.Builder().putInt("input", result).build();
        return Result.success(outputData);
    }
}