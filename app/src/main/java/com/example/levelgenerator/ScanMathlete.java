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

public class ScanMathlete extends AppCompatActivity {
    public static String input;
    private NfcAdapter myNFCAdapter;
    private PendingIntent myPendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_mathlete);
        /**
        myNFCAdapter = NfcAdapter.getDefaultAdapter(this);
        if (myNFCAdapter == null) {
            finish();
            return;
        }
        myPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0); **/
    }

    @Override
    public void onResume() {
        super.onResume();
       // myNFCAdapter.enableForegroundDispatch(this, myPendingIntent, null, null);
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
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String fullID = handleIntent(intent); //NFC Tag data
        int startLoc = fullID.indexOf("#");
        input = fullID.substring(startLoc, fullID.length() - 1);

        if (MainActivity.map.containsKey(input) && MainActivity.map.get(input) instanceof Mathlete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ScanMathlete.this);
            builder.setPositiveButton("Log In New Data", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getIntent().putExtra("MathleteID", input);
                    startActivity(new Intent(ScanMathlete.this, UpdateLevelActivity.class));
                }
            });

            builder.setNegativeButton("Check Progress", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getIntent().putExtra("MathleteID", input);
                    startActivity(new Intent(ScanMathlete.this, GameListActivity.class));
                }
            });
            AlertDialog alert1 = builder.create();
            alert1.show();
        }
        else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

            builder1.setTitle("Error!");
            builder1.setMessage("Invalid Tag: Please Scan A Mathlete Chip");
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

    //reads NFC Tag data and returns it
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


    public void onClick(View view) {
        Intent intent = new Intent(this, GameListActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText2);
        input = editText.getText().toString();
        if (MainActivity.map.containsKey(input) && MainActivity.map.get(input) instanceof Mathlete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ScanMathlete.this);
            builder.setPositiveButton("Log In New Data", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getIntent().putExtra("MathleteID", input);
                    startActivity(new Intent(ScanMathlete.this, UpdateLevelActivity.class));
                }
            });

            builder.setNegativeButton("Check Progress", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getIntent().putExtra("MathleteID", input);
                    startActivity(new Intent(ScanMathlete.this, GameListActivity.class));
                }
            });
            AlertDialog alert1 = builder.create();
            alert1.show();
        }
        else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Error!");
            builder1.setMessage("Invalid Tag: Please Scan A Mathlete Chip");
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
