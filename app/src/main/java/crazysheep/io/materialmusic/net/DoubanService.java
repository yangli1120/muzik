package crazysheep.io.materialmusic.net;

import crazysheep.io.materialmusic.bean.doubanfm.AlbumDto;
import crazysheep.io.materialmusic.bean.doubanfm.PlaylistDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * douban music api
 *
 * Created by crazysheep on 15/12/20.
 */
public interface DoubanService {

    public static final long DEFAULT_CHANNEL_ID = 2;

    @GET("http://douban.fm/j/mine/playlist")
    Call<PlaylistDto> fetchPlaylist(@Query("channel") long channelId);

    @GET("https://api.douban.com/v2/music/{albumId}")
    Call<AlbumDto> getAlbumInfo(@Path("albumId") long albumId);
}
