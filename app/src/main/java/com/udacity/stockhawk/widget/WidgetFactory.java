package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Binder;
import android.support.v4.content.ContextCompat;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.MyNumberFormat;

import static com.udacity.stockhawk.data.Contract.Quote.POSITION_ID;
import static com.udacity.stockhawk.data.Contract.Quote.POSITION_NAME;
import static com.udacity.stockhawk.data.Contract.Quote.POSITION_PRICE;
import static com.udacity.stockhawk.data.Contract.Quote.POSITION_SYMBOL;

/**
 * Created by andika on 03/12/17.
 */

public class WidgetFactory extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) data.close();

                final long identityToken = Binder.clearCallingIdentity();

                data = getContentResolver().query(
                        Contract.Quote.URI,
                        Contract.Quote.QUOTE_COLUMNS,
                        null,
                        null,
                        null
                );

                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(position))
                    return null;

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.stock_widget_list_item);
                String symbol = data.getString(POSITION_SYMBOL);
                String name = data.getString(POSITION_NAME);
                String price = MyNumberFormat.dollarFormat().format(data.getFloat(POSITION_PRICE));
                float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
                float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
                String percentage = MyNumberFormat.percentageFormat().format(percentageChange/100);
                views.setTextViewText(R.id.symbol, symbol);
                views.setTextColor(R.id.symbol, Color.parseColor("#cccccc"));
                views.setTextViewText(R.id.price, price);
                views.setTextColor(R.id.price, Color.parseColor("#cccccc"));
                views.setTextViewText(R.id.change, percentage);

                if (rawAbsoluteChange > 0) {
                    views.setInt(
                            R.id.change,
                            "setBackgroundColor",
                            ContextCompat.getColor(getBaseContext(), R.color.material_green_700)
                    );
                } else {
                    views.setInt(
                            R.id.change,
                            "setBackgroundColor",
                            ContextCompat.getColor(getBaseContext(), R.color.material_red_700)
                    );
                }

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(Intent.EXTRA_TEXT, new String[] {symbol, name});
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getBaseContext().getPackageName(), R.layout.list_item_quote);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if(data.moveToPosition(position)){
                    return data.getLong(POSITION_ID);
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}
