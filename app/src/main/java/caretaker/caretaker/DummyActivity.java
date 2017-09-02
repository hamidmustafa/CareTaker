package caretaker.caretaker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.os.StrictMode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.InputStream;
import android.widget.*;
import android.os.NetworkOnMainThreadException;
public class DummyActivity extends AppCompatActivity {

    List headlines;
    List links;
    private String [] Array1 = {"1", "2","3"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ListView listView_parsed=(ListView) findViewById(R.id.listview11);
        headlines = new ArrayList();
        links = new ArrayList();

        try {
            URL url = new URL("http://feeds.pcworld.com/pcworld/latestnews");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(getInputStream(url), "UTF_8");
            boolean insideItem=false;
            int eventtype=xmlPullParser.getEventType();
            while(eventtype!=XmlPullParser.END_DOCUMENT)
            {if(eventtype==XmlPullParser.START_TAG) {
                if (xmlPullParser.getName().equalsIgnoreCase("items")) {
                    insideItem = true;
                } else if (xmlPullParser.getName().equalsIgnoreCase("title")) {
                    if (insideItem)
                        headlines.add(xmlPullParser.nextText());

                } else if (xmlPullParser.getName().equalsIgnoreCase("link")) {
                    if (insideItem)
                        links.add(xmlPullParser.nextText());
                }
            }
                else if(eventtype==XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item"))
            {
                insideItem=false;
            }
                eventtype=xmlPullParser.next();


            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter=new ArrayAdapter(this,R.layout.simplerow,R.id.rowTextView,headlines);
        listView_parsed.setAdapter(adapter);


    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }


    }
}
