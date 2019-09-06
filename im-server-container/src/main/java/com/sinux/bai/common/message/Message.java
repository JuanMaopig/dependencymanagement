package com.sinux.bai.common.message;

import com.sinux.base.support.common.constants.Constants;

/**
 * @author Sopp
 * @create 2019-04-08
 * @Description：Global return information definition class
 */
public class Message{

    /*---------------------------------------------Define basic parameters to start-----------------------------------------------------*/

	/**
     * Return status code
     */
    private int code;

    /**
     * Return to the front end prompt information
     */
    private String msg;

    /**
     * Return data
     */
    private Object data;

    /**
     * Paging data
     */
    private Long count;
    

    /*---------------------------------------------Define the end of the basic parameters-----------------------------------------------------*/

    /*---------------------------------------------Define the constructor to start-----------------------------------------------------*/

    public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	/**
     * Functional Description: Construction Method - No Data
     *
     * @param: [codeSuccess, msgSuccess]
     * @return:
     * @auther: Sopp
     * @date: 2019/4/8 10:52
     */
    public Message(int codeSuccess, String msgSuccess) {
        this.code = codeSuccess;
        this.msg = msgSuccess;
    }

    /**
     * Functional Description: Construction Method - with data
     *
     * @param: [codeSuccess, msgSuccess, object]
     * @return:
     * @auther: Sopp
     * @date: 2019/4/8 10:54
     */
    public Message(int codeSuccess, String msgSuccess, Object object) {
        this.code = codeSuccess;
        this.msg = msgSuccess;
        this.data = object;
    }

    /**
     * Functional Description: Construction Method - with data and paging
     *
     * @param: [codeSuccess, msgSuccess, object, count]
     * @return:
     * @auther: Sopp
     * @date: 2019/4/8 11:07
     */
    public Message(int codeSuccess, String msgSuccess, Object object, Long count) {
        this.code = codeSuccess;
        this.msg = msgSuccess;
        this.data = object;
        this.count = count;
    }

    /*---------------------------------------------Define the constructor end-----------------------------------------------------*/





    /*---------------------------------------------Define the return method to start-----------------------------------------------------*/

    /**
     * Functional Description: Method of successful operation - no return value
     *
     * @param: null
     * @return: Message
     * @auther: Sopp
     * @date: 2019/4/8 10:37
     */
    public static Message success() {
        return new Message(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS);
    }

    /**
     * Functional Description: The method of successful operation - there is a return value
     *
     * @param: [object]
     * @return: com.mds.common.Message
     * @auther: Sopp
     * @date: 2019/4/8 10:45
     */
    public static Message success(Object object) {
        return new Message(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS, object);
    }

    /**
     * Functional Description: Operation successful - with return value and paging information
     *
     * @param: [object, count]
     * @return: com.mds.common.Message
     * @auther: Sopp
     * @date: 2019/4/8 11:07
     */
    public static Message successList(Object object, Long count) {
        return new Message(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS, object, count);
    }

    /**
     * Functional Description: Operation failed
     *
     * @param: null
     * @return: com.mds.common.Message
     * @auther: Sopp
     * @date: 2019/4/8 10:59
     */
    public static Message fail() {
        return new Message(Constants.CODE_FAIL, Constants.MSG_FAIL);
    }

    /**
     * Functional Description: Error
     *
     * @param: [msg]
     * @return: com.mds.common.Message
     * @auther: Sopp
     * @date: 2019/4/8 11:02
     */
    public static Message error(String msg) {
        return new Message(Constants.CODE_ERROR, msg);
    }

    /**
     * Functional Description: No request data
     *
     * @param: null
     * @return: com.mds.common.Message
     * @auther: Sopp
     * @date: 2019/4/8 11:03
     */
    public static Message noData() {
        return new Message(Constants.CODE_NODATA, Constants.MSG_NODATA);
    }


    /**
     * Function description: Request parameter error
     *
     * @param: msg: The error message provided when returning is generally provided by the global exception class.
     * @return: com.mds.portable.common.message.Message
     * @auther: Sopp
     * @date: 2019/4/19 10:08
     */
    public static Message paramError(String msg) {
        return new Message(Constants.CODE_ERRORPARAM, msg);
    }

    /**
     * Function description: Server exception
     *
     * @param: : null
     * @return: com.mds.portable.common.message.Message
     * @auther: Sopp
     * @date: 2019/4/19 10:03
     */
    public static Message warning() {
        return new Message(Constants.CODE_WARNING, Constants.MSG_WARNING);
    }

    /**
     * description: No access
     *
     * @param
     * @return Message
     * @throws
     * @auther Hower
     * @date: 2019/6/5 15:40
     */
    public static Message permission() {
        return new Message(Constants.CODE_NOPERMISSION, Constants.MSG_REMIND);
    }

    /**
     * Function description: Request method error
     *
     * @param
     * @return com.mds.portable.common.message.Message
     * @throws
     * @auther Sopp
     * @date: 2019/5/5 14:54
     */
    public static Message methodError() {
        return new Message(Constants.CODE_METHODERROR, Constants.MSG_METHODERROR);
    }
    /*---------------------------------------------Define the end of the return method-----------------------------------------------------*/

}
