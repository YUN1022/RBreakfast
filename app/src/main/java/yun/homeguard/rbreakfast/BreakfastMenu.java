package yun.homeguard.rbreakfast;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BreakfastMenu extends AppCompatActivity {

    public static String DATABASE_TABLE="menutable";
    public static String DATABASE_CREATTABLE="create table "+DATABASE_TABLE+"(name,shop);";
    private menuAdapter adapter=null;
    public DBOpenHelper openHelper;
    public SQLiteDatabase db;
    ArrayList<menuItem> menulist;
    TextView empty_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_menu);
        openHelper=new DBOpenHelper(this);
        db=openHelper.getWritableDatabase();

        ListView lv=(ListView)findViewById(R.id.menu_lv);
        menulist=new ArrayList<menuItem>();
        adapter=new menuAdapter(this,menulist);
        lv.setAdapter(adapter);

        final Cursor c=db.rawQuery("select * from "+DATABASE_TABLE,null);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            String name=c.getString(c.getColumnIndex("name"));
            String shop=c.getString(c.getColumnIndex("shop"));
            menulist.add(new menuItem(name,shop));
            c.moveToNext();
        }
        c.moveToFirst();


        Button addbtn=(Button)findViewById(R.id.add_btn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View item= LayoutInflater.from(BreakfastMenu.this).inflate(R.layout.dialog_addmenu,null);
                AlertDialog.Builder ad= new AlertDialog.Builder(BreakfastMenu.this);
                ad.setTitle("新增");
                ad.setView(item);
                ad.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editTextname=(EditText)item.findViewById(R.id.input_text);
                        EditText editTextshop=(EditText)item.findViewById(R.id.input2_text);
                        String menuName=editTextname.getText().toString() , shopName=editTextshop.getText().toString();

                       if(menuName.length()==0){
                            Toast.makeText(BreakfastMenu.this,"輸入點東西吧",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            menulist.add(new menuItem(menuName,shopName));

                           ContentValues contentValues=new ContentValues();
                           contentValues.put("name",menuName);
                           contentValues.put("shop",shopName);
                           db.insert(DATABASE_TABLE,null,contentValues);
                           adapter.notifyDataSetChanged();
                        }
                    }
                });
                ad.show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder clickAD=new AlertDialog.Builder(BreakfastMenu.this);
                clickAD.setMessage("要刪除嗎?");
                clickAD.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c.moveToPosition(position);
                        db.delete(DATABASE_TABLE,"name="+"'" +c.getString(c.getColumnIndex("name"))+"'",null);
                        menulist.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                clickAD.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                clickAD.show();
                return false;
            }
        });

        empty_txt=(TextView)findViewById(R.id.txt_empty);
        empty_txt.setText("暫無資料");
        lv.setEmptyView(empty_txt);

    }
    static class DBOpenHelper extends SQLiteOpenHelper{

        public DBOpenHelper(Context context){
            super(context,"menu.db",null,1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATTABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
