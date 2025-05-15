package kr.ac.uc.yachtdice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    Button btnStart, btnRecord, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnStart = findViewById(R.id.btnStart);
        btnRecord = findViewById(R.id.btnRecord);
        btnExit = findViewById(R.id.btnExit);

        btnStart.setOnClickListener(v ->
                startActivity(new Intent(StartActivity.this, com.example.yachtdice.RoomSelectionActivity.class)));

        btnRecord.setOnClickListener(v ->
                Toast.makeText(this, "기록보기는 추후 지원 예정입니다.", Toast.LENGTH_SHORT).show());

        btnExit.setOnClickListener(v -> finish());
    }
}
