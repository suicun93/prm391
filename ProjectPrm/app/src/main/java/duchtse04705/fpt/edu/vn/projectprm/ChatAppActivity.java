package duchtse04705.fpt.edu.vn.projectprm;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.Result;

public class ChatAppActivity extends AppCompatActivity implements AIListener {
        RecyclerView msgRecyclerView;
        List<ChatAppMsgDTO> msgDtoList = new ArrayList<>();
        ChatAppMsgAdapter chatAppMsgAdapter;
        AIBot aiBot;
        
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
                
                setupUI();
                initObject();
        }
        
        private void initObject() {
                // INIT AIBOT
                aiBot = new AIBot( this, this );
                
                // Create the initial data list.
                addMessage( ChatAppMsgDTO.MSG_TYPE.RECEIVED, "Hello" );
                
                // Create the data adapter with above data list.
                chatAppMsgAdapter = new ChatAppMsgAdapter( msgDtoList );
                
                // Set data adapter to RecyclerView.
                msgRecyclerView.setAdapter( chatAppMsgAdapter );
                
                final EditText msgInputText = (EditText) findViewById( R.id.chat_input_msg );
                
                Button msgSendButton = (Button) findViewById( R.id.chat_send_msg );
                
                msgSendButton.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                final String msgContent = msgInputText.getText().toString();
                                
                                if (! msgContent.isEmpty()) {
                                        new AsyncTask<AIRequest, Void, AIResponse>() {
                                                String requestData = "";
                                                
                                                @Override
                                                protected void onPreExecute() {
                                                        super.onPreExecute();
                                                        requestData = msgContent;
                                                        
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
                                                        if (aiResponse != null) {
                                                                Result result = aiResponse.getResult();
                                                                Fulfillment fulfillment = result.getFulfillment();
                                                                String receivedMessage = fulfillment.getSpeech();
                                                                
                                                                // Show results in View.
                                                                Log.i( "PRM391", "Query:" + result.getResolvedQuery() +
                                                                        "\nResult: " + receivedMessage );
                                                                addMessage( ChatAppMsgDTO.MSG_TYPE.RECEIVED, receivedMessage );
                                                        }
                                                }
                                        }.execute( aiBot.aiRequest );
                                }
                        }
                } );
        }
        
        private void addMessage(ChatAppMsgDTO.MSG_TYPE msgType, String msgContent) {
                ChatAppMsgDTO msgDto = new ChatAppMsgDTO( msgType, msgContent );
                synchronized (msgDtoList) {
                        if (msgDtoList == null) {
                                msgDtoList = new ArrayList<>();
                        } else {
                                msgDtoList.add( msgDto );
                        }
                        
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
        
        
        private void setupUI() {
                // Get RecyclerView object.
                msgRecyclerView = (RecyclerView) findViewById( R.id.chat_recycler_view );
                
                // Set RecyclerView layout manager.
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
                msgRecyclerView.setLayoutManager( linearLayoutManager );
        }
        
        @Override
        public void onResult(AIResponse response) {
                Log.d( "PRM391 result listened", "onResult: " + response.toString() );
                Result result = response.getResult();
                
                // Get parameters
//        String parameterString = "";
//        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
//            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
//                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
//            }
//        }
                //get output
                Fulfillment fulfillment = result.getFulfillment();
                String data = fulfillment.getSpeech();
                
                // Show results in TextView.
                addMessage( ChatAppMsgDTO.MSG_TYPE.RECEIVED, data );
        }
        
        @Override
        public void onError(AIError error) {
        
        }
        
        @Override
        public void onAudioLevel(float level) {
        
        }
        
        @Override
        public void onListeningStarted() {
        
        }
        
        @Override
        public void onListeningCanceled() {
        
        }
        
        @Override
        public void onListeningFinished() {
        
        }
}
