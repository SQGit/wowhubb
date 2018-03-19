package sql;

import android.provider.BaseColumns;

/**
 * Created by delaroy on 5/11/17.
 */
public class BeneficiaryContract {

    public static final class BeneficiaryEntry implements BaseColumns {

        public static final String TABLE_NAME = "viewvenuaddress";
        public static final String COLUMN_BENEFICIARY_ADDRESS = "address";
        public static final String COLUMN_BENEFICIARY_CITY = "city";
        public static final String COLUMN_BENEFICIARY_ID= "id";
        public static final String COLUMN_BENEFICIARY_STATE = "state";
        public static final String COLUMN_BENEFICIARY_ZIPCODE = "zipcode";
        public static final String COLUMN_BENEFICIARY_VENUENAME= "venuename";


    }
}
