package com.example.week9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Context context = null;
    EditText date;
    EditText startBefore;
    EditText startAfter;
    EditText movie;
    RecyclerView recyclerView;
    Spinner spinner;
    TheaterCollection theaters;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyclerAdapter;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        date = (EditText) findViewById(R.id.giveDate);
        startBefore = (EditText) findViewById(R.id.giveStartBefore);
        startAfter = (EditText) findViewById(R.id.giveStartAfter);
        movie = (EditText) findViewById(R.id.giveName);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        spinner = (Spinner) findViewById(R.id.spinner);
        title = (TextView) findViewById(R.id.title);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        TheaterCollection.parseXML();
        theaters = new TheaterCollection();
        ArrayAdapter<Theater> adapter = new ArrayAdapter<Theater>(context, android.R.layout.simple_spinner_item, theaters.listTheaters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }



    public void search(View v) throws IOException, XmlPullParserException {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Theater selectedTheater = (Theater) spinner.getSelectedItem();
        String chosenDate = date.getText().toString();
        String chosenStart = "00:00:00"; //shows that start after this
        String chosenEnd = "23:59:59"; //shows that start before this
        String name = "";
        int flag = 0;
        if(chosenDate.equals("")){ 
            chosenDate = "13.08.2020";
        }
        if(!startBefore.getText().toString().equals("")){
            chosenEnd = startBefore.getText().toString();
        }
        if(!startAfter.getText().toString().equals("")){
            chosenStart = startAfter.getText().toString();
        }
        if(!movie.getText().toString().equals("")) { //if the name of the movie was given
            name = movie.getText().toString();
            if(selectedTheater.getID().equals("1029")){
                flag = 1;
            }
        }
        if(flag == 1) {
            List<XmlParser.Show> tempList = new ArrayList<XmlParser.Show>();
            List<String> IDs = new ArrayList<String>(Arrays.asList("1014", "1015", "1016", "1017", "1041", "1018", "1019", "1021", "1022"));
            for(String ID : IDs){
                String id = ID;
                System.out.println("ID: " + id);
                InputStream input = new URL("https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=" + chosenDate).openStream();
                XmlParser parser = new XmlParser();
                layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);
                tempList.addAll(parser.parse(input, id));
            }
            recyclerAdapter = new TheaterShowingsAdapter(tempList, chosenStart, chosenEnd, name, theaters); //parse returns list of shows
            recyclerView.setAdapter(recyclerAdapter);
            title.setText(movie.getText().toString());
        } else{
            InputStream input = new URL("https://www.finnkino.fi/xml/Schedule/?area=" + selectedTheater.getID() + "&dt=" + chosenDate).openStream();
            XmlParser parser = new XmlParser();
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            recyclerAdapter = new MyAdapter(parser.parse(input, selectedTheater.getID()), chosenStart, chosenEnd, name); //parse returns list of shows
            recyclerView.setAdapter(recyclerAdapter);
            title.setText("");
        }
    }


}
