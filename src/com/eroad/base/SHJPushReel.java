package com.eroad.base;

import com.next.message.SHReel;
import com.next.message.SHResMsgM;

public class SHJPushReel extends SHReel {

public void processPackage(SHResMsgM msg){
	if(_delegate != null){
		_delegate.processPackage(msg);
	}
}
}
