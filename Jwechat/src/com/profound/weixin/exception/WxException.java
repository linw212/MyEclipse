package com.profound.weixin.exception;

/**
 * 微信相关异常
 * @author Administrator
 *
 */
public class WxException extends RuntimeException {

	private static final long serialVersionUID = -400423612167742220L;
	public WxException(String message)
	{
		super(message);
	}
}
