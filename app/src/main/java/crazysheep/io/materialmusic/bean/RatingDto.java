package crazysheep.io.materialmusic.bean;

/**
 * rating dto
 *
 * Created by crazysheep on 15/12/21.
 */
public class RatingDto extends BaseDto {

    /*
    *     {
            "max": 10,
            "average": "9.0",
            "numRaters": 322,
            "min": 0
          }
    * */
    public int max;
    public String average;
    public int numRaters;
    public int min;

}
