package kr.ac.uc.yachtdice.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

import kr.ac.uc.yachtdice.R;
import kr.ac.uc.yachtdice.game.WaitingRoomActivity;
import kr.ac.uc.yachtdice.model.RoomStatus;
import kr.ac.uc.yachtdice.network.GameApiService;
import kr.ac.uc.yachtdice.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomListActivity extends AppCompatActivity {

    private ListView roomListView;
    private ArrayAdapter<String> adapter;
    private List<String> roomDisplayList = new ArrayList<>();
    private List<RoomStatus> roomStatusList = new ArrayList<>();
    private GameApiService api = RetrofitClient.getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        roomListView = findViewById(R.id.roomListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, roomDisplayList);
        roomListView.setAdapter(adapter);

        // 서버에서 방 목록 가져오기
        loadRoomList();

        roomListView.setOnItemClickListener((parent, view, position, id) -> {
            RoomStatus selectedRoom = roomStatusList.get(position);
            if (selectedRoom.getCount() >= 4) {
                Toast.makeText(this, "방이 가득 찼습니다!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, WaitingRoomActivity.class);
            intent.putExtra("roomCode", selectedRoom.getCode());
            intent.putExtra("playerName", "게스트");
            startActivity(intent);
            finish();
        });
    }

    private void loadRoomList() {
        api.getRoomStatusList().enqueue(new Callback<List<RoomStatus>>() {
            @Override
            public void onResponse(Call<List<RoomStatus>> call, Response<List<RoomStatus>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomStatusList.clear();
                    roomDisplayList.clear();

                    for (RoomStatus room : response.body()) {
                        roomStatusList.add(room);
                        String display = room.getCode() + " (" + room.getCount() + "/4)";
                        roomDisplayList.add(display);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<RoomStatus>> call, Throwable t) {
                Toast.makeText(RoomListActivity.this, "방 목록을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}