package io.github.t3r1jj.pbmap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qozix.tileview.TileView;
import com.qozix.tileview.hotspots.HotSpot;
import com.qozix.tileview.tiles.Tile;
import com.qozix.tileview.widgets.ScalingLayout;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

import io.github.t3r1jj.pbmap.model.Map;

public class MainActivity extends AppCompatActivity {

    public class MyTileView extends TileView {

        public MyTileView(Context context) {
            super(context);
        }

        public MyTileView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyTileView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void onScaleChanged(float scale, float previous) {
            super.onScaleChanged(scale, previous);
            highlightManager.setScale(scale);
        }
    }

    private ScalingLayout highlightManager;

    public class HighlightView extends View {
        Paint paint = new Paint();
        Rect rectangle;

        public HighlightView(Context context, Rect rectangle) {
            super(context);
            this.rectangle=rectangle;
        }

        @Override
        public void onDraw(Canvas canvas) {
            paint.setColor(Color.parseColor("#77ff0000"));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectangle, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(3);
            canvas.drawRect(rectangle, paint);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        highlightManager = new ScalingLayout( this );
        TileView tileView = new MyTileView( this );
        tileView.setSize(8*256, 8*256);  // the original size of the untiled image
        tileView.addDetailLevel(1f, "tiles/1/1000/1000.png", 256, 256);
        tileView.addDetailLevel(.5f, "tiles/1/500/500.png", 256, 256);
        tileView.addScalingViewGroup(highlightManager );
        View textView = new HighlightView(this, new Rect(0,0,300,400));
        ScalingLayout.LayoutParams lp = new ScalingLayout.LayoutParams(300, 400);
        highlightManager.addView( textView, lp );
        setContentView(tileView);

        new Controller(this, "data/1.xml").addData(tileView);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
