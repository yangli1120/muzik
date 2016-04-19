package crazysheep.io.materialmusic.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import crazysheep.io.materialmusic.net.NetClient;
import retrofit2.Retrofit;

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

    protected ActionBar getSupportActionBar() {
        if(getActivity() instanceof AppCompatActivity)
            return ((AppCompatActivity) getActivity()).getSupportActionBar();

        return null;
    }

}
