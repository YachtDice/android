package kr.ac.uc.yachtdice.network;

import kr.ac.uc.yachtdice.game.Player;
import kr.ac.uc.yachtdice.model.PlayerInfo;
import kr.ac.uc.yachtdice.model.Room;
import kr.ac.uc.yachtdice.model.RoomStatus;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface GameApiService {
    @GET("/rooms")
    Call<List<String>> getRoomList();

    @POST("/rooms")
    Call<Void> createRoom(@Body Room room);

    @POST("/rooms/{code}/join")
    Call<Void> joinRoom(@Path("code") String code, @Body Player player);

    @POST("/rooms/{code}/ready")
    Call<Void> setReady(@Path("code") String code, @Query("name") String name);

    @GET("/rooms/{code}/players")
    Call<List<PlayerInfo>> getPlayers(@Path("code") String code);

    @POST("/rooms/{code}/start")
    Call<Void> startGame(@Path("code") String code);

    @DELETE("/rooms/{code}")
    Call<Void> deleteRoom(@Path("code") String code);

    @GET("/rooms/{code}/exists")
    Call<Boolean> roomExists(@Path("code") String code);

    @GET("/rooms")
    Call<List<RoomStatus>> getRoomStatusList();

    @GET("/api/room/players")
    Call<List<PlayerInfo>> getRoomPlayers(@Query("roomCode") String roomCode);


}
