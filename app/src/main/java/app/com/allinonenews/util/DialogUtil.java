package app.com.allinonenews.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import java.util.List;

import app.com.allinonenews.R;

/**
 * Created by mukesh on 6/4/17.
 */

public class DialogUtil {

    public interface CountryListSelectCallback{
        void onSelected(String selectedCategory);
        void cancel();
    }

    public static void showCategoryList(Context context, final List<String > list, String selected, final CountryListSelectCallback callback){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.select_category));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.filter_list_item,R.id.text1);
        arrayAdapter.addAll(list);
        int pos=list.indexOf(selected.toLowerCase());
        builder.setSingleChoiceItems(arrayAdapter,pos,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callback.onSelected(arrayAdapter.getItem(which));
            }
        });
        builder.show();

    }
}
