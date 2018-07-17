package duchtse04705.fpt.edu.vn.projectprm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

class ChatAppMsgAdapter extends RecyclerView.Adapter<ChatAppMsgViewHolder> {
          private List<ChatAppMsgDTO> msgDtoList;
          
          public ChatAppMsgAdapter(List<ChatAppMsgDTO> msgDtoList) {
                    this.msgDtoList = msgDtoList;
          }
          
          @Override
          public void onBindViewHolder(ChatAppMsgViewHolder holder, int position) {
                    ChatAppMsgDTO msgDto = this.msgDtoList.get( position );
                    
                    // IF THE MESSAGE IS A RECEIVED MESSAGE
                    
                    if (ChatAppMsgDTO.MSG_TYPE.RECEIVED.equals( msgDto.getMsgType() )) {
                              
                              // Show received message in left linearlayout.
                              holder.leftMsgLayout.setVisibility( LinearLayout.VISIBLE );
                              holder.leftMsgTextView.setText( msgDto.getMsgContent() );
                              
                              // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
                              // Otherwise each iteview's distance is too big.
                              holder.rightMsgLayout.setVisibility( LinearLayout.GONE );
                    }
                    
                    // IF THE MESSAGE IS A SENT MESSAGE
                    else if (ChatAppMsgDTO.MSG_TYPE.SENT.equals( msgDto.getMsgType() )) {
                              
                              // Show sent message in right linearlayout.
                              holder.rightMsgLayout.setVisibility( LinearLayout.VISIBLE );
                              holder.rightMsgTextView.setText( msgDto.getMsgContent() );
                              
                              // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
                              // Otherwise each iteview's distance is too big.
                              holder.leftMsgLayout.setVisibility( LinearLayout.GONE );
                    }
          }
          
          @Override
          public ChatAppMsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    LayoutInflater layoutInflater = LayoutInflater.from( parent.getContext() );
                    View view = layoutInflater.inflate( R.layout.activity_chat_app_item_view, parent, false );
                    return new ChatAppMsgViewHolder( view );
          }
          
          @Override
          public int getItemCount() {
                    if (msgDtoList == null) {
                              msgDtoList = new ArrayList<>();
                    }
                    return msgDtoList.size();
          }
}
