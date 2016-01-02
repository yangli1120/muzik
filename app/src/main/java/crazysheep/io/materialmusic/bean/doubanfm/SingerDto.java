package crazysheep.io.materialmusic.bean.doubanfm;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * singer dto
 *
 * Created by crazysheep on 15/12/20.
 */
@ParcelablePlease
public class SingerDto implements Parcelable {

    /*
    * {
          "related_site_id": 0,
          "is_site_artist": false,
          "id": "2569",
          "name": "ALI PROJECT"
        }
    * */
    public int related_site_id;
    public boolean is_site_artist;
    public long id;
    public String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        SingerDtoParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<SingerDto> CREATOR = new Creator<SingerDto>() {
        public SingerDto createFromParcel(Parcel source) {
            SingerDto target = new SingerDto();
            SingerDtoParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public SingerDto[] newArray(int size) {
            return new SingerDto[size];
        }
    };
}
