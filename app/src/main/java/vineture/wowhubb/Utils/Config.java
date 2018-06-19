package vineture.wowhubb.Utils;

/**
 * Created by Salman on 22-03-2017.
 */
public class Config {

    public static final String WEB_URL_LOGIN = "http://104.197.80.225:3010/wow/login";
    public static final String WEB_URL_EMAILLOGIN = "http://104.197.80.225:3010/wow/emaillogin";
    public static final String WEB_URL_FORGOT = "http://104.197.80.225:3010/wow/forgetpassword";
    public static final String WEB_URL_RESETPWD = "http://104.197.80.225:3010/wow/changepassword";
    public static final String WEB_URL_OTP = "http://104.197.80.225:3010/wow/otp";
    public static final String WEB_URL_VERIFYOTP = "http://104.197.80.225:3010/wow/verifyotp";
    public static final String WEB_URL_MAILOTP = "http://104.197.80.225:3010/wow/mailotp";
    public static final String WEB_URL_SIGNUP = "http://104.197.80.225:3010/wow/signup";
    public static final String WEB_URL_PROFILE_IMG = "http://104.197.80.225:3010/wow/user/personalimage";
    public static final String WEB_URL_COVER_IMG = "http://104.197.80.225:3010/wow/user/personalcover";
    public static final String WEB_URL_SELFINTRO = "http://104.197.80.225:3010/wow/user/personalself";
    public static final String WEB_URL_UPDATE_PERSONAL = "http://104.197.80.225:3010/wow/user/updatepersonalprofile";
    public static final String WEB_URL_GETPERSONAL = "http://104.197.80.225:3010/wow/user/getpersonalprofile";
    public static final String WEB_URL_GETCHECKTAG = "http://104.197.80.225:3010/wow/checktagid";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";


    //Event Service Provider:
    public static final String WEB_URL_EVENT_SERVICE_PROVIDER="http://104.197.80.225:3010/wow/provider/getserviceproviders";
    public static final String WEB_URL_EVENT_SERVICE_CATEGORY="http://104.197.80.225:3010/wow/provider/getcategory";
    public static final String WEB_URL_EVENT_SERVICE_LIST="http://104.197.80.225:3010/wow/provider/getservices";
    public static final String WEB_URL_EVENT_FILTER_LIST="http://104.197.80.225:3010/wow/provider/filterservice";


    //WowtagVideos
    public static final String WEB_URL_GET_WOWTAGLIST="http://104.197.80.225:3010/wow/event/geteventtitles";
    public static final String WEB_URL_PARTICULARLIST="http://104.197.80.225:3010/wow/event/myeventtitles";
    public static final String WEB_URL_GET_PARTICULAR_WOWTAGLIST="http://104.197.80.225:3010/wow/event/getparticulareventtitle";
    public static final String WEB_URL_GET_PARTICULAR_WOWTAGLIST_DELETE="http://104.197.80.225:3010/wow/event/deletewowtagvideo";
    public static final String WEB_URL_FRTCH_WOWTAGID="http://104.197.80.225:3010/wow/event/fetchparticularevent";


    //Near By Events:
    public static final String WEB_URL_GET_NEARBY_EVENT_LIST="http://104.197.80.225:3010/wow/event/getnearbyeventslist";
    public static final String WEB_URL_GET_NEAR_BY_EVENTS="http://104.197.80.225:3010/wow/event/getnearbyevents";


    public static boolean isStringNullOrWhiteSpace(String value) {
        if (value == null) {
            return true;
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }


}
