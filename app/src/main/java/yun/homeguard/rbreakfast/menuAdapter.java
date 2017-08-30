package yun.homeguard.rbreakfast;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 123 on 2017/7/21.
 */

public class menuAdapter extends ArrayAdapter {

    Context context;
    public menuAdapter(Context context,ArrayList<menuItem> items){
        super(context,0,items);
        this.context=context;
    }

    public View getView(int position, View convertview, ViewGroup parent){

        LayoutInflater inflater=LayoutInflater.from(context);
        LinearLayout itemlayout=null;

        if(convertview==null){
            itemlayout=(LinearLayout)inflater.inflate(R.layout.listitem,null);
        }
        else{
            itemlayout=(LinearLayout)convertview;
        }

        menuItem item=(menuItem)getItem(position);
        TextView menuName=(TextView)itemlayout.findViewById(R.id.menu_text);
        menuName.setText(item.menu);
        TextView shopName=(TextView)itemlayout.findViewById(R.id.shop_text);
        shopName.setText(item.shop);

        return itemlayout;
    }

}
