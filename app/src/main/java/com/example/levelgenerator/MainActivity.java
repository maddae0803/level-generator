package com.example.levelgenerator;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final HashMap<String, Participant> map = new HashMap<String, Participant>();
    private NfcAdapter myNFCAdapter;
    private PendingIntent myPendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ReadFromDB rDB = new ReadFromDB();
            Participant[] pList = rDB.execute().get();
            for (Participant p: pList) {
                map.put(p.getID(), p);
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(map.size());
        /**
        myNFCAdapter = NfcAdapter.getDefaultAdapter(this);
        if (myNFCAdapter == null) {
            finish();
            return;
        }
        myPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);**/
        //Mathlete m = (Mathlete) map.get("#779b20c0-676c-4d5a-96a7-3a9f1f54d07e");
        //System.out.println(m.successes.size());
        //System.out.println(m.successes.get("BoxBlox").currentLevel);
        //ArrayList<String> items = new ArrayList<>();
        /**
        items.add(UUID.randomUUID().toString());
        items.add("#b10051e7-2489-dc91-090e-ef1809a2f6c9");
        items.add(new Integer(4).toString());
        items.add(new Integer(8).toString());
        items.add("Chocolate Fix");
        items.add(new DateTime().toString());
        try {
            new StoreToDB().execute(items).get();
            Toast.makeText(getApplicationContext(), "New Entry Added", Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
        } **/

    }

    @Override
    public void onResume() {
        super.onResume();
        //myNFCAdapter.enableForegroundDispatch(this, myPendingIntent, null, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
        if (myNFCAdapter != null) {
            myNFCAdapter.disableForegroundDispatch(this);
        }**/
    }

    @Override
    /** detects NFC Tag ("new intent") and gets the message via handleIntent
     *  gets rid of the semicolon
     *  if the tag exists in the Coach/Mathlete Map and the Participant is a Coach, open the next screen
     *  otherwise, print the error message
     * */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String fullID = handleIntent(intent);
        int startLoc = fullID.indexOf("#");
        String message = fullID.substring(startLoc, fullID.length() - 1);

        if (map.containsKey(message) && map.get(message) instanceof Coach)
            startActivity(new Intent(this, ScanMathlete.class));
        else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

            builder1.setTitle("Error!");
            builder1.setMessage("Invalid Tag: Please Scan A Coach Chip");
            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert1 = builder1.create();
            alert1.show();

        }

    }
    //retrieves the NFC Message
    public String handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefRecord relayRecord = ((NdefMessage)rawMsgs[0]).getRecords()[0];
            String nfcData = new String(relayRecord.getPayload());
            return nfcData;

        }
            return null;
    }
    //when Virtual Emulator needs to be used
    public void onClick(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        final String text = editText.getText().toString();
        if (MainActivity.map.containsKey(text) && MainActivity.map.get(text) instanceof Coach) {
            startActivity(new Intent(this, ScanMathlete.class));
        }
        else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Error!");
            builder1.setMessage("Invalid Tag: Please Scan A Coach Chip");
            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert1 = builder1.create();
            alert1.show();

        }

    }



}
