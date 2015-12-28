package crazysheep.io.materialmusic.bean.doubanfm;

import java.util.List;

/**
 * playlist dto
 *
 * Created by crazysheep on 15/12/20.
 */
public class PlaylistDto extends BaseDto {

    /*
     * {
     "r": 0,
     "is_show_quick_start": 0,
     "song": [
     {
     "album": "/subject/1465647/",
     "status": 0,
     "picture": "http://img3.doubanio.com/lpic/s3918589.jpg",
     "ssid": "4f08",
     "artist": "浜崎あゆみ",
     "url": "http://mr7.doubanio.com/7a76404cce9aa15e302f2bb3bc95db97/0/fm/song/p913_128k.mp3",
     "title": "End Roll (Neuro Mantic Mix)",
     "like": "0",
     "subtype": "",
     "length": 308,
     "sid": "913",
     "singers": [
     {
     "related_site_id": 0,
     "is_site_artist": false,
     "id": "4949",
     "name": "浜崎あゆみ"
     }
     ],
     "aid": "1465647",
     "file_ext": "mp3",
     "sha256": "58c586c88dc498539d6e77121f4ed91a299a219a42f3fc40fddb4117f9838a53",
     "kbps": "128",
     "albumtitle": "A",
     "alert_msg": ""
     }
     ]
     }
     * */
    public int r;
    public int is_show_quick_start;
    public List<SongDto> song;

}
