package duchtse04705.fpt.edu.vn.projectprm;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatAppMsgViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftMsgLayout;
        
        LinearLayout rightMsgLayout;
        
        TextView leftMsgTextView;
        
        TextView rightMsgTextView;
        
        public ChatAppMsgViewHolder(View itemView) {
                super( itemView );
                leftMsgLayout = itemView.findViewById( R.id.chat_left_msg_layout );
                rightMsgLayout = itemView.findViewById( R.id.chat_right_msg_layout );
                leftMsgTextView = itemView.findViewById( R.id.chat_left_msg_text_view );
                rightMsgTextView = itemView.findViewById( R.id.chat_right_msg_text_view );
        }
}
