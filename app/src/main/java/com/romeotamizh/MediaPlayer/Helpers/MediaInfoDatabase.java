package com.romeotamizh.MediaPlayer.Helpers;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.romeotamizh.MediaPlayer.Helpers.Context.MEDIATYPE;

import java.util.ArrayList;
import java.util.Arrays;

import static com.romeotamizh.MediaPlayer.Helpers.MyApplication.getContext;


public class MediaInfoDatabase {


    private ArrayList<CharSequence> titleList;
    private ArrayList<CharSequence> extensionList;
    private ArrayList<Integer> idList;
    private ArrayList<CharSequence> albumIdList;
    private MEDIATYPE mediaType;
    private ArrayList<CharSequence> durationList;
    private ArrayList<Uri> uriList;

    public ArrayList<CharSequence> getAlbumTitleList() {
        return albumTitleList;
    }

    private ArrayList<CharSequence> albumTitleList;
    private int mediaCount;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public MediaInfoDatabase(MEDIATYPE mediaType) {
        this.mediaType = mediaType;
        addMedia(this.mediaType);
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public TrackInfo getTrackInfo(int id, MEDIATYPE mediaType) {
        return new TrackInfo(id, this.getIdsInAlbum(id), getUriById(id), getTitleById(id), mediaType);
    }


    public ArrayList<CharSequence> getExtensionList() {
        return extensionList;
    }


    public ArrayList<CharSequence> getTitleList() {
        return titleList;
    }

    public CharSequence getTitleById(int id) {
        return titleList.get(idList.indexOf(id));
    }

    public CharSequence getExtensionById(int id) {
        return extensionList.get(idList.indexOf(id));
    }

    public CharSequence getDurationById(int id) {
        return durationList.get(idList.indexOf(id));
    }


    public ArrayList<Integer> getIdList() {
        return idList;
    }

    public ArrayList<Integer> getIdsInAlbum(int id) {
        ArrayList<Integer> temp = new ArrayList<>();
        StringBuilder currentAlbumId = new StringBuilder(albumIdList.get(idList.indexOf(id)));
        String currentAlbum = currentAlbumId.substring(0, currentAlbumId.indexOf("."));
        for (CharSequence tempAlbumId : albumIdList) {
            StringBuilder tempAlbum = new StringBuilder(tempAlbumId);
            if (tempAlbumId.toString().contains(currentAlbum + ".")) {
                String x = tempAlbum.substring(tempAlbum.lastIndexOf(".") + 1);
                while (x.contains("."))
                    x = x.substring(tempAlbum.lastIndexOf(".") + 1);
                temp.add(Integer.valueOf(x));
            }
        }

        getIdsInAlbum(currentAlbum);


        return temp;
    }

    public ArrayList<Integer> getIdsInAlbum(CharSequence currentAlbum) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (CharSequence tempAlbumId : albumIdList) {
            StringBuilder tempAlbum = new StringBuilder(tempAlbumId);
            if (tempAlbum.toString().contains(currentAlbum + ".")) {
                String x = tempAlbum.substring(tempAlbum.lastIndexOf(".") + 1);
                while (x.contains("."))
                    x = x.substring(tempAlbum.lastIndexOf(".") + 1);
                temp.add(Integer.valueOf(x));
            }
        }

        return temp;
    }

    public MEDIATYPE getMediaType() {
        return mediaType;
    }

    public ArrayList<CharSequence> getDurationList() {
        return durationList;
    }

    public ArrayList<Uri> getUriList() {
        return uriList;
    }

    public Uri getUriById(int id) {
        return uriList.get(idList.indexOf(id));

    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void addMedia(MEDIATYPE mediaType) {

        this.titleList = new ArrayList<>();
        this.durationList = new ArrayList<>();
        this.idList = new ArrayList<>();
        this.extensionList = new ArrayList<>();
        this.uriList = new ArrayList<>();
        this.albumIdList = new ArrayList<>();
        this.albumTitleList = new ArrayList<>();
        Cursor cursor;
        Uri contentUri;
        String sortOrder;


        if (mediaType == MEDIATYPE.AUDIO) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            sortOrder = "upper(" + MediaStore.Audio.Media.TITLE + ")ASC";
        } else {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            sortOrder = "upper(" + MediaStore.Video.Media.TITLE + ")ASC";

        }

        cursor = getContext().getContentResolver().query(contentUri, null, null, null, sortOrder);
        CharSequence title;
        int id;
        Uri uri;
        String currAlbum = "";
        ArrayList<CharSequence> albumTitleList1 = new ArrayList();



        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("_id"));
                uri = ContentUris.withAppendedId(contentUri, id);
                title = (cursor.getString(cursor.getColumnIndex("title")));
                this.titleList.add(title);
                this.durationList.add(FormatData.formatTime(cursor.getInt(cursor.getColumnIndex("duration"))));
                this.idList.add(id);
                this.extensionList.add(FormatData.getExtension(cursor.getString(cursor.getColumnIndex("_display_name"))));
                this.uriList.add(uri);
                currAlbum = cursor.getString(cursor.getColumnIndex("album"));
                if (!albumTitleList1.contains(currAlbum))
                    albumTitleList1.add(currAlbum);
                this.albumIdList.add(currAlbum + "." + id);
                this.mediaCount++;

            }

            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("Column names", cursor.getColumnName(i));
            }

            cursor.close();
            CharSequence[] c = albumTitleList1.toArray(new CharSequence[0]);
            Arrays.sort(c);
            this.albumTitleList.addAll(Arrays.asList(c));
        }
    }


    public class TrackInfo {
        private int id;
        private ArrayList<Integer> idsInAlbum;
        private Uri uri;
        private MEDIATYPE mediaType;
        private CharSequence title;

        TrackInfo(int id, ArrayList<Integer> idsInAlbum, Uri uri, CharSequence title, MEDIATYPE mediaType) {

            this.id = id;
            this.idsInAlbum = idsInAlbum;
            this.title = title;
            this.uri = uri;
            this.mediaType = mediaType;


        }

        public CharSequence getTitle() {
            return title;
        }

        public int getId() {
            return id;
        }

        public ArrayList<Integer> getIdsInAlbum() {

            return idsInAlbum;
        }


        public Uri getUri() {
            return uri;
        }

        public MEDIATYPE getMediaType() {
            return mediaType;
        }


    }


}