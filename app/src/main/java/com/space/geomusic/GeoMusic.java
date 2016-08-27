package com.space.geomusic;

import android.app.Activity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by pc-hp on 22-08-2016.
 */
public class GeoMusic extends Activity {
    private MediaPlayer musicPlayer = new MediaPlayer();
    private String songs[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        ListView songsListView = (ListView) findViewById(R.id.songs);
        songs = getSongs();
        ArrayAdapter<String> songListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,songs);
        songsListView.setAdapter(songListAdapter);

        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String song = songs[position];
                Toast.makeText(GeoMusic.this,song,Toast.LENGTH_SHORT).show();
                playSong(song);
            }
        });
    }

    public String[] getSongs() {
        Cursor cursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media.DISPLAY_NAME}, null, null,"LOWER("+MediaStore.Audio.Media.DISPLAY_NAME+")ASC");
        String songs[] = new String[cursor.getCount()];
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                songs[i] = cursor.getString(0);
                i++;
            } while (cursor.moveToNext());
        }
        return songs;
    }

    private void playSong(String songName){
        String songPath = Environment.getExternalStorageDirectory()+ File.separator + songName;
        try {
            musicPlayer.reset();
            musicPlayer.setDataSource(songPath);
            musicPlayer.prepare();
            musicPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
