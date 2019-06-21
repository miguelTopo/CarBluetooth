package co.edu.udistrital.carbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread extends Thread {

    private final BluetoothServerSocket mmServerSocket;

    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public AcceptThread(BluetoothAdapter mBluetoothAdapter) {
        BluetoothServerSocket tmp = null;
        try {
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("testBluetooth", MY_UUID);
        } catch (IOException e) {
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }

            //Si la conexión es aceptada
            if (socket != null) {                //
                try {
                    manageConnectedSocket(socket);
                    mmServerSocket.close();
                    break;
                } catch (IOException e) {
                }
            }
        }
    }

    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        //Vamos a validar algo aqui
        System.out.println("Esto va a hacer algo");
    }
}
