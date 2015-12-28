package crazysheep.io.materialmusic.bean.doubanfm;

/**
 * result response from login api
 *
 * Created by crazysheep on 15/12/18.
 */
public class LoginDto extends BaseDto {

    /**
     * {
     "user_id": "<user_id>",
     "err": "ok",
     "token": "<token_string>",
     "expire": "<expire_time_in_millisecond>",
     "r": 0,
     "user_name": "钟小腾",
     "email": "<user_account>"
     }
     * */
    public long user_id;
    public String err;
    public String token;
    public String expire;
    public int r;
    public String user_name;
    public String email;

}

