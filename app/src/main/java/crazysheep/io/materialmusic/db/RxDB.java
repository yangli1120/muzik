package crazysheep.io.materialmusic.db;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import java.util.List;

import crazysheep.io.materialmusic.bean.localmusic.LocalAlbumDto;
import crazysheep.io.materialmusic.bean.localmusic.LocalArtistDto;
import crazysheep.io.materialmusic.bean.localmusic.LocalSongDto;
import crazysheep.io.materialmusic.utils.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * use rx java do database operations at background
 *
 * Created by crazysheep on 15/12/29.
 */
public class RxDB {

    public interface OnQueryListener<T> {
        void onResult(List<T> results);
        void onError(String err);
    }

    /**
     * query all songs from system media store
     * */
    public static Subscription getAllSongs(@NonNull final ContentResolver resolver,
                                           @NonNull final OnQueryListener<LocalSongDto> listener) {
        return Observable
                .just(true)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Boolean, List<LocalSongDto>>() {
                    @Override
                    public List<LocalSongDto> call(Boolean aBoolean) {
                        return MediaStoreHelper.getAllSongs(resolver);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalSongDto>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(Utils.checkNull(e)
                                ? "unknow exception..." : e.getMessage());
                    }

                    @Override
                    public void onNext(List<LocalSongDto> localSongDtos) {
                        listener.onResult(localSongDtos);
                    }
                });
    }

    /**
     * query all artists from system media store
     * */
    public static Subscription getAllArtists(@NonNull final ContentResolver resolver,
                                             @NonNull final OnQueryListener<LocalArtistDto> listener) {
        return Observable.just(true)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Boolean, List<LocalArtistDto>>() {
                    @Override
                    public List<LocalArtistDto> call(Boolean aBoolean) {
                        return MediaStoreHelper.getAllArtist(resolver);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalArtistDto>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(Utils.checkNull(e)
                                ? "unknow exception..." : e.getMessage());
                    }

                    @Override
                    public void onNext(List<LocalArtistDto> localArtistDtos) {
                        listener.onResult(localArtistDtos);
                    }
                });
    }

    /**
     * query all albums from system media store
     * */
    public static Subscription getAllAlbums(@NonNull final ContentResolver resolver,
                                            @NonNull final OnQueryListener<LocalAlbumDto> listener) {
        return Observable.just(true)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Boolean, List<LocalAlbumDto>>() {
                    @Override
                    public List<LocalAlbumDto> call(Boolean aBoolean) {
                        return MediaStoreHelper.getAllAlbums(resolver);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalAlbumDto>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(Utils.checkNull(e)
                                ? "unknow exception..." : e.getMessage());
                    }

                    @Override
                    public void onNext(List<LocalAlbumDto> localAlbumDtos) {
                        listener.onResult(localAlbumDtos);
                    }
                });
    }

}
