package kr.ac.uc.yachtdice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class RoomListActivity extends AppCompatActivity {

    ListView roomListView;
    ArrayAdapter<String> adapter;
    List<String> roomList = new ArrayList<>(); // 예시용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        roomListView = findViewById(R.id.roomListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, roomList);
        roomListView.setAdapter(adapter);

        // TODO: 서버에서 방 목록 받아오기
        roomList.addAll(Arrays.asList("1234", "5678", "9012")); // 예시
        adapter.notifyDataSetChanged();

        roomListView.setOnItemClickListener((parent, view, position, id) -> {
            String roomCode = roomList.get(position);
//            Intent intent = new Intent(this, WaitingRoomActivity.class);
//            intent.putExtra("roomCode", roomCode);
//            intent.putExtra("playerName", "게스트"); // 나중에 입력 받도록 수정 가능
//            startActivity(intent);
//            finish();
        });
    }
}
