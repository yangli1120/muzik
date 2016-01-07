package crazysheep.io.materialmusic.bean;

import com.activeandroid.Model;

import java.lang.reflect.Field;

/**
 * base model extends from {@link Model}, for parcelable column '_id',
 * see{@linkplain https://github.com/pardom/ActiveAndroid/issues/279}
 *
 * Created by crazysheep on 16/1/6.
 */
public class BaseModel extends Model {

    public void setAaId(Long id) {
        try {
            Field idField = Model.class.getDeclaredField("mId");
            idField.setAccessible(true);
            idField.set(this, id);
        } catch (Exception e) {
            throw new RuntimeException("Reflection failed to get the Active Android ID", e);
        }
    }
}
