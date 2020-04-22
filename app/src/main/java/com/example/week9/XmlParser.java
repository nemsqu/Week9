package com.example.week9;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    private static final String ns = null;

    public List<Show> parse(InputStream in, String ID) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser, ID);
        } finally {
            in.close();
        }
    }
    private List<Show> readFeed(XmlPullParser parser, String ID) throws XmlPullParserException, IOException {
        List<Show> shows = new ArrayList<Show>();

        parser.require(XmlPullParser.START_TAG, ns, "Schedule");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            System.out.println(name);
            // Starts by looking for the entry tag
            if (name.equals("Shows")) {
                readShows(parser, shows, ID);
            }else {
                skip(parser);
            }
        }
        return shows;
    }
public static class Show {
    public final String startTime;
    public final String endTime;
    public final String movieName;
    public final String theaterID;

    private Show(String start, String end, String name, String ID) {
        this.startTime = start;
        this.endTime = end;
        this.movieName = name;
        this.theaterID = ID;
    }
    public String getStart(){
        return startTime;
    }
    public String getEnd(){
        return endTime;
    }
    public String getMovieName(){
        return movieName;
    }
}

    private List<Show> readShows(XmlPullParser parser, List<Show> shows, String ID) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Shows");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            System.out.println(name);
            // Starts by looking for the entry tag
            if (name.equals("Show")) {
                shows.add(readEntry(parser, ID));
            }else {
                System.out.println("nimi oli " + name);
                skip(parser);
                System.out.println("Skipattu. Seuraavaks " + parser.next());
            }
        }
        return shows;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private Show readEntry(XmlPullParser parser, String ID) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Show");
        String start = null;
        String end = null;
        String movie = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("dttmShowStart")) {
                start = readStart(parser);
            } else if (name.equals("dttmShowEnd")) {
                end = readEnd(parser);
            } else if (name.equals("Title")) {
                movie = readMovie(parser);
            } else {
                skip(parser);
            }
        }
        return new Show(start, end, movie, ID);
    }

    // Processes title tags in the feed.
    private String readStart(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "dttmShowStart");
        String start = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "dttmShowStart");
        return start;
    }

    // Processes link tags in the feed.
    private String readEnd(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "dttmShowEnd");
        String end = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "dttmShowEnd");
        return end;
    }

    // Processes summary tags in the feed.
    private String readMovie(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Title");
        String movie = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Title");
        return movie;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            System.out.println(parser.getEventType());
            System.out.println(XmlPullParser.START_TAG);
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }



}

