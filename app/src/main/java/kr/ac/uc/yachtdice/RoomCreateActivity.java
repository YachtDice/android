package kr.ac.uc.yachtdice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RoomCreateActivity extends AppCompatActivity {

    EditText roomCodeInput;
    Button btnCreateRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_create);

        roomCodeInput = findViewById(R.id.roomCodeInput);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);

        btnCreateRoom.setOnClickListener(v -> {
            String code = roomCodeInput.getText().toString().trim();
            if (code.isEmpty()) {
                Toast.makeText(this, "방 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: 서버에 방 생성 요청 (지금은 생략)
//            Intent intent = new Intent(this, WaitingRoomActivity.class);
//            intent.putExtra("roomCode", code);
//            intent.putExtra("playerName", "호스트");
//            startActivity(intent);
//            finish();
        });
    }
}