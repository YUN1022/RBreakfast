package yun.homeguard.rbreakfast;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button menu,rand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu=(Button)findViewById(R.id.menu_btn);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,BreakfastMenu.class);
                 startActivity(intent);
            }
        });

        BreakfastMenu.DBOpenHelper openhelper =new BreakfastMenu.DBOpenHelper(this);
        final SQLiteDatabase db=openhelper.getWritableDatabase();

        rand=(Button)findViewById(R.id.Rand_btn);
        rand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Cursor c=db.rawQuery("select * from "+BreakfastMenu.DATABASE_TABLE,null);
                    int count=(int)(Math.random()*c.getCount());
                    c.moveToPosition(count);
                    String name=c.getString(c.getColumnIndex("name")),shop=c.getString(c.getColumnIndex("shop"));
                    AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);


                    ad.setTitle("抽中了...");
                    ad.setMessage(""+shop+"的"+name);
                    ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    ad.show();
                }catch (Exception e){
                    AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
                    ad.setMessage("你清單沒任何東西喔");
                    ad.show();
                }
      //         Cursor c=db.rawQuery("select * from "+BreakfastMenu.DATABASE_TABLE,null);

                //Toast.makeText(MainActivity.this,name+" "+shop,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
