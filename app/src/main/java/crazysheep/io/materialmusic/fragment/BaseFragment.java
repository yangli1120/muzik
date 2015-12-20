package crazysheep.io.materialmusic.fragment;

import android.app.Fragment;
import android.os.Bundle;

import crazysheep.io.materialmusic.net.NetClient;
import retrofit.Retrofit;

/**
 * base fragment
 *
 * Created by crazysheep on 15/12/17.
 */
public class BaseFragment extends Fragment {

    public static String TAG = BaseFragment.class.getSimpleName();

    protected Retrofit mRetrofit;

    public BaseFragment() {
        super();

        TAG = getClass().getSimpleName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRetrofit = NetClient.retrofit();
    }

}
