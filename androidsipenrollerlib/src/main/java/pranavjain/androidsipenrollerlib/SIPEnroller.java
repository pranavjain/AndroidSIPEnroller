package pranavjain.androidsipenrollerlib;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.StrictMode;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pranav.Jain on 22/06/16.
 */
public class SIPEnroller {
    //Host Details contain all the data parsed from XML
    String[][] HostDetails = new String[0][];
    public int providerID = -1;
    public String provider;
    public String email;
    public String pass;
    public String display;
    public String username;
    public String[] ProviderName;
    public String status;

    public String Result(String p1) throws JSONException {
        //Fetch whether enrollment has been success or not
        JSONObject jsnobject = new JSONObject(p1);
        return jsnobject.getString("success");
    }

    public void Enrolment() throws IOException, JSONException {
        //POST request for user Enrolment
        String postEmail = HostDetails[providerID][0] + "=" + email;
        String postPass = HostDetails[providerID][1] + "=" + pass;
        String postdisplay = HostDetails[providerID][2] + "=" + display;
        String postusername = HostDetails[providerID][3] + "=" + username;
        String finalparameter = postEmail + "&" + postPass + "&" + postdisplay + "&" + postusername;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        URL url = new URL(HostDetails[providerID][4]);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
        dStream.writeBytes(finalparameter);
        dStream.flush();
        dStream.close();
        final StringBuilder output = new StringBuilder("Request URL " + url);
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        StringBuilder responseOutput = new StringBuilder();
        while ((line = br.readLine()) != null) {
            responseOutput.append(line);
        }
        br.close();

        status = Result(responseOutput.toString());

    }


    public String EnrolUser(Context AppContext) throws IOException, JSONException {
        //Fetches XML from Assests Folder
        try {
            InputStream SIPStream = AppContext.getAssets().open("ProviderData.xml");
            int length = SIPStream.available();
            byte[] data = new byte[length];
            SIPStream.read(data);
            String xml = new String(data);
            //Calls XML Parsing function
            DoParse(xml);
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        return status;
    }

    public void DoParse(String xml) throws IOException, JSONException {
        //Take Fetched XML as input and find relevant tags from it
        XmlPullParserFactory xmlFactoryObject = null;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        XmlPullParser myparser = null;
        try {
            myparser = xmlFactoryObject.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        InputStream is1 = new ByteArrayInputStream(xml.getBytes());
        try {
            myparser.setInput(is1, null);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        int event = 0;
        try {
            event = myparser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        int HostCount = -1;
        int ParaCount = 0;
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = myparser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    try {

                        if (myparser.getName().equals("number")) {
                            String NoOfHostS = myparser.nextText();
                            int NoOfHost = Integer.valueOf(NoOfHostS);
                            HostDetails = new String[NoOfHost][5];
                            ProviderName = new String[NoOfHost];


                        }
                        if (myparser.getName().equals("para")) {
                            HostDetails[HostCount][ParaCount] = myparser.nextText();
                            ParaCount++;

                        }
                        if (myparser.getName().equals("name")) {
                            ProviderName[HostCount] = myparser.nextText();


                        }
                        if (myparser.getName().equals("account")) {
                            HostCount++;
                            ParaCount = 0;

                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case XmlPullParser.TEXT:
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            try {
                event = myparser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        FindProviderID();

    }

    public void FindProviderID() throws IOException, JSONException {
        //Conver provider to providerID
        for (int a = 0; a < ProviderName.length; a++) {
            if (ProviderName[a].equals(provider)) {
                providerID = a;
                break;
            }
        }
        Enrolment();

    }


}
