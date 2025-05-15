package kr.ac.uc.yachtdice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RoomSelectionActivity extends AppCompatActivity {

    Button btnJoin, btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_selection);

        btnJoin = findViewById(R.id.btnJoin);
        btnCreate = findViewById(R.id.btnCreate);

        btnJoin.setOnClickListener(v ->
                startActivity(new Intent(RoomSelectionActivity.this, RoomListActivity.class)));

        btnCreate.setOnClickListener(v ->
                startActivity(new Intent(RoomSelectionActivity.this, RoomCreateActivity.class)));
    }
}
