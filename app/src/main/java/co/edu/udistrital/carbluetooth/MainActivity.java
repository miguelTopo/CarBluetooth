package co.edu.udistrital.carbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 10;

    BluetoothAdapter mBluetoothAdapter;
    List<String> mArrayAdapter;

    BluetoothDevice device;

    Button upButton;
    Button downButton;
    Button leftButton;
    Button rightButton;
    Button stopButton;

    ConnectThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        enableBluetoothCapabilities();
        initBroadcastReceiver();

        if (mBluetoothAdapter.startDiscovery())
            System.out.println("funcionó");

        System.out.println(" A CONECTAR");

        if (device != null) {
            thread = new ConnectThread(device, mBluetoothAdapter);
            thread.start();
        }
    }

    private void initComponents() {
        this.upButton = findViewById(R.id.upButton);
        this.downButton = findViewById(R.id.downButton);
        this.leftButton = findViewById(R.id.leftButton);
        this.rightButton = findViewById(R.id.rightButton);
        this.stopButton = findViewById(R.id.stopButton);

        this.upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upAction();
            }
        });

        this.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftAction();
            }
        });

        this.downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downAction();
            }
        });

        this.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightAction();
            }
        });

        this.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAction();
            }
        });
    }

    private void initBroadcastReceiver() {
        final BroadcastReceiver mReceived = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equalsIgnoreCase(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceived, filter);
    }

    private void validateDevicesSync() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            //Existen dispositivos sincronizados
            for (BluetoothDevice d : pairedDevices) {
                if (mArrayAdapter == null)
                    mArrayAdapter = new ArrayList<>(1);
                if (d.getName().equalsIgnoreCase("HC-05")) {
                    device = d;
                    mArrayAdapter.add(d.getName() + "\n" + d.getAddress());
                    break;
                }
            }
        }
    }


    private void enableBluetoothCapabilities() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //Device does not support Bluetooth
            System.out.println("Este dispositivo no soporte Bluetooth");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtnIntent, REQUEST_ENABLE_BT);
            } else
                validateDevicesSync();
        }
    }

    public void upAction() {
        thread.getManageClientConnection().write("w".getBytes());
    }

    public void leftAction() {
        thread.getManageClientConnection().write("a".getBytes());
    }

    public void downAction() {
        thread.getManageClientConnection().write("s".getBytes());
    }

    public void rightAction() {
        thread.getManageClientConnection().write("d".getBytes());
    }

    public void stopAction() {
        thread.getManageClientConnection().write("e".getBytes());
    }
}