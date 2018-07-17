package duchtse04705.fpt.edu.vn.projectprm;

import android.content.Context;

import ai.api.AIListener;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIRequest;

class AIBot {
          private final ai.api.android.AIConfiguration config = new ai.api.android.AIConfiguration(
                    AppConfig.clientAccessToken,
                    ai.api.android.AIConfiguration.SupportedLanguages.English,
                    ai.api.android.AIConfiguration.RecognitionEngine.System );
          public final AIService aiService;
          public AIDataService aiDataService;
          final public AIRequest aiRequest = new AIRequest();
          
          public AIBot(Context context, AIListener aiListener) {
                    // Init AI
                    aiService = AIService.getService( context, config );
                    aiService.setListener( aiListener );
                    aiDataService = new AIDataService( context, config );
                    aiRequest.setQuery( "hello" );
          }
}
