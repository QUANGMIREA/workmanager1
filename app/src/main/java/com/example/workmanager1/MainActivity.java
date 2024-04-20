package com.example.workmanager1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText inputNumber;
    Button calculateButton;
    TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputNumber = findViewById(R.id.inputNumber);
        calculateButton = findViewById(R.id.calculateButton);
        resultView = findViewById(R.id.resultView);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int input = Integer.parseInt(inputNumber.getText().toString());
                startWorkChain(input);
            }
        });
    }
    private void startWorkChain(int input) {
        // Định nghĩa Input Data
        Data inputData = new Data.Builder().putInt("input", input).build();

        // Tạo các WorkRequest với Data
        OneTimeWorkRequest multiplyWork = new OneTimeWorkRequest.Builder(MultiplyWorker.class)
                .setInputData(inputData)
                .build();

        OneTimeWorkRequest addWork = new OneTimeWorkRequest.Builder(AddWorker.class).build();
        OneTimeWorkRequest subtractWork = new OneTimeWorkRequest.Builder(SubtractWorker.class).build();

        // Lên lịch thực hiện tuần tự
        WorkManager.getInstance(this)
                .beginWith(multiplyWork)
                .then(addWork)
                .then(subtractWork)
                .enqueue();

        // Theo dõi kết quả
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(subtractWork.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            int result = workInfo.getOutputData().getInt("result", 0);
                            resultView.setText(String.valueOf(result));
                        }
                    }
                });

    }





}


