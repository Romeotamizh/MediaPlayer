package com.romeotamizh.MusicPlayer.FavouriteMoment;

public class FavouriteMoments {
    public int[] mFavouriteMomentsList = new int[100];
    public int mFavouriteMomentsCount = 1;
    public boolean isFavouriteMomentsExist = false;


    public FavouriteMoments(int[] mFavouriteMomentsList, int mFavouriteMomentsCount, boolean isFavouriteMomentsExist) {
        this.mFavouriteMomentsList = mFavouriteMomentsList;
        this.mFavouriteMomentsCount = mFavouriteMomentsCount;
        this.isFavouriteMomentsExist = isFavouriteMomentsExist;
    }
}
