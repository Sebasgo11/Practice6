package com.sebastiangomez.practice6;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Bares extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bares);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bares, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.demografia) {
            Intent A1 = new Intent(this,Demografia.class);
            startActivity(A1);
            return true;
        }

        if (id == R.id.hoteles) {
            Intent A2 = new Intent(this,Hoteles.class);
            startActivity(A2);
            return true;
        }

        if (id == R.id.sitios) {
            Intent A3 = new Intent(this,Sitios_Turisticos.class);
            startActivity(A3);
            return true;
        }

        if (id == R.id.about) {
            Intent A4 = new Intent(this,Acerca_De.class);
            startActivity(A4);
            return true;
        }

        if (id == R.id.publicidad) {
            Intent A5 = new Intent(this,MainActivity.class);
            startActivity(A5);
            return true;
        }

        if (id == R.id.ubicacion) {
            Intent A6 = new Intent(this,MapsActivity.class);
            startActivity(A6);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
