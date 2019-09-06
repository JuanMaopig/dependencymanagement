package com.sinux.bai.common.message.constats;

/**
 * @author Sopp
 * @create 2019-04-08
 * @Description：Overall Constants，Save Constants
 */
public class Constants {
    /*--------------------------------------------return code------------------------------------------------*/
    /**
     * request success
     */
    public static final int CODE_SUCCESS = 0;

    /**
     * request fail
     */
    public static final int CODE_FAIL = 1;

    /**
     * request error
     */
    public static final int CODE_ERROR = 2;

    /**
     * no data
     */
    public static final int CODE_NODATA = 300;

    /**
     * param error
     */
    public static final int CODE_ERRORPARAM = 301;

    /**
     * warning
     */
    public static final int CODE_WARNING = 303;

    /**
     * method error
     */
    public static final int CODE_METHODERROR = 305;

    /**
     * no permission
     */
    public static final int CODE_NOPERMISSION = 306;




    /*-------------------------------------------return message-----------------------------------------------*/
    /**
     * operation success
     */
    public static final String MSG_SUCCESS = "Successful operation";

    /**
     * operation fail
     */
    public static final String MSG_FAIL = "operation failed";

    /**
     * no data
     */
    public static final String MSG_NODATA = "Request no data";

    /**
     * warning
     */
    public static final String MSG_WARNING = "Server exception";

    /**
     * method error
     */
    public static final String MSG_METHODERROR = "Please use the correct request method";

    /**
     * no remind
     */
    public static final String MSG_REMIND = "No access";


    /*-------------------------------------------overall magic constant-----------------------------------------------*/

    /**
     * default page number
     */
    public static final int DEFAULT_PAGENUM = 1;
    /**
     * default page size
     */
    public static final int DEFAULT_PAGESIZE = 20;

}
