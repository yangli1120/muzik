package crazysheep.io.materialmusic.bean;

/**
 * channel dto
 *
 * Created by crazysheep on 15/12/18.
 */
public class ChannelDto extends BaseDto {

    /*
    * {
      "name_en": "Personal Radio",
      "seq_id": 0,
      "abbr_en": "My",
      "name": "私人兆赫",
      "channel_id": 0
    }
    * */
    public String name_en;
    public long seq_id;
    public String abbr_en;
    public String name;
    public long channel_id;
}
