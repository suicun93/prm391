package duchtse04705.fpt.edu.vn.projectprm;

public class ChatAppMsgDTO {
        public enum MSG_TYPE {
                SENT, RECEIVED;
                
                @Override
                public String toString() {
                        switch (this) {
                                case SENT:
                                        return "MSG_TYPE_SENT";
                                case RECEIVED:
                                        return "MSG_TYPE_RECEIVED";
                                default:
                                        return null;
                        }
                }
        }
        
        
        // Message content.
        private String msgContent;
        
        // Message type.
        private MSG_TYPE msgType;
        
        public ChatAppMsgDTO(MSG_TYPE msgType, String msgContent) {
                this.msgType = msgType;
                this.msgContent = msgContent;
        }
        
        public String getMsgContent() {
                return msgContent;
        }
        
        public void setMsgContent(String msgContent) {
                this.msgContent = msgContent;
        }
        
        public MSG_TYPE getMsgType() {
                return msgType;
        }
        
        public void setMsgType(MSG_TYPE msgType) {
                this.msgType = msgType;
        }
}
