package com.sayone.hush;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity {

    public static int Category;
    private int tag;
    String status,id;
    ImageView im;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        final Button all_btn;
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        CalendarEventChangeReceiver playObject = new CalendarEventChangeReceiver();  //me
        status = playObject.status1();            //me
        id=playObject.id1();
         SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		boolean helpShown = prefs.getBoolean("shownHelp", false);
		if (!helpShown) {
			InitializeHelp();
		}
		RelativeLayout splash = (RelativeLayout) findViewById(R.id.splash);
		Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		splash.startAnimation(fadeOut);
		HashMap<String, List<CalendarEvent>> events = CalendarService
				.readCalendar(this);
		if (events == null) {
			return;
		}
		ListView eventList = (ListView) findViewById(R.id.event_list);
		final ArrayList<CalendarEvent> eventsToList = new ArrayList<CalendarEvent>();
		for (List<CalendarEvent> eventsList : events.values()) {
			for (CalendarEvent event : eventsList) {
				eventsToList.add(event);
			}
		}
		EventListAdapter eListAdapter = new EventListAdapter(this, eventsToList);
		eventList.setAdapter(eListAdapter);
		eventList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				String id = eventsToList.get(position).getId();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				// Android 2.2+
				intent.setData(Uri
						.parse("content://com.android.calendar/events/"
								+ String.valueOf(id)));
				// Android 2.1 and below.
				// intent.setData(Uri.parse("content://calendar/events/" +
				// String.valueOf(calendarEventID)));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NO_HISTORY
						| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				startActivity(intent);
			}

		});


        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                                               //me
        switch (item.getItemId()) {
            case R.id.list_all:
                Log.v("ttt", "You pressed the icon!");
                Category =1;
                Toast.makeText(this,"list all:"+Category,Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;
            case R.id.list_selected:Log.v("ttt", "You pressed the text!");
                Category =0;
                Toast.makeText(this,"list selected:"+Category,Toast.LENGTH_LONG).show();
                Intent intent1 = getIntent();
                finish();
                startActivity(intent1);
                break;
        }
        return true;
    }






    int currentIndex = 0;

	private void InitializeHelp() {
		final RelativeLayout helpLayout = (RelativeLayout) findViewById(R.id.help_layout);
		ImageView[] pages = new ImageView[6];
		final int[] pageIds = {R.id.page1, R.id.page2,R.id.page3, R.id.page4, R.id.page5,R.id.page6};
		for(int i=0;i<6; i++){
			pages[i] = (ImageView) findViewById(pageIds[i]);
		}
        im= (ImageView) findViewById(R.id.img);
		
		helpLayout.setVisibility(View.VISIBLE);
		Animation in = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left);
		Animation out = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);
		final Button btnNext = (Button) findViewById(R.id.next_btn);
		final TextSwitcher helpSwitcher = (TextSwitcher) findViewById(R.id.help_text);



		Resources res = getResources();
		final String[] helps = res.getStringArray(R.array.help_texts);
		helpSwitcher.setInAnimation(in);
		helpSwitcher.setOutAnimation(out);
		helpSwitcher.setFactory(new ViewFactory() {

			public View makeView() {
				// TODO Auto-generated method stub
				// create new textView and set the properties like clolr, size
				// etc
				TextView myText = new TextView(MainActivity.this);
				myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
				myText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);
				myText.setTextColor(Color.WHITE);
                // Loading Font Face
                Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/our lil secret forever.ttf");
                // Applying font
                myText.setTypeface(tf);
				return myText;
			}
		});
		helpSwitcher.setText(helps[currentIndex]);
		btnNext.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageView prevPage = (ImageView) findViewById(pageIds[currentIndex]);
				currentIndex++;
				if (currentIndex >= helps.length) {
					helpLayout.setVisibility(View.GONE);
					SharedPreferences.Editor editor = getPreferences(
							MODE_PRIVATE).edit();
					editor.putBoolean("shownHelp", true);
					editor.commit();
					return;
				}

				ImageView nextPage = (ImageView) findViewById(pageIds[currentIndex]);
				prevPage.setImageResource(R.drawable.marker_inactive);
				nextPage.setImageResource(R.drawable.marker_active);
				helpSwitcher.setText(helps[currentIndex]);
                if(currentIndex ==5)
                {
                    im.setVisibility(View.VISIBLE);
                    helpSwitcher.setVisibility(View.INVISIBLE);
                    btnNext.setText("OK");
                    btnNext.setBackgroundColor(R.color.HushLightBlue);
             
                }
			}
		});
	}


    public int getCategory()                                                                        //me
    {

        return Category;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }



}
