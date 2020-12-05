package com.wnetsi.w_net;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wnetsi.R;

import eneter.messaging.diagnostic.EneterTrace;
import eneter.messaging.endpoints.typedmessages.DuplexTypedMessagesFactory;
import eneter.messaging.endpoints.typedmessages.IDuplexTypedMessageSender;
import eneter.messaging.endpoints.typedmessages.IDuplexTypedMessagesFactory;
import eneter.messaging.endpoints.typedmessages.TypedResponseReceivedEventArgs;
import eneter.messaging.messagingsystems.messagingsystembase.IDuplexOutputChannel;
import eneter.messaging.messagingsystems.messagingsystembase.IMessagingSystemFactory;
import eneter.messaging.messagingsystems.tcpmessagingsystem.TcpMessagingSystemFactory;
import eneter.net.system.EventHandler;

public class SendData extends AppCompatActivity {

    public static class MyRequest{
        public String Text, Text2, Text3;
    }

    public static class MyResponse{
        public int Length, Length2, Length3;
    }

    private Handler myRefresh = new Handler();
    private EditText jmlPC, tMulai, tPakai, eResponse,eResponse2,eResponse3;
    private Button sendBtn;

    private IDuplexTypedMessageSender<MyResponse,MyRequest> mySender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);

        jmlPC = findViewById(R.id.editText_JumlahPC);
        tMulai = findViewById(R.id.editText_Waktumulai);
        tPakai = findViewById(R.id.editText_lamapemakaian);
        sendBtn = findViewById(R.id.button);
        //eResponse = findViewById(R.id.string_edittext);
        //eResponse2 = findViewById(R.id.string2_edittext);
        //eResponse3 = findViewById(R.id.string3_edittext);

        sendBtn.setOnClickListener(myOnSendRequestClickHandler);


        Thread anOpenConnectionThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
              try {
                  openConnection();
              }
              catch (Exception err){
                  EneterTrace.error("Open Connection Failed",err);
              }
            }
        });
        anOpenConnectionThread.start();
    }
    @Override
    public void onDestroy()
    {
        mySender.detachDuplexOutputChannel();

        super.onDestroy();
    }

    private void openConnection() throws Exception{
        IDuplexTypedMessagesFactory aSenderFactory = new DuplexTypedMessagesFactory();
        mySender =aSenderFactory.createDuplexTypedMessageSender(MyResponse.class,MyRequest.class);

        mySender.responseReceived().subscribe(myOnResponseHandler);

        IMessagingSystemFactory aMessaging = new TcpMessagingSystemFactory();
        IDuplexOutputChannel anOutputChannel =
                aMessaging.createDuplexOutputChannel("tcp://10.0.2.2:8060/");

        mySender.attachDuplexOutputChannel(anOutputChannel);
    }

    private void onSendRequest (View v){
        MyRequest aRequestMsg = new MyRequest();
        aRequestMsg.Text = jmlPC.getText().toString();
        aRequestMsg.Text2 = tMulai.getText().toString();
        aRequestMsg.Text3 = tPakai.getText().toString();

        try {
            mySender.sendRequestMessage(aRequestMsg);
        }
        catch (Exception err){
            EneterTrace.error("Sending Message Failed",err);
        }
    }
    private void onResponseReceived (Object sender, final TypedResponseReceivedEventArgs<MyResponse>e){
        myRefresh.post(new Runnable() {
            @Override
            public void run() {
                //eResponse.setText(Integer.toString(e.getResponseMessage().Length));
                //eResponse2.setText(Integer.toString(e.getResponseMessage().Length2));
                //eResponse3.setText(Integer.toString(e.getResponseMessage().Length3));
            }
        });
    }

    private EventHandler <TypedResponseReceivedEventArgs<MyResponse>> myOnResponseHandler
            = new EventHandler<TypedResponseReceivedEventArgs<MyResponse>>() {
        @Override
        public void onEvent(Object sender,
                            TypedResponseReceivedEventArgs<MyResponse> e) {
            onResponseReceived(sender,e);
        }
    };

    private View.OnClickListener myOnSendRequestClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onSendRequest(v);
            Toast.makeText(SendData.this,"Booking berhasil dibuat",Toast.LENGTH_SHORT).show();
        }
    };
}