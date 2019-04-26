package apps.abhibhardwaj.com.doctriod.patient.vdoctor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class VDoctorActivity extends AppCompatActivity {
  private Toolbar toolbar;
  private TextView tvToolbarTitle;
  private ImageView ivBack;

  ChatArrayAdapter chatArrayAdapter;
  EditText chatText;
  ListView chatList;
  Button sendbtn;
  Button begin;
  String  Authorization="";
  String Recieved="";
  Context mContext=this;
  int contentview;
  //boolean typing=true;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vdoctor);
    contentview=R.layout.activity_vdoctor;

    initToolbar();


    begin =(Button) findViewById(R.id.begin);
    begin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        setContentView(R.layout.activity_main);
        contentview=R.layout.activity_main;
        int v=R.layout.activity_main;
        chatList = (ListView) findViewById(R.id.chatlist);
        chatText = (EditText) findViewById(R.id.msgtyped);
        changeview();
        chatArrayAdapter = new ChatArrayAdapter(mContext, new ArrayList<ChatMessage>(),chatList);
        chatList.setAdapter(chatArrayAdapter);
        chatArrayAdapter.add(new ChatMessage(1,"typing..."));
        //  chatArrayAdapter.getItem(0).setIsMine(1);
        new InitVDoctor(mContext).execute();
      }
    });

  }

  private void initToolbar() {
    toolbar = findViewById(R.id.vdoctor_tool_bar);
    tvToolbarTitle = findViewById(R.id.vdoctor_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("Virtual Doctor");
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    ivBack = findViewById(R.id.vdoctor_toolbar_iv_back);

    ivBack.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(VDoctorActivity.this, HomeActivity.class));
        finish();
      }
    });

  }


  public void changeview(){
    sendbtn = (Button) findViewById(R.id.send_btn);
    sendbtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        String message = chatText.getText().toString();
        ChatMessage chatMessage = new ChatMessage(0, message);

        chatArrayAdapter.add(chatMessage);

        JSONObject jsn=new JSONObject();
        try {
          jsn.put("message",message);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        new Chat(mContext).execute(Authorization,jsn.toString());
        chatText.setText("");
        chatArrayAdapter.add(new ChatMessage(1,"typing..."));
        chatList.setSelection(chatArrayAdapter.getCount() - 1);
      }
    });
  }
  public void ServerWelcome(JSONObject res){
    try {
      Recieved=(res.getString("message"));
      Authorization=res.getString("uuid");
      chatArrayAdapter.remove((ChatMessage) chatArrayAdapter.chatList.getItemAtPosition(chatArrayAdapter.getCount()-1));
      chatArrayAdapter.notifyDataSetChanged();

      ChatMessage chatMessage = new ChatMessage(1, Recieved);
      chatArrayAdapter.add(chatMessage);
      chatList.setSelection(chatArrayAdapter.getCount() - 1);
    }catch(JSONException e){
      e.printStackTrace();
    }
  }
  //  this method recieves the server response
  public void ServerChat(JSONObject res){
    try {
      Recieved=(res.getString("message"));
      Log.d("nfndskjfnjkdsn   ",Recieved);
      chatArrayAdapter.remove((ChatMessage) chatArrayAdapter.chatList.getItemAtPosition(chatArrayAdapter.getCount()-1));
      chatArrayAdapter.notifyDataSetChanged();

      ChatMessage chatMessage = new ChatMessage(1, Recieved);
      chatArrayAdapter.add(chatMessage);
      chatList.setSelection(chatArrayAdapter.getCount() - 1);

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }


}

