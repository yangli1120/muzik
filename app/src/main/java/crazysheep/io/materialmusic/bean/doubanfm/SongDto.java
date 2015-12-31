package crazysheep.io.materialmusic.bean.doubanfm;

import java.util.List;

import crazysheep.io.materialmusic.bean.ISong;

/**
 * song dto
 *
 * Created by crazysheep on 15/12/20.
 */
public class SongDto implements ISong {

    /*
    {
      "album": "/subject/2213534/",
      "status": 0,
      "picture": "http://img4.douban.com/lpic/s2658818.jpg",
      "ssid": "de89",
      "artist": "ALI PROJECT",
      "url": "http://mr7.doubanio.com/7dcdb891ef8f88ba71213715485ed312/0/fm/song/p366_128k.mp3",
      "title": "名なしの森",
      "like": "0",
      "subtype": "",
      "length": 256,
      "sid": "366",
      "singers": [
        {
          "related_site_id": 0,
          "is_site_artist": false,
          "id": "2569",
          "name": "ALI PROJECT"
        }
      ],
      "aid": "2213534",
      "file_ext": "mp3",
      "sha256": "842d1e69065907b94b495bb2d69e8a77417d0f137a152e32c8c888c3bd7df273",
      "kbps": "128",
      "albumtitle": "薔薇架刑",
      "alert_msg": ""
    }
     * */
    public String album;
    public int status;
    public String picture;
    public String ssid;
    public String artist;
    public String url;
    public String title;
    public int like;
    public int length;
    public long sid;
    public List<SingerDto> singers;
    public long aid;
    public String file_ext;
    public String sha256;
    public int kbps;
    public String albumtitle;
    public String alert_msg;

    @Override
    public String getName() {
        return title;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    public String getCover() {
        return picture;
    }

    @Override
    public String getArtist() {
        return artist;
    }
}
