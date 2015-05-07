package com.example.root.usaraidlclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.usaraidlserver.IRemote;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

/* NOTAS el aidl debe de estar en un nombre de package identico a la app con la cual queremos comunicarnos
* y configurar el gradle para que busque los aidles
* */
    EditText mFirst,mSecond;
    Button mAdd,mSubtract,mClear;
    TextView mResultText;
    protected IRemote mService;
    ServiceConnection mServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirst = (EditText) findViewById(R.id.firstValue);
        mSecond = (EditText) findViewById(R.id.secondValue);
        mResultText = (TextView) findViewById(R.id.resultText);
        mAdd = (Button) findViewById(R.id.add);
        mAdd.setOnClickListener(this);

        initConnection();
    }



    void initConnection(){
        mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // TODO Auto-generated method stub
                mService = null;
                Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
                Log.d("IRemote", "Binding - Service disconnected");
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                // TODO Auto-generated method stub
                mService = IRemote.Stub.asInterface((IBinder) service);
                Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
                Log.d("IRemote", "Binding is done - Service connected");
            }
        };
        if(mService == null)
        {
            Intent it = new Intent();
            it.setAction("com.remote.service.CALCULATOR");
            //binding to remote service
            bindService(it, mServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }
    protected void onDestroy() {

        super.onDestroy();
        unbindService(mServiceConnection);
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.add:{
                int a = Integer.parseInt(mFirst.getText().toString());
                int b = Integer.parseInt(mSecond.getText().toString());

                try{
                    mResultText.setText("Result -> Add ->"+mService.add(a,b));
                    Log.d("IRemote", "Binding - Add operation");
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            break;
        }
    }
}
