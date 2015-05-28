package com.loxeras.dalimonitor;
//File: 	MainActivity.java
//design: 	activity_main.xml
//date:		22.05.14
//(c) 2014 by Dario Duff
//Main form, allows to connect to Bluetooth device
////////////////////////////////////////////////////////////////////////////////

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class connect extends ActionBarActivity {
    private BluetoothAdapter btAdapter = null;
    private Set<BluetoothDevice>pairedDevices;
    private BluetoothSocket btSocket;
    private OutputStream outStream;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ArrayList address = null;
    private ListView lv;
    private EditText connect2index;
    private Button bt_connect;


    public final static String EXTRA_MESSAGE = "com.tridonic.dalimonitor.MESSAGE";
    private static BluetoothThread BluetoothThread = new BluetoothThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hide the Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);
        lv = (ListView)findViewById(R.id.listView1);


        //--------------------------------------
        //Get the BT adapter
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (btAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!btAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
        }

        connect2index = (EditText)findViewById(R.id.et_Connect2index);    //Zu verbindendes Geraet
        bt_connect = (Button)findViewById(R.id.bt_connect);
        bt_connect.setEnabled(false);

    }

    //Called when devices should be show
    public void create_list(View view){
        //Erstellt die lIste mit den MAC addressen und dem Geraetenamen
        pairedDevices = btAdapter.getBondedDevices();
        //hauhdauhasdas
        ArrayList list = new ArrayList();
        address = new ArrayList();
        for(BluetoothDevice bt : pairedDevices){
            list.add(bt.getName() + "  ["+ bt.getAddress() + "]");
            address.add(bt.getAddress());
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        lv.setChoiceMode(1);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                connect2index.setText(position+"");
                bt_connect.setEnabled(true);
            }

        });
    }

    /////////////////////////////
    //@desc:	Connect to a Device whos choosen from the list view-
    //@param:	none
    //@return:	none
    ////////////////////////////
    public void connect(View view) throws IOException{
        //Called when button "connect" clicked
        int index = 0;	//Set intext value to default


        if (!btAdapter.isEnabled()){
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
        }

        index = Integer.parseInt(connect2index.getText().toString());
        BluetoothThread.connect((String) address.get(index));
        Intent intent = new Intent(this.getApplicationContext(), monitor.class);	//Opens command.class
        // String message = (String) address.get(index);
        // intent.putExtra(EXTRA_MESSAGE, message);

        //Open a new site to show incoming messages
        startActivity(intent);
    }

    ////////////////////////////////
    //@desc 	Returns the Devices who are paired with the Smartphone
    //@param: 	none
    //@return: 	paired devices
    //
    /////////////////////////////
    public Set<BluetoothDevice> getPairedDevices() {
        return pairedDevices;
    }

    public void setPairedDevices(Set<BluetoothDevice> pairedDevices) {
        this.pairedDevices = pairedDevices;
    }
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment(){/*Nothing here*/}

    }
    public static BluetoothThread get_thread(){
        //Returns the Bluetootht thread for global use
        return BluetoothThread;
    }
}
