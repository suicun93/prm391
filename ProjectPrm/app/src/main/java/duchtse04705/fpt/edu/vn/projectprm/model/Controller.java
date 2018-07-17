package duchtse04705.fpt.edu.vn.projectprm.model;

/**
 * Created by huy_td on 7/16/18.
 */

import java.util.ArrayList;

import duchtse04705.fpt.edu.vn.projectprm.ChatAppActivity;

public class Controller {
          public static void GenData() {
                    ChatAppActivity.listAllSick.add( new Sick( "Cảm cúm", new String[]{"headache", "cough", "sneeze"}, new String[]{"Bạn bị cúm"} ) );
                    ChatAppActivity.listAllSick.add( new Sick( "Sốt", new String[]{"headache", "high body temperature", "headache"}, new String[]{"Bạn bị "} ) );
                    ChatAppActivity.listAllSick.add( new Sick( "Đau đầu", new String[]{"headache", "dizziness"}, new String[]{"Bạn bị đau "} ) );
                    ChatAppActivity.listAllSick.add( new Sick( "Viêm họng ", new String[]{"cough", "adenite"}, new String[]{"Bạn bị viêm họng"} ) );
          }
}
