package com.profound.common.kit;

public enum QueryCondition 
{
	/**等于*/				equal,
	/**不等于*/				not_equal,
	/**大于*/				large,
	/**大于等于*/			large_equal,
	/**小于*/				small,
	/**小于等于*/			small_equal,
	/**左右like(%xxx%)*/		like_anywhere,
	/**左like(xxx%)*/		like_start,
	/**右like(%xxx)*/		like_end,
	/**包含*/				in,
	/**不包含*/				not_in,
	/**是否为空(当参数值为1时为空,0时为非空)*/							if_null,
	/**在什么之间(参数值应为数组,between 数组[0] and 数组[1])*/			between,
	/**不在什么之间(参数值应为数组,not between 数组[0] and 数组[1])*/	not_between,
	/**在什么之间(一般用于日期或者数值范围约束)*/						large_small
}
