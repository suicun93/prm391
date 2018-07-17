package duchtse04705.fpt.edu.vn.projectprm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ChangedPackages;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.Result;
import duchtse04705.fpt.edu.vn.projectprm.model.Controller;
import duchtse04705.fpt.edu.vn.projectprm.model.Sick;

public class ChatAppActivity extends AppCompatActivity implements AIListener {
          
          private RecyclerView msgRecyclerView;
          private Button chatVoiceButton;
          private TextView txtInfor;
          private EditText msgInputText;
          private final List<ChatAppMsgDTO> msgDtoList = new ArrayList<>();
          private ChatAppMsgAdapter chatAppMsgAdapter;
          private AIBot aiBot;
          GoogleSignInClient mGoogleSignInClient;
          public static final int RC_SIGN_IN = 1;
          public static ArrayList<Sick> listAllSick = new ArrayList<>();
          public static ArrayList<Sick> listSickMaybe_User = new ArrayList<>();
          
          @Override
          protected void onStart() {
                    super.onStart();
                    AppConfig.checkAudioRecordPermission( this );
          }
          
          @Override
          protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate( savedInstanceState );
                    setContentView( R.layout.activity_chat_app );
                    setTitle( "Android Project - Medicine chat bot" );
                    Controller.GenData();
                    setupUI();
                    initObject();
                    setUpGGService();
          }
          
          private void setUpGGService() {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                              .requestEmail()
                              .build();
                    mGoogleSignInClient = GoogleSignIn.getClient( this, gso );
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount( this );
                    updateUI( account );
                    
          }
          
          private void updateUI(GoogleSignInAccount acct) {
                    acct = GoogleSignIn.getLastSignedInAccount( ChatAppActivity.this );
                    if (acct != null) {
                              String personName = acct.getDisplayName();
                              txtInfor.setText( "Hello : " + personName );
                    }
          }
          
          private void sendMessage(final String message) {
                    new AsyncTask<AIRequest, Void, AIResponse>() {
                              String requestData = "";
                              
                              @Override
                              protected void onPreExecute() {
                                        super.onPreExecute();
                                        requestData = message;
                                        
                                        // Send request to AI
                                        aiBot.aiRequest.setQuery( requestData );
                                        
                                        // Set UI
                                        addMessage( ChatAppMsgDTO.MSG_TYPE.SENT, requestData );
                                        msgInputText.setText( "" );
                              }
                              
                              @Override
                              protected AIResponse doInBackground(AIRequest... requests) {
                                        //final AIRequest request = requests[0];
                                        // Send request and wait response
                                        try {
                                                  final AIResponse response = aiBot.aiDataService
                                                            .request(
                                                                      aiBot.aiRequest );
                                                  return response;
                                        } catch (AIServiceException e) {
                                                  Log.e( "PRM391", e.getMessage() );
                                        }
                                        return null;
                              }
                              
                              @Override
                              protected void onPostExecute(AIResponse aiResponse) {
                                        // Received response
                                        responseProcess( aiResponse, false );
                              }
                    }.execute( aiBot.aiRequest );
          }
          
          private void initObject() {
                    // Init Aibot
                    aiBot = new AIBot( this, this );
                    
                    // Create first greeting.
                    addMessage( ChatAppMsgDTO.MSG_TYPE.RECEIVED, "Welcome to Health Care Chatbot." );
                    
                    // Create the data adapter with above data list.
                    chatAppMsgAdapter = new ChatAppMsgAdapter( msgDtoList );
                    
                    // Set data adapter to RecyclerView.
                    msgRecyclerView.setAdapter( chatAppMsgAdapter );
                    
                    Button msgSendButton = findViewById( R.id.chat_send_msg );
                    
                    msgSendButton.setOnClickListener( new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                        final String msgContent = msgInputText.getText().toString().trim();
                                        if (! msgContent.isEmpty()) {
                                                  sendMessage( msgContent );
                                        }
                              }
                    } );
                    Button btnSignIn = findViewById( R.id.btn_signin );
                    btnSignIn.setOnClickListener( new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                                        startActivityForResult( signInIntent, RC_SIGN_IN );
                              }
                    } );
                    txtInfor = findViewById( R.id.txtInforName );
                    
          }
          
          @Override
          protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                    super.onActivityResult( requestCode, resultCode, data );
                    if (requestCode == RC_SIGN_IN) {
                              // The Task returned from this call is always completed, no need to attach
                              // a listener.
                              Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent( data );
                              handleSignInResult( task );
                    }
          }
          
          private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
                    try {
                              GoogleSignInAccount account = completedTask.getResult( ApiException.class );
                              
                              // Signed in successfully, show authenticated UI.
                              updateUI( account );
                    } catch (ApiException e) {
                              // The ApiException status code indicates the detailed failure reason.
                              // Please refer to the GoogleSignInStatusCodes class reference for more information.
                              Log.w( "Stt", "signInResult:failed code=" + e.getStatusCode() );
                              updateUI( null );
                    }
          }
          
          private void setupUI() {
                    // Get Chat voice button
                    chatVoiceButton = findViewById( R.id.chatVoice );
                    if (chatVoiceButton != null) {
                              chatVoiceButton.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                  if (isListening()) {
                                                            aiBot.aiService.stopListening();
                                                  } else {
                                                            aiBot.aiService.startListening();
                                                  }
                                        }
                              } );
                    }
                    
                    // Get input text
                    msgInputText = findViewById( R.id.chat_input_msg );
                    
                    // Get RecyclerView object.
                    msgRecyclerView = findViewById( R.id.chat_recycler_view );
                    
                    // Set RecyclerView layout manager.
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
                    msgRecyclerView.setLayoutManager( linearLayoutManager );
          }
          
          
          // Listen mode
          @Override
          public void onResult(AIResponse response) {
                    responseProcess( response, true );
          }
          
          @Override
          public void onError(AIError error) {
                    addMessage( ChatAppMsgDTO.MSG_TYPE.RECEIVED, "Error" + error.toString() );
          }
          
          @Override
          public void onAudioLevel(float level) {
          
          }
          
          @Override
          public void onListeningStarted() {
                    chatVoiceButton.setText( "Listening" );
          }
          
          @Override
          public void onListeningCanceled() {
                    chatVoiceButton.setText( "Voice" );
          }
          
          @Override
          public void onListeningFinished() {
                    chatVoiceButton.setText( "Voice" );
          }
          
          @Override
          public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                    super.onRequestPermissionsResult( requestCode, permissions, grantResults );
                    switch (requestCode) {
                              case 33: {
                                        // If request is cancelled, the result arrays are empty.
                                        if (grantResults.length > 0
                                                  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                                  // permission was granted, yay! Do the
                                                  // contacts-related task you need to do.
                                        } else {
                                                  // permission denied, boo! Disable the
                                                  // functionality that depends on this permission.
                                        }
                              }
                    }
          }
          
          private boolean isListening() {
                    return chatVoiceButton.getText().toString().trim().equalsIgnoreCase( "Listening" );
          }
          
          private void addMessage(ChatAppMsgDTO.MSG_TYPE msgType, String msgContent) {
                    ChatAppMsgDTO msgDto = new ChatAppMsgDTO( msgType, msgContent );
                    synchronized (msgDtoList) {
                              msgDtoList.add( msgDto );
                              
                              int newMsgPosition = msgDtoList.size() - 1;
                              
                              // Notify recycler view insert one new data.
                              if (chatAppMsgAdapter == null) {
                                        chatAppMsgAdapter = new ChatAppMsgAdapter( msgDtoList );
                              }
                              chatAppMsgAdapter.notifyItemInserted( newMsgPosition );
                              
                              // Scroll RecyclerView to the last message.
                              msgRecyclerView.scrollToPosition( newMsgPosition );
                    }
          }
          
          private boolean isList(String string) {
                    return string.contains( AppConfig.regex );
          }
          
          private void popupToSelect(final String[] list) {
                    AlertDialog.Builder builder = new AlertDialog.Builder( this );
                    builder.setTitle( list[0] );
                    
                    // Cut the first element of array
                    final String[] newArray = new String[list.length - 1];
                    System.arraycopy( list, 1, newArray, 0, list.length - 1 );
                    builder.setItems( newArray, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                        sendMessage( newArray[which] );
                              }
                    } );
                    builder.show();
          }
          
          private void responseProcess(AIResponse aiResponse, boolean fromListening) {
                    if (aiResponse != null) {
                              Result result = aiResponse.getResult();
                              Fulfillment fulfillment = result.getFulfillment();
                              String receivedMessage = fulfillment.getSpeech();
                              
                              // Show results in View.
                              Log.i( "stt", "Query:" + result.getResolvedQuery() +
                                        "\nResult: " + receivedMessage );
                              
                              // if process when listening then display message what was heard
                              if (fromListening) {
                                        addMessage( ChatAppMsgDTO.MSG_TYPE.SENT, result.getResolvedQuery() );
                              }
                              
                              // Check received Message is json or not
                              if (isList( receivedMessage )) {
                                        String[] list = receivedMessage.split( AppConfig.regex );
                                        String stringResone = list[0];
                                        addMessage( ChatAppMsgDTO.MSG_TYPE.RECEIVED, stringResone );
                                        popupToSelect( list );
                              } else {
                                        String stringResone = receivedMessage;
                                        if (stringResone.contains( "200/" )) {
                                                  stringResone = stringResone.substring( 4 );
                                                  addMessage( ChatAppMsgDTO.MSG_TYPE.RECEIVED, stringResone );
                                                  validSick( stringResone );
                                        } else {
                                                  addMessage( ChatAppMsgDTO.MSG_TYPE.RECEIVED, receivedMessage );
                                        }
                              }
                    }
          }
          
          public void validSick(String stringResonpe) {
                    int countCorr = 0;
                    int index = 0;
                    for (int i = 0; i < listAllSick.size(); i++) {
                              if (listAllSick.get( i ).isHasBeen()) {
                                        boolean isHasBeenCheck = false;
                                        for (int j = 0; j < listAllSick.get( i ).getListSymptom().length; j++) {
                                                  if (stringResonpe.contains( listAllSick.get( i ).getListSymptom()[j] )) {
                                                            countCorr++;
                                                            listAllSick.get( i ).setHasBeen( true );
                                                            index = i;
                                                            isHasBeenCheck = true;
                                                            break;
                                                  }
                                        }
                                        if (! isHasBeenCheck) {
                                                  listAllSick.get( i ).setHasBeen( false );
                                        }
                              }
                    }
                    if (countCorr == 1) {
                              addMessage( ChatAppMsgDTO.MSG_TYPE.RECEIVED, listAllSick.get( index ).getList_drugtreatment()[0] );
                    } else {
                              addMessage( ChatAppMsgDTO.MSG_TYPE.RECEIVED, "Xin lỗi. Hiện tại chưa tìm ra bệnh. Vui lòng cấp thêm thông tin!" );
                    }
          }
}
