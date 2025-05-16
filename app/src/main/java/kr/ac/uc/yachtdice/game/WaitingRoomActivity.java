package kr.ac.uc.yachtdice.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import kr.ac.uc.yachtdice.R;
import kr.ac.uc.yachtdice.model.PlayerInfo;
import kr.ac.uc.yachtdice.network.GameApiService;
import kr.ac.uc.yachtdice.network.RetrofitClient;
import kr.ac.uc.yachtdice.ui.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.*;

public class WaitingRoomActivity extends AppCompatActivity {

    private String roomCode;
    private String playerName;
    private boolean isHost;
    private ListView playerListView;
    private Button btnStart;
    private ArrayAdapter<String> adapter;
    private List<String> playerNames = new ArrayList<>();
    private GameApiService api = RetrofitClient.getApiService();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        roomCode = getIntent().getStringExtra("roomCode");
        playerName = getIntent().getStringExtra("playerName");
        isHost = getIntent().getBooleanExtra("isHost", false);

        playerListView = findViewById(R.id.playerListView);
        btnStart = findViewById(R.id.btnStart);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerNames);
        playerListView.setAdapter(adapter);

        if (!isHost) btnStart.setEnabled(false);

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(WaitingRoomActivity.this, MainActivity.class);
            intent.putStringArrayListExtra("players", new ArrayList<>(playerNames)); // 인원 리스트 전달
            startActivity(intent);
            finish();
        });

        startPollingPlayers();
    }

    private void startPollingPlayers() {
        Runnable pollingTask = new Runnable() {
            @Override
            public void run() {
                api.getRoomPlayers(roomCode).enqueue(new Callback<List<PlayerInfo>>() {
                    @Override
                    public void onResponse(Call<List<PlayerInfo>> call, Response<List<PlayerInfo>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            playerNames.clear();
                            for (PlayerInfo info : response.body()) {
                                playerNames.add(info.getName());
                            }
                            adapter.notifyDataSetChanged();
                        }
                        handler.postDelayed(thisRunnable, 2000); // 🔺 여기를 수정
                    }

                    @Override
                    public void onFailure(Call<List<PlayerInfo>> call, Throwable t) {
                        handler.postDelayed(thisRunnable, 2000); // 🔺 여기를 수정
                    }
                });
            }

            final Runnable thisRunnable = this;
        };

        handler.postDelayed(pollingTask, 2000);
    }
}
