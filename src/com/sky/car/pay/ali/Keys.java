/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.sky.car.pay.ali;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088911910671344";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "sunnyzu2015@163.com";

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMZBmxwqyzd6EKdIOy/ttWDImhh5CE8KyKKHOTnLYvYzWngbqyz9XkieUK8JzNL1x2iAAHMIR0KjMvkIc0NNEupiHlzSRE+UXBLaD6AtZqbGRp4gQ1BCIUMFf4maJpWDd4a5rjPRd4EdWXFpiJ1OK+fVRPHUEq2qo+V8YGBN9peDAgMBAAECgYEAkXGp6FUgtrdPJlngSf4atUEVg2SvisU/gbSwb8zc8f+knD64Ko5KL/4s9Oy8nft/ahOVwM5O8P0nNPlVDgeqGox4p9yR4GFOSvC8TQT/OaANVYxnDxrojMyfDmJ90ZVoFZqoWFIdcnyOITKdDOlMiXXfSrBSxr1olsFez20Ny/kCQQDl3oeWcZWsscw10EsuxrRdPbaAj53r0iSUfznrts76G4jpxx3Jt2vTBan5ttbopirFQzjp8+yf/C8nxFh/rk2/AkEA3MsZwnugjGHojQwFOyHEa6Y8v8d8QBYNPfy+ZyUWXzjzTYPPP5Grx1WQaKKg4hPjzUqLIW9C3/zTGXCauNgvPQJAF88ItChnnE9+G0TLxLiIDfBKat51OOu3JjpXQXbZ0UXw1GXvS+lXna/EfzzU3Zv4ah4gUvsU0y1HpRLNJ8xh+QJBANGMNMN+v43ccnBDA0fusVOVrzw4YLUV+LVnBXIxIb13+HoN6gkkAMsrauXTyyslw2MYJ1mxFeAtPFB9rWpXSuUCQQC+kx0rwlIMXhewcQ06rNix8mbf//tiQsHUqIi0+dttvBLPK8mLD8omjb2I+AXe+sYKK9Nhc3X7kOODfFWBd48I";

	//支付宝公钥
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
}
